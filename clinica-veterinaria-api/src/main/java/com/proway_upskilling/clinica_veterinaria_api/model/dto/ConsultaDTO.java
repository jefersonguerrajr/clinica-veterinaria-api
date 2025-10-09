package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaDTO {
    private Long id;
    private Long petId;
    private Long veterinarioId;
    private Long clienteId;
    private String motivo;
    private String diagnostico;
    private String tratamento;
    private String status;

    private String nomePet;
    private String nomeVeterinario;
}