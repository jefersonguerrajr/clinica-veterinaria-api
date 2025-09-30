package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/veterinario")
public class VeterinarioController {

    private final VeterinarioService service;

    public VeterinarioController(VeterinarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VeterinarioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> findAll(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<VeterinarioDTO> create(@Valid @RequestBody VeterinarioDTO dto) {
        dto = service.create(dto);
        return ResponseEntity.created(URI.create("veterinario" + dto.getId())).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> update(@PathVariable Long id, @Valid @RequestBody VeterinarioDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
