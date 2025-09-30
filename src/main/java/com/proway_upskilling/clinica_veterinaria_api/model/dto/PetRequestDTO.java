package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Espécie é obrigatória")
    private String especie;

    @NotBlank(message = "Raça é obrigatória")
    private String raca;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull(message = "Peso é obrigatório")
    @Positive(message = "Peso deve ser maior que zero")
    private Double peso;

    @NotNull(message = "ID do dono é obrigatório")
    private Long donoId;


}



