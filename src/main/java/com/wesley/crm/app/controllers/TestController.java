package com.wesley.crm.app.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/ip")
    public ResponseEntity<Map<String, Object>> getIpInfo(HttpServletRequest request) {
        Map<String, Object> ipInfo = new HashMap<>();
        
        // Headers relacionados a IP
        ipInfo.put("X-Forwarded-For", request.getHeader("X-Forwarded-For"));
        ipInfo.put("X-Real-IP", request.getHeader("X-Real-IP"));
        ipInfo.put("X-Originating-IP", request.getHeader("X-Originating-IP"));
        ipInfo.put("CF-Connecting-IP", request.getHeader("CF-Connecting-IP"));
        ipInfo.put("True-Client-IP", request.getHeader("True-Client-IP"));
        ipInfo.put("Proxy-Client-IP", request.getHeader("Proxy-Client-IP"));
        ipInfo.put("WL-Proxy-Client-IP", request.getHeader("WL-Proxy-Client-IP"));
        
        // IP direto
        ipInfo.put("Remote-Addr", request.getRemoteAddr());
        ipInfo.put("Remote-Host", request.getRemoteHost());
        ipInfo.put("Remote-Port", request.getRemotePort());
        
        // Informações adicionais
        ipInfo.put("Server-Name", request.getServerName());
        ipInfo.put("Server-Port", request.getServerPort());
        ipInfo.put("Local-Addr", request.getLocalAddr());
        ipInfo.put("Local-Name", request.getLocalName());
        ipInfo.put("Local-Port", request.getLocalPort());
        
        return ResponseEntity.ok(ipInfo);
    }
}