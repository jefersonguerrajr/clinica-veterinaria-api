package com.proway_upskilling.clinica_veterinaria_api.repository;

import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByNomeLikeIgnoreCase(String nome, Pageable pageable);
}
