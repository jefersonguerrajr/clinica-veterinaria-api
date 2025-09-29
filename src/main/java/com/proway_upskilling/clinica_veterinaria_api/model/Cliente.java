package com.proway_upskilling.clinica_veterinaria_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String nome;

    @Column(name = "telephone")
    private String telefone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String endereco;

}
