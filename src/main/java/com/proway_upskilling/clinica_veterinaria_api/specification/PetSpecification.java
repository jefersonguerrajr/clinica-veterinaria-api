package com.proway_upskilling.clinica_veterinaria_api.specification;

import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import org.springframework.data.jpa.domain.Specification;

public class PetSpecification {

    public static Specification<Pet> hasNome(String nome){
        return ((root, query, cb) ->
                nome == null ? null : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
    }

    public static Specification<Pet> hasEspecie(String especie){
        return ((root, query, cb) ->
                especie == null ? null : cb.like(cb.lower(root.get("especie")), "%" + especie.toLowerCase() + "%"));
    }

    public static Specification<Pet> hasRaca(String raca){
        return ((root, query, cb) ->
                raca == null ? null : cb.like(cb.lower(root.get("raca")), "%" + raca.toLowerCase() + "%"));
    }

    public static Specification<Pet> hasPeso(Double peso){
        return ((root, query, cb) ->
                peso == null ? null : cb.equal(root.get("peso"), peso));
    }
}
