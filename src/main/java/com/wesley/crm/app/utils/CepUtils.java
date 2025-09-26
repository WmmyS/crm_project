package com.wesley.crm.app.utils;

public class CepUtils {
    
    public static String formatCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return cep;
        }
        
        // Remove espaços e hífens
        String cleanCep = cep.replaceAll("[\\s-]", "");
        
        // Se tem 8 dígitos, formata como XXXXX-XXX
        if (cleanCep.matches("\\d{8}")) {
            return cleanCep.substring(0, 5) + "-" + cleanCep.substring(5);
        }
        
        return cep; // Retorna original se não conseguir formatar
    }
}