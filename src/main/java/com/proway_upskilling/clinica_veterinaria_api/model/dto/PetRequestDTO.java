package com.proway_upskilling.clinica_veterinaria_api.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.proway_upskilling.clinica_veterinaria_api.validation.annotation.PetIdadeExclusive;
import com.proway_upskilling.clinica_veterinaria_api.validation.deserializer.LocalDateDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Dados do Pet")
@PetIdadeExclusive
public class PetRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do pet", example = "Fido", required = true)
    private String nome;

    @NotBlank(message = "Espécie é obrigatória")
    @Schema(description = "Espécie do pet", example = "Cachorro", required = true)
    private String especie;

    @NotBlank(message = "Raça é obrigatória")
    @Schema(description = "Raça do pet", example = "Labrador", required = true)
    private String raca;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Past(message = "Data de nascimento deve ser no passado")
    @Schema(description = "Data de nascimento do pet", example = "2021-05-20", type = "string", format = "date", nullable = true)
    private LocalDate dataNascimento;

    @Positive(message = "Idade aproximada deve ser maior que zero")
    @Schema(description = "Idade aproximada em anos (ex: 0.5 = 6 meses)", example = "1.4", nullable = true)
    private Double idadeAproximada;

    @NotNull(message = "Peso é obrigatório")
    @Positive(message = "Peso deve ser maior que zero")
    @Schema(description = "Peso do pet em kg", example = "12.5", required = true)
    private Double peso;

    @NotNull(message = "ID do cliente é obrigatório")
    @Schema(description = "ID do tutor/cliente do pet", example = "1", required = true)
    private Long clienteId;

}



