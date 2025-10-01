package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaDTO {
    private Long id;
    private Long petId;
    private Long veterinarioId;
    private String motivo;
    private String diagnostico;
    private String tratamento;
    private String status;

    private String nomePet;
    private String nomeVeterinario;
}