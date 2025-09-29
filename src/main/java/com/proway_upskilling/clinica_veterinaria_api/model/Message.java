package com.proway_upskilling.clinica_veterinaria_api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Message {
    private String title;
    private String description;
    private LocalDateTime timestamp;
}
