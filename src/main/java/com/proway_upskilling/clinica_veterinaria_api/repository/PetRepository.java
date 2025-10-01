package com.proway_upskilling.clinica_veterinaria_api.repository;

import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {
    Page<Pet> findByClienteId(Long clienteId, Pageable pageable);
}
