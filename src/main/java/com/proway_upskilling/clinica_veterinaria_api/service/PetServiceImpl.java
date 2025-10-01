package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.mapper.PetMapper;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import com.proway_upskilling.clinica_veterinaria_api.specification.PetSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository repository;
    private final ClienteRepository clienteRepository;
    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository repository, ClienteRepository clienteRepository, PetMapper petMapper) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.petMapper = petMapper;
    }

    @Override
    public PetResponseDTO create(PetRequestDTO petDTO) {
        Cliente cliente = clienteRepository.findById(petDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + petDTO.getClienteId()));

        Pet pet = petMapper.toEntity(petDTO, cliente);
        Pet salvo = repository.save(pet);
        return petMapper.toResponseDTO(salvo);
    }

    @Override
    public PetResponseDTO findById(Long id) {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado!"));
        return petMapper.toResponseDTO(pet);
    }

    @Override
    public Page<PetResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(petMapper::toResponseDTO);
    }

    @Override
    public PetResponseDTO update(Long id, PetRequestDTO petDTO) {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado!"));

        Cliente cliente = clienteRepository.findById(petDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        PetMapper.updateEntity(pet, petDTO, cliente);
        Pet updatedPet = repository.save(pet);
        return petMapper.toResponseDTO(updatedPet);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pet não encontrado!");
        }
        repository.deleteById(id);
    }

    @Override
    public Page<PetResponseDTO> findByCliente(Long clienteId, Pageable pageable) {
        return repository.findByClienteId(clienteId,pageable)
                .map(petMapper::toResponseDTO);
    }

    @Override
    public Page<PetResponseDTO> search(String nome, String especie, String raca, Double peso, Double idadeMin, Double idadeMax, Pageable pageable){
        Specification<Pet> spec = Specification.where(PetSpecification.hasNome(nome))
                .and(PetSpecification.hasEspecie(especie))
                .and(PetSpecification.hasRaca(raca))
                .and(PetSpecification.hasPeso(peso))
                .and(PetSpecification.hasIdadeBetween(idadeMin, idadeMax));

        return repository.findAll(spec, pageable)
                .map(petMapper::toResponseDTO);

    }
}
