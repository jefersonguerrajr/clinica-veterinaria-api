package com.proway_upskilling.clinica_veterinaria_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especie;

    @Column(nullable = false)
    private String raca;

    @Column(nullable = false)
    private Double peso;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "idade_aproximada")
    private Double idadeAproximada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    public Double getIdadeAnosEMeses() {
        if (dataNascimento != null) {
            LocalDate hoje = LocalDate.now();
            Period p = Period.between(dataNascimento, hoje);

            int anos = p.getYears();
            int meses = p.getMonths();

            double idade = anos + meses / 12.0;

            return BigDecimal.valueOf(idade)
                    .setScale(1, RoundingMode.CEILING)
                    .doubleValue();
        } else if (idadeAproximada != null) {
            return BigDecimal.valueOf(idadeAproximada)
                    .setScale(1, RoundingMode.CEILING)
                    .doubleValue();
        } else {
            return null;
        }
    }
}