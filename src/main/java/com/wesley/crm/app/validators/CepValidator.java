package com.wesley.crm.app.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<ValidCep, String> {

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.trim().isEmpty()) {
            return true; // Permite null/vazio, use @NotBlank se obrigatório
        }
        
        // Remove espaços e hífens
        String cleanCep = cep.replaceAll("[\\s-]", "");
        
        // Valida se tem exatamente 8 dígitos
        return cleanCep.matches("\\d{8}");
    }
}