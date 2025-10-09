package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.controller.docs.IPetControllerDocs;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.ConsultaService;
import com.proway_upskilling.clinica_veterinaria_api.service.PetService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController implements IPetControllerDocs {

    private final PetService service;

    private final ConsultaService consultaService;

    public PetController(PetService service, ConsultaService consultaService) {
        this.service = service;
        this.consultaService = consultaService;
    }

    @Override
    public ResponseEntity<PetResponseDTO> create(@Valid @RequestBody PetRequestDTO dto) {
        PetResponseDTO response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PetResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<Page<PetResponseDTO>> getAll(int page, int size, String sort) {
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Override
    public ResponseEntity<PetResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PetRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<PetResponseDTO>> getAllByCliente(@PathVariable Long clienteId, int page, int size, String sort){
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(service.findByCliente(clienteId,pageable));
    }

    @Override
    public ResponseEntity<Page<PetResponseDTO>> searchPets(String nome, String especie, String raca, Double peso, Double idadeMin, Double idadeMax,
                                                           int page, int size, String sort){
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(service.search(nome, especie, raca, peso, idadeMin, idadeMax, pageable));
    }

    @Override
    public ResponseEntity<Page<ConsultaDTO>> getConsultasByPet(@PathVariable Long petId, int page, int size, String sort) {
        Pageable pageable = buildPageable(page, size, sort);
        return ResponseEntity.ok(consultaService.buscarConsultasPorPetId(petId,pageable));
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        String sortBy = sortParams[0];
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

}
