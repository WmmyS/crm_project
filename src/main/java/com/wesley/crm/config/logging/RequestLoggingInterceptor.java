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

        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("loggedUser", username);
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
        saveAuditLogAsync(username, (String) request.getAttribute("loggedUserRole"), 
                         method, uri, (String) request.getAttribute("clientIp"), 
                         status, duration, errorMessage, requestId);
        
        // Limpar RequestId da thread
        requestIdGenerator.clearRequestId();
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
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