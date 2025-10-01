package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import com.proway_upskilling.clinica_veterinaria_api.specification.PetSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements IPetService {

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
        Cliente dono = clienteRepository.findById(petDTO.getDonoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id " + petDTO.getDonoId()));

        Pet pet = petMapper.toEntity(petDTO, dono);
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

        Cliente dono = clienteRepository.findById(petDTO.getDonoId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        PetMapper.updateEntity(pet, petDTO, dono);
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
        return repository.findByDonoId(clienteId,pageable)
                .map(petMapper::toResponseDTO);
    }

    public Page<PetResponseDTO> search(String nome, String especie, String raca, Double peso, Pageable pageable){
        Specification<Pet> spec = Specification.where(PetSpecification.hasNome(nome))
                .and(PetSpecification.hasEspecie(especie))
                .and(PetSpecification.hasRaca(raca))
                .and(PetSpecification.hasPeso(peso));

        return repository.findAll(spec, pageable)
                .map(petMapper::toResponseDTO);

    }
}
