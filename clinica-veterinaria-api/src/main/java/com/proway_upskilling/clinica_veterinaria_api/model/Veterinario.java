package com.proway_upskilling.clinica_veterinaria_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "veterinario")
public class Veterinario {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 80, nullable = false)
    private String nome;

    @Column(name = "specialty", length = 40, nullable = false)
    private String especialidade;

    @Column(name = "crmv", length = 13, nullable = false)
    private String crmv;

    @Column(name = "hiredate", nullable = false)
    private LocalDate dataContratacao;

    @OneToMany(mappedBy = "veterinario")
    private List<Consulta> consultas;
}
