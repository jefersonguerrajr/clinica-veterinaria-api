package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import org.springframework.data.domain.Page;

public interface IPetService {
    PetResponseDTO create(PetRequestDTO petDTO);
    PetResponseDTO findById(Long id);
    Page<PetResponseDTO> findAll(int page, int size, String sort, String direction);
    PetResponseDTO update(Long id, PetRequestDTO petDTO);
    void delete(Long id);
}
