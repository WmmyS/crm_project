package com.wesley.crm.app.controllers.logging;

import com.wesley.crm.app.controllers.BaseController;
import com.wesley.crm.domain.entities.AuditLog;
import com.wesley.crm.infra.database.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "📋 Auditoria", description = "Consulta de logs de auditoria")
public class AuditController extends BaseController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping
    @Operation(summary = "📋 Listar logs de auditoria", description = "🔐 **Requer Autenticação Dupla** - Lista logs paginados.")
    public Page<AuditLog> listarLogs(
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @GetMapping("/request/{requestId}")
    @Operation(summary = "🔍 Buscar por RequestId", description = "🔐 **Requer Autenticação Dupla** - Busca logs por RequestId específico.")
    public ResponseEntity<AuditLog> buscarPorRequestId(@PathVariable String requestId) {
        Optional<AuditLog> log = auditLogRepository.findAll().stream()
                .filter(audit -> requestId.equals(audit.getRequestId()))
                .findFirst();
        
        return log.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}