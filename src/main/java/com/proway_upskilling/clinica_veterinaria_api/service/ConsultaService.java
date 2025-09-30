package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import com.proway_upskilling.clinica_veterinaria_api.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {
    private final ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    public List<Consulta> listarTodos() {
        return repository.findAll();
    }
}
