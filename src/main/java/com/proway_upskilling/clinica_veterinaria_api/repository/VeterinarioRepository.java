package com.proway_upskilling.clinica_veterinaria_api.repository;

import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long>, JpaSpecificationExecutor<Veterinario> {

    boolean existsByCrmv(@NonNull String crmv);

    Page<Veterinario> findVeterinarioByDataContratacaoGreaterThanEqual(@NonNull LocalDate dataContratacao, Pageable pageable);
}
