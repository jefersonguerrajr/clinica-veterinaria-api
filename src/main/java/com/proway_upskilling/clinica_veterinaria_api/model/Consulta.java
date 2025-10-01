package com.proway_upskilling.clinica_veterinaria_api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @Column(length = 150, nullable = false)
    private String motivo;

    @Column(length = 250)
    private String diagnostico;

    @Column(length = 250)
    private String tratamento;

    @Column(length = 1, nullable = false)
    private String status; // Exemplo: "A"=Ativa, "F"=Finalizada
}