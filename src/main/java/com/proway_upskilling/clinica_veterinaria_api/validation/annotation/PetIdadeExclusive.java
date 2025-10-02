package com.proway_upskilling.clinica_veterinaria_api.validation.annotation;

import com.proway_upskilling.clinica_veterinaria_api.validation.validator.PetIdadeExclusiveValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PetIdadeExclusiveValidator.class)
@Documented
public @interface PetIdadeExclusive {
    String message() default "Informe apenas a data de nascimento ou a idade aproximada, nunca os dois.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}