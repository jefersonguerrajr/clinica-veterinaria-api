package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VeterinarioDTO {

    private long id;

    @NotNull(message = "Nome é obrigatório!")
    @Size(max = 80)
    private String nome;

    @NotNull(message = "Especialidade é obrigatório!")
    @Size(max = 40)
    private String especialiade;

    @NotNull(message = "CRMV é obrigatório!")
    @Size(max = 13)
    private String crmv;

    private LocalDate dataContratacao;

    private List<Consulta> consultas;
}
