package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.mapper.PetMapper;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void shouldCreatePetSuccessfully(){
        PetRequestDTO petDTO = buildPetRequestDTO();
        Cliente cliente = buildCliente();
        Pet petEntity = buildPetEntity(cliente, petDTO);
        Pet savedPet = buildPetEntity(cliente, petDTO);
        savedPet.setId(1L);
        PetResponseDTO responseDTO = buildPetResponseDTO(savedPet);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(petMapper.toEntity(petDTO, cliente)).thenReturn(petEntity);
        when(petRepository.save(petEntity)).thenReturn(savedPet);
        when(petMapper.toResponseDTO(savedPet)).thenReturn(responseDTO);

        PetResponseDTO result = petService.create(petDTO);

        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getNome(), result.getNome());
        assertEquals(responseDTO.getEspecie(), result.getEspecie());
        assertEquals(responseDTO.getRaca(), result.getRaca());
        assertEquals(responseDTO.getDataNascimento(), result.getDataNascimento());
        assertEquals(responseDTO.getIdade(), result.getIdade());
        assertEquals(responseDTO.getPeso(), result.getPeso());
        assertEquals(responseDTO.getClienteId(), result.getClienteId());

        verify(petRepository).save(petEntity);
    }

    @Test
    void shouldThrowExceptionWhenCreatingPetWithNonExistentCliente() {
        PetRequestDTO petDTO = buildPetRequestDTO();

        when(clienteRepository.findById(petDTO.getClienteId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.create(petDTO)
        );

        assertEquals("Cliente não encontrado com id " + petDTO.getClienteId(), exception.getMessage());

        verify(petRepository, never()).save(any());
    }

    @Test
    void shouldReturnPetWhenFoundById() {
        Long petId = 1L;
        Cliente cliente = buildCliente();
        Pet pet = buildPetEntity(cliente, buildPetRequestDTO());
        pet.setId(petId);
        PetResponseDTO responseDTO = buildPetResponseDTO(pet);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(petMapper.toResponseDTO(pet)).thenReturn(responseDTO);

        PetResponseDTO result = petService.findById(petId);

        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getNome(), result.getNome());
        assertEquals(responseDTO.getEspecie(), result.getEspecie());
        assertEquals(responseDTO.getRaca(), result.getRaca());
        assertEquals(responseDTO.getDataNascimento(), result.getDataNascimento());
        assertEquals(responseDTO.getIdade(), result.getIdade());
        assertEquals(responseDTO.getPeso(), result.getPeso());
        assertEquals(responseDTO.getClienteId(), result.getClienteId());

        verify(petRepository).findById(petId);
    }

    @Test
    void shouldThrowExceptionWhenPetNotFoundById() {
        Long petId = 1L;

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.findById(petId)
        );

        assertEquals("Pet não encontrado!", exception.getMessage());

        verify(petRepository).findById(petId);
    }

    @Test
    void shouldReturnAllPets() {
        Cliente cliente = buildCliente();
        Pet pet1 = buildPetEntity(cliente, buildPetRequestDTO());
        pet1.setId(1L);
        Pet pet2 = buildPetEntity(cliente, buildPetRequestDTO());
        pet2.setId(2L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pet> petPage = new PageImpl<>(List.of(pet1, pet2));

        PetResponseDTO responseDTO1 = buildPetResponseDTO(pet1);
        PetResponseDTO responseDTO2 = buildPetResponseDTO(pet2);

        when(petRepository.findAll(pageable)).thenReturn(petPage);
        when(petMapper.toResponseDTO(pet1)).thenReturn(responseDTO1);
        when(petMapper.toResponseDTO(pet2)).thenReturn(responseDTO2);

        Page<PetResponseDTO> result = petService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(responseDTO1.getId(), result.getContent().get(0).getId());
        assertEquals(responseDTO2.getId(), result.getContent().get(1).getId());

        verify(petRepository).findAll(pageable);
    }


    @Test
    void shouldReturnEmptyPageWhenNoPetsExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pet> emptyPage = new PageImpl<>(List.of());

        when(petRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<PetResponseDTO> result = petService.findAll(pageable);

        assertTrue(result.isEmpty());

        verify(petRepository).findAll(pageable);
    }

    @Test
    void shouldUpdatePetSuccessfully() {
        Long petId = 1L;

        Cliente cliente = buildCliente();
        Pet existingPet = buildPetEntity(cliente, buildPetRequestDTO());
        existingPet.setId(petId);
        existingPet.setNome("OldName");
        existingPet.setPeso(10.0);

        PetRequestDTO petDTO = buildPetRequestDTO();

        Pet updatedPet = buildPetEntity(cliente, petDTO);
        updatedPet.setId(petId);
        PetResponseDTO responseDTO = buildPetResponseDTO(updatedPet);

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        when(clienteRepository.findById(petDTO.getClienteId())).thenReturn(Optional.of(cliente));
        doNothing().when(petMapper).updateEntity(existingPet,petDTO,cliente);
        when(petRepository.save(existingPet)).thenReturn(updatedPet);
        when(petMapper.toResponseDTO(updatedPet)).thenReturn(responseDTO);

        PetResponseDTO result = petService.update(petId, petDTO);

        assertEquals(responseDTO.getId(), result.getId());
        assertEquals("Fido", result.getNome());
        assertEquals(12.5, result.getPeso());
        assertEquals(responseDTO.getEspecie(), result.getEspecie());
        assertEquals(responseDTO.getRaca(), result.getRaca());
        assertEquals(responseDTO.getDataNascimento(), result.getDataNascimento());
        assertEquals(responseDTO.getIdade(), result.getIdade());
        assertEquals(responseDTO.getClienteId(), result.getClienteId());

        verify(petRepository).findById(petId);
        verify(clienteRepository).findById(petDTO.getClienteId());
        verify(petMapper).updateEntity(existingPet, petDTO, cliente);
        verify(petRepository).save(existingPet);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentPet() {
        Long petId = 1L;
        PetRequestDTO petDTO = buildPetRequestDTO();

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.update(petId, petDTO)
        );

        assertEquals("Pet não encontrado!", exception.getMessage());

        verify(petRepository).findById(petId);
        verify(clienteRepository, never()).findById(any());
        verify(petMapper, never()).updateEntity(any(), any(), any());
        verify(petRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPetWithNonexistentCliente() {
        Long petId = 1L;
        PetRequestDTO petDTO = buildPetRequestDTO();
        Pet existingPet = buildPetEntity(buildCliente(), buildPetRequestDTO());
        existingPet.setId(petId);

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        when(clienteRepository.findById(petDTO.getClienteId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.update(petId, petDTO)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(petRepository).findById(petId);
        verify(clienteRepository).findById(petDTO.getClienteId());
        verify(petMapper, never()).updateEntity(any(), any(), any());
        verify(petRepository, never()).save(any());
    }

    @Test
    void shouldDeletePetSuccessfully() {
        Long petId = 1L;

        when(petRepository.existsById(petId)).thenReturn(true);
        doNothing().when(petRepository).deleteById(petId);

        assertDoesNotThrow(() -> petService.delete(petId));

        verify(petRepository).existsById(petId);
        verify(petRepository).deleteById(petId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentPet() {
        Long petId = 1L;

        when(petRepository.existsById(petId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.delete(petId)
        );

        assertEquals("Pet não encontrado!", exception.getMessage());
        verify(petRepository).existsById(petId);
        verify(petRepository, never()).deleteById(any());
    }

    @Test
    void shouldReturnPetsForCliente() {
        Long clienteId = 1L;
        Cliente cliente = buildCliente();

        Pet pet1 = buildPetEntity(cliente, buildPetRequestDTO());
        pet1.setId(1L);
        Pet pet2 = buildPetEntity(cliente, buildPetRequestDTO());
        pet2.setId(2L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pet> petPage = new PageImpl<>(List.of(pet1, pet2));

        PetResponseDTO responseDTO1 = buildPetResponseDTO(pet1);
        PetResponseDTO responseDTO2 = buildPetResponseDTO(pet2);

        when(petRepository.findByClienteId(clienteId, pageable)).thenReturn(petPage);
        when(petMapper.toResponseDTO(pet1)).thenReturn(responseDTO1);
        when(petMapper.toResponseDTO(pet2)).thenReturn(responseDTO2);

        Page<PetResponseDTO> result = petService.findByCliente(clienteId, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(responseDTO1.getId(), result.getContent().get(0).getId());
        assertEquals(responseDTO2.getId(), result.getContent().get(1).getId());

        verify(petRepository).findByClienteId(clienteId, pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenClienteHasNoPets() {
        Long clienteId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pet> emptyPage = new PageImpl<>(List.of());

        when(petRepository.findByClienteId(clienteId, pageable)).thenReturn(emptyPage);

        Page<PetResponseDTO> result = petService.findByCliente(clienteId, pageable);

        assertTrue(result.isEmpty());
        verify(petRepository).findByClienteId(clienteId, pageable);
    }

    @Test
    void shouldReturnOnlyPetsMatchingSpecifiedEspecie() {
        Cliente cliente = buildCliente();

        Pet dog = buildPetEntity(cliente, buildPetRequestDTO());
        dog.setId(1L);
        dog.setEspecie("Cachorro");

        Pet cat = buildPetEntity(cliente, buildPetRequestDTO());
        cat.setId(2L);
        cat.setEspecie("Gato");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Pet> petPage = new PageImpl<>(List.of(dog));

        PetResponseDTO dogDTO = buildPetResponseDTO(dog);

        when(petRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(petPage);
        when(petMapper.toResponseDTO(dog)).thenReturn(dogDTO);

        Page<PetResponseDTO> result = petService.search(
                null, "Cachorro", null, null, null, null, pageable
        );

        assertEquals(1, result.getTotalElements());
        assertEquals("Cachorro", result.getContent().get(0).getEspecie());
        assertEquals(dogDTO.getId(), result.getContent().get(0).getId());

        verify(petRepository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    void shouldReturnEmptyPageWhenNoPetsMatchSearchCriteria() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pet> emptyPage = new PageImpl<>(List.of());

        when(petRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        Page<PetResponseDTO> result = petService.search(
                "Miau", "Gato", "Persa", 5.0, 1.0, 3.0, pageable
        );

        assertTrue(result.isEmpty());
        verify(petRepository).findAll(any(Specification.class), eq(pageable));
    }

    private PetRequestDTO buildPetRequestDTO() {
        PetRequestDTO petDTO = new PetRequestDTO();
        petDTO.setNome("Fido");
        petDTO.setEspecie("Cachorro");
        petDTO.setRaca("Labrador");
        petDTO.setDataNascimento(LocalDate.of(2021, 5, 20));
        petDTO.setIdadeAproximada(null);
        petDTO.setPeso(12.5);
        petDTO.setClienteId(1L);
        return petDTO;
    }

    private Cliente buildCliente() {
        return Cliente.builder()
                .id(1L)
                .nome("João Silva")
                .telefone("81999999999")
                .email("joao@email.com")
                .endereco("Rua das Flores, 123")
                .pets(new ArrayList<>())
                .build();
    }

    private Pet buildPetEntity(Cliente cliente, PetRequestDTO dto) {
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

    private PetResponseDTO buildPetResponseDTO(Pet pet) {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(pet.getId());
        dto.setNome(pet.getNome());
        dto.setEspecie(pet.getEspecie());
        dto.setRaca(pet.getRaca());
        dto.setDataNascimento(pet.getDataNascimento());
        dto.setIdade(pet.getIdadeAproximada());
        dto.setPeso(pet.getPeso());
        dto.setClienteId(pet.getCliente().getId());
        return dto;
    }

}
