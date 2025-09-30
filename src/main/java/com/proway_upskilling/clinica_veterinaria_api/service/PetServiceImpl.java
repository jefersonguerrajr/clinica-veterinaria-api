package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import org.springframework.data.domain.Page;

public class PetServiceImpl implements IPetService {

    private final PetRepository repository;
    private final ClienteRepository clienteRepository;

    public PetServiceImpl(PetRepository repository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public PetResponseDTO create(PetRequestDTO petDTO) {
        Cliente dono = clienteRepository.findById(petDTO.getDonoId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id " + petDTO.getDonoId()));

        Pet pet = PetMapper.toEntity(petDTO, dono);
        Pet salvo = repository.save(pet);
        return PetMapper.toResponseDTO(salvo);

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
