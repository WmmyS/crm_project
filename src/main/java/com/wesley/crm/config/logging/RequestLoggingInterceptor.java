package com.wesley.crm.config.logging;

import com.wesley.crm.config.logging.RequestIdGenerator;
import com.wesley.crm.domain.entities.AuditLog;
import com.wesley.crm.domain.entities.User;
import com.wesley.crm.infra.database.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private RequestIdGenerator requestIdGenerator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Gerar RequestId único
        String requestId = requestIdGenerator.generateRequestId();
        requestIdGenerator.setRequestId(requestId);
        
        // Adicionar RequestId no header de resposta
        response.setHeader(requestIdGenerator.getRequestIdHeader(), requestId);
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = getClientIpAddress(request);
        
        String username = "ANONYMOUS";
        String userRole = "NONE";
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            username = user.getUsername();
            userRole = user.getRole().toString();
        }

        logger.info("🌐 REQUEST [{}] | {} {} | User: {} ({}) | IP: {}", 
                requestId, method, uri, username, userRole, ip);
        
        // Debug: Log todos os headers relacionados a IP para troubleshooting
        if (logger.isDebugEnabled()) {
            logger.debug("IP Headers - X-Forwarded-For: {}, X-Real-IP: {}, Remote-Addr: {}", 
                    request.getHeader("X-Forwarded-For"), 
                    request.getHeader("X-Real-IP"), 
                    request.getRemoteAddr());
        }

        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("loggedUser", username);
        request.setAttribute("loggedUserRole", userRole);
        request.setAttribute("clientIp", ip);
        request.setAttribute("requestId", requestId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String username = (String) request.getAttribute("loggedUser");
        String requestId = (String) request.getAttribute("requestId");
        int status = response.getStatus();

        String errorMessage = null;
        if (ex != null) {
            errorMessage = ex.getMessage();
            logger.error("❌ ERROR [{}] | {} {} | User: {} | Status: {} | Duration: {}ms | Error: {}", 
                    requestId, method, uri, username, status, duration, errorMessage, ex);
        } else {
            String statusIcon = getStatusIcon(status);
            logger.info("{} RESPONSE [{}] | {} {} | User: {} | Status: {} | Duration: {}ms", 
                    statusIcon, requestId, method, uri, username, status, duration);
        }

        // Salvar no banco de dados (async)
        String userRole = (String) request.getAttribute("loggedUserRole");
        String clientIp = (String) request.getAttribute("clientIp");
        saveAuditLogAsync(username, userRole, method, uri, clientIp, 
                         status, duration, errorMessage, requestId);
        
        // Limpar RequestId da thread
        requestIdGenerator.clearRequestId();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        // Lista de headers comuns para capturar IP real
        String[] ipHeaders = {
            "X-Forwarded-For",
            "X-Real-IP", 
            "X-Originating-IP",
            "CF-Connecting-IP", // Cloudflare
            "True-Client-IP",   // Akamai
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : ipHeaders) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // Se contém múltiplos IPs (separados por vírgula), pega o primeiro
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                // Valida se é um IP válido (não localhost interno)
                if (isValidIp(ip)) {
                    return ip;
                }
            }
        }
        
        // Fallback para o IP direto da requisição
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr != null ? remoteAddr : "unknown";
    }
    
    private boolean isValidIp(String ip) {
        // Rejeita IPs inválidos ou internos comuns
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        
        // Rejeita localhost e IPs internos comuns
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1") || ip.equals("::1")) {
            return false;
        }
        
        return true;
    }

    private String getStatusIcon(int status) {
        if (status >= 200 && status < 300) return "✅";
        if (status >= 300 && status < 400) return "🔄";
        if (status >= 400 && status < 500) return "⚠️";
        if (status >= 500) return "💥";
        return "ℹ️";
    }

    @Async
    public void saveAuditLogAsync(String username, String userRole, String method, 
                                 String endpoint, String ipAddress, Integer statusCode, 
                                 Long durationMs, String errorMessage, String requestId) {
        try {
            AuditLog auditLog = new AuditLog(username, userRole, method, endpoint, 
                                           ipAddress, statusCode, durationMs, errorMessage, requestId);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            logger.error("Erro ao salvar log de auditoria [{}]: {}", requestId, e.getMessage());
        }
    }
}