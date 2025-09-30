package com.proway_upskilling.clinica_veterinaria_api.model;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Consulta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String motivo;

    @Column(length = 250)
    private String diagnostico;

    @Column(length = 200)
    private String tratamento;

    @Column(length = 1)
    private String situacao;
}