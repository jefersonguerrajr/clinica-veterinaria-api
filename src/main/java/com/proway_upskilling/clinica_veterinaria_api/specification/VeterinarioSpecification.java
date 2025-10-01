package com.proway_upskilling.clinica_veterinaria_api.specification;

import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VeterinarioSpecification {

    public static Specification<Veterinario> comNome(String nome) {
        return ((root, query, cb) ->
                nome == null
                        ? null
                        : cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
    }

    public static Specification<Veterinario> comEspecialidade(String especialidade) {
        return ((root, query, cb) ->
                especialidade == null
                        ? null
                        : cb.like(cb.lower(root.get("especialidade")), "%" + especialidade.toLowerCase() + "%"));
    }

    public static Specification<Veterinario> comCrmv(String crmv) {
        return ((root, query, cb) ->
                crmv == null
                        ? null
                        : cb.like(cb.lower(root.get("crmv")), "%" + crmv.toLowerCase() + "%"));
    }

    public static Specification<Veterinario> comDataContratacao(LocalDate dataContratacao) {
        return ((root, query, cb) ->
                dataContratacao == null
                        ? null
                        : cb.equal(root.get("dataContratacao"), dataContratacao));
    }
}
