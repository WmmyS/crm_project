package com.wesley.crm.app.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCep {
    String message() default "CEP deve conter 8 dígitos (formato: XXXXX-XXX ou XXXXXXXX)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}