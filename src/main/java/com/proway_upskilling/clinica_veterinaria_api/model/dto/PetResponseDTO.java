package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PetResponseDTO {
    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNascimento;
    private Double peso;
    private Long donoId;
}
