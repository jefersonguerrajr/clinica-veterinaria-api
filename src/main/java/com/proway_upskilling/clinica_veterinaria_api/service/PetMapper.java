package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet toEntity(PetRequestDTO dto, Cliente cliente) {
        Pet pet = new Pet();
        pet.setNome(dto.getNome());
        pet.setEspecie(dto.getEspecie());
        pet.setRaca(dto.getRaca());
        pet.setDataNascimento(dto.getDataNascimento());
        pet.setIdadeAproximada(dto.getIdadeAproximada());
        pet.setPeso(dto.getPeso());
        pet.setCliente(cliente);
        return pet;
    }

    public PetResponseDTO toResponseDTO(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(pet.getId());
        dto.setNome(pet.getNome());
        dto.setEspecie(pet.getEspecie());
        dto.setRaca(pet.getRaca());
        dto.setDataNascimento(pet.getDataNascimento());
        dto.setIdade(pet.getIdadeAnosEMeses());
        dto.setPeso(pet.getPeso());
        dto.setClienteId(pet.getCliente() != null ? pet.getCliente().getId() : null);
        return dto;
    }

    public static void updateEntity(Pet pet, PetRequestDTO dto, Cliente cliente) {
        pet.setNome(dto.getNome());
        pet.setEspecie(dto.getEspecie());
        pet.setRaca(dto.getRaca());
        pet.setDataNascimento(dto.getDataNascimento());
        pet.setIdadeAproximada(dto.getIdadeAproximada());
        pet.setPeso(dto.getPeso());
        pet.setCliente(cliente);
    }

}

