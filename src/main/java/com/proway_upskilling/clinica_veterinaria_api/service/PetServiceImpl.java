package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.dtos.pet.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.dtos.pet.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import org.springframework.data.domain.Page;

public class PetServiceImpl implements IPetService {

    private final PetRepository repository;

    public PetServiceImpl(PetRepository repository) {
        this.repository = repository;
    }

    @Override
    public PetResponseDTO create(PetRequestDTO petDTO) {
        return null;
    }

    @Override
    public PetResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public Page<PetResponseDTO> findAll(int page, int size, String sort, String direction) {
        return null;
    }

    @Override
    public PetResponseDTO update(Long id, PetRequestDTO petDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
