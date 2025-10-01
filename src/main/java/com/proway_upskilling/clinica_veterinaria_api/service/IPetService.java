package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPetService {
    PetResponseDTO create(PetRequestDTO petDTO);
    PetResponseDTO findById(Long id);
    Page<PetResponseDTO> findAll(Pageable pageable);
    PetResponseDTO update(Long id, PetRequestDTO petDTO);
    void delete(Long id);
    Page<PetResponseDTO> findByCliente(Long clienteId, Pageable pageable);
    Page<PetResponseDTO> search(String nome, String especie, String raca, Double peso, Pageable pageable);
}
