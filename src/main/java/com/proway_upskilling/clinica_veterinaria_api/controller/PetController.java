package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.IPetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<PetResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
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

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PetResponseDTO>> getAllByCliente(@PathVariable Long clienteId, Pageable pageable){
        return ResponseEntity.ok(service.findByCliente(clienteId,pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PetResponseDTO>> searchPets(@RequestParam(required = false) String nome,
                                                           @RequestParam(required = false) String especie,
                                                           @RequestParam(required = false) String raca,
                                                           @RequestParam(required = false) Double peso,
                                                           Pageable pageable){
        return ResponseEntity.ok(service.search(nome, especie, raca, peso, pageable));

    }
}
