package com.wesley.crm.config.logging;

import com.wesley.crm.infra.database.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LogCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(LogCleanupService.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Scheduled(cron = "0 0 2 * * ?") // Todo dia às 2h da manhã
    @Transactional
    public void cleanupOldLogs() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(90);
            int deletedCount = auditLogRepository.deleteOldLogs(cutoffDate);
            
            if (deletedCount > 0) {
                logger.info("🧹 CLEANUP | Removidos {} logs antigos (> 90 dias)", deletedCount);
            }
        } catch (Exception e) {
            logger.error("❌ Erro na limpeza de logs: {}", e.getMessage(), e);
        }
    }
}