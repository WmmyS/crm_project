package com.wesley.crm.config.logging;

import com.wesley.crm.domain.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    public void logAction(String action, String entity, Long entityId, String details) {
        String username = getCurrentUsername();
        auditLogger.info("📋 AUDIT | User: {} | Action: {} | Entity: {} | ID: {} | Details: {}", 
                username, action, entity, entityId, details);
    }

    public void logCreate(String entity, Long entityId, String details) {
        logAction("CREATE", entity, entityId, details);
    }

    public void logUpdate(String entity, Long entityId, String details) {
        logAction("UPDATE", entity, entityId, details);
    }

    public void logDelete(String entity, Long entityId, String details) {
        logAction("DELETE", entity, entityId, details);
    }

    public void logLogin(String username, String ip) {
        auditLogger.info("🔐 LOGIN | User: {} | IP: {}", username, ip);
    }

    public void logLogout(String username, String ip) {
        auditLogger.info("🚪 LOGOUT | User: {} | IP: {}", username, ip);
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            return user.getUsername();
        }
        return "SYSTEM";
    }
}