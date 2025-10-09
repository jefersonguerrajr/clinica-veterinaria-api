package com.proway_upskilling.clinica_veterinaria_api.specification;

import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PetSpecification {

    public static Specification<Pet> hasNome(String nome) {
        return ((root, query, cb) ->
                nome == null ? null : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
    }

    public static Specification<Pet> hasEspecie(String especie) {
        return ((root, query, cb) ->
                especie == null ? null : cb.like(cb.lower(root.get("especie")), "%" + especie.toLowerCase() + "%"));
    }

    public static Specification<Pet> hasRaca(String raca) {
        return ((root, query, cb) ->
                raca == null ? null : cb.like(cb.lower(root.get("raca")), "%" + raca.toLowerCase() + "%"));
    }

    public static Specification<Pet> hasPeso(Double peso) {
        return ((root, query, cb) ->
                peso == null ? null : cb.equal(root.get("peso"), peso));
    }

    public static Specification<Pet> hasIdadeBetween(Double idadeMin, Double idadeMax) {
        return (root, query, cb) -> {
            if (idadeMin == null && idadeMax == null) return null;

            double margem = 0.05;
            double min = (idadeMin != null) ? idadeMin : Double.MIN_VALUE;
            double max = (idadeMax != null) ? idadeMax : Double.MAX_VALUE;

            if (idadeMin != null && idadeMax != null && idadeMin.equals(idadeMax)) {
                min = idadeMin - margem;
                max = idadeMax + margem;
            }

            LocalDate hoje = LocalDate.now();

            Predicate predData = cb.conjunction();
            if (idadeMin != null) {
                int anosMin = (int)Math.floor(min);
                int mesesMin = (int)Math.round((min - anosMin) * 12);
                LocalDate dataMax = hoje.minusYears(anosMin).minusMonths(mesesMin);
                predData = cb.and(predData, cb.lessThanOrEqualTo(root.get("dataNascimento"), dataMax));
            }
            if (idadeMax != null) {
                int anosMax = (int)Math.floor(max);
                int mesesMax = (int)Math.round((max - anosMax) * 12);
                LocalDate dataMin = hoje.minusYears(anosMax).minusMonths(mesesMax);
                predData = cb.and(predData, cb.greaterThanOrEqualTo(root.get("dataNascimento"), dataMin));
            }

            Predicate predAprox = cb.conjunction();
            if (idadeMin != null) {
                predAprox = cb.and(predAprox, cb.greaterThanOrEqualTo(root.get("idadeAproximada"), min));
            }
            if (idadeMax != null) {
                predAprox = cb.and(predAprox, cb.lessThanOrEqualTo(root.get("idadeAproximada"), max));
            }

            return cb.or(predData, predAprox);
        };
    }
}