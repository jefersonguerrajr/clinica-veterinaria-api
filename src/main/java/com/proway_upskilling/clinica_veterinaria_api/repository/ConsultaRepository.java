package com.proway_upskilling.clinica_veterinaria_api.repository;

import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findByPetId(Long petId, Pageable pageable);

}