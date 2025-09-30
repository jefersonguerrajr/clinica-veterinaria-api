package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.IPetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    private final IPetService service;

    public PetController(IPetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> create(@Valid @RequestBody PetRequestDTO dto) {
        PetResponseDTO response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PetResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(service.findAll(page, size, sortBy, direction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody PetRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
