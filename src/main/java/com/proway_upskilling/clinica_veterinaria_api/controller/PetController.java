package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.controller.docs.IPetControllerDocs;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.IPetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController implements IPetControllerDocs {

    private final IPetService service;

    public PetController(IPetService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<PetResponseDTO> create(PetRequestDTO dto) {
        PetResponseDTO response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PetResponseDTO> getById(Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<Page<PetResponseDTO>> getAll(int page, int size, String sort) {
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Override
    public ResponseEntity<PetResponseDTO> update(Long id, PetRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<PetResponseDTO>> getAllByCliente(Long clienteId, int page, int size, String sort){
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(service.findByCliente(clienteId,pageable));
    }

    @Override
    public ResponseEntity<Page<PetResponseDTO>> searchPets(String nome, String especie, String raca, Double peso, Double idadeMin, Double idadeMax,
                                                           int page, int size, String sort){
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(service.search(nome, especie, raca, peso, idadeMin, idadeMax, pageable));
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        String sortBy = sortParams[0];
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

}
