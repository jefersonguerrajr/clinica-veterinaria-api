package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteDTO {

    @NotBlank(message = "nome obrigatório")
    private String name;
    @NotBlank(message = "email obrigatório")
    private String email;
    private String telephone;
    private String address;

}
