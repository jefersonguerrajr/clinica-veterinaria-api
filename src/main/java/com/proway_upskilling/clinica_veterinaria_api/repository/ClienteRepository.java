package com.proway_upskilling.clinica_veterinaria_api.repository;

import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
