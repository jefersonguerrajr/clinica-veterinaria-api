package com.proway_upskilling.clinica_veterinaria_api.validation.validator;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.validation.annotation.PetIdadeExclusive;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PetIdadeExclusiveValidator implements ConstraintValidator<PetIdadeExclusive, PetRequestDTO> {

    @Override
    public boolean isValid(PetRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        boolean hasDataNascimento = dto.getDataNascimento() != null;
        boolean hasIdadeAproximada = dto.getIdadeAproximada() != null;

        if (hasDataNascimento && hasIdadeAproximada) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Informe apenas data de nascimento ou idade aproximada, nunca ambos.")
                    .addPropertyNode("dataNascimento")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}