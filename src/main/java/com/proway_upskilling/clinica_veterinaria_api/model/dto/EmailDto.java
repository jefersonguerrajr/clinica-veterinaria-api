package com.proway_upskilling.clinica_veterinaria_api.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDto {
    private String emailFrom;
    private String emailTo;
    private String subject;
    private String text;
}
