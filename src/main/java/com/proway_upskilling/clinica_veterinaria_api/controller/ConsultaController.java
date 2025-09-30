package com.proway_upskilling.clinica_veterinaria_api.controller;

import java.util.List;

import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import com.proway_upskilling.clinica_veterinaria_api.service.ConsultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
