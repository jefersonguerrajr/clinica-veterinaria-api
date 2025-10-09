package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.GlobalExceptionHandler;
import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(GlobalExceptionHandler.class)
public class VeterinarioServiceImplTest {

    @Mock
    private VeterinarioRepository repository;

    @InjectMocks
    private VeterinarioServiceImpl service;

    private Veterinario veterinario;
    private VeterinarioDTO dto;

    @BeforeEach
    void setUp() {
        veterinario = createDefaultVeterinario();
        dto = createDefaultVeterinarioDTO();
    }

    @Test
    void shouldCreateVeterinarioSuccessfully() {
        when(repository.existsByCrmv(dto.getCrmv())).thenReturn(false);
        when(repository.save(ArgumentMatchers.<Veterinario>any())).thenReturn(veterinario);

        VeterinarioDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(dto.getCrmv(), result.getCrmv());
        verify(repository).save(ArgumentMatchers.<Veterinario>any());
    }

    @Test
    void shouldThrowExceptionWhenCrmvAlreadyExists() {
        when(repository.existsByCrmv(dto.getCrmv())).thenReturn(true);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.create(dto)
        );

        assertEquals("CRMV já cadastrado!", exception.getMessage());

        verify(repository, never()).save(ArgumentMatchers.<Veterinario>any());
    }

    @Test
    void shouldReturnMessageWhenVeterinarioExistsDelete() {

        long id = 1L;
        Veterinario veterinario = createDefaultVeterinario();

        when(repository.findById(id)).thenReturn(Optional.of(veterinario));
        doNothing().when(repository).delete(veterinario);

        ResponseEntity<Message> response = service.delete(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Veterinário removido com sucesso.", response.getBody().getTitle());

        verify(repository).findById(id);
        verify(repository).delete(veterinario);
    }

    @Test
    void shouldThrowExceptionWhenVeterinarioDoesNotExistDelete() {
        long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(id)
        );

        assertEquals("Veterinário com ID 99 não encontrado!", exception.getMessage());

        verify(repository).findById(id);
        verify(repository, never()).delete((Veterinario) any());
    }

    @Test
    void shouldReturnVeterinarioWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(veterinario));

        VeterinarioDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals(veterinario.getId(), result.getId());
        assertEquals(veterinario.getNome(), result.getNome());
        assertEquals(veterinario.getCrmv(), result.getCrmv());

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenVeterinarioNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(1L)
        );

        assertEquals("Veterinário com ID 1 não encontrado!", exception.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnUpdatedVeterinarioWhenIdExists() {
        Long id = 1L;
        Veterinario existingVeterinario = createDefaultVeterinario();
        VeterinarioDTO updateDTO = createDefaultVeterinarioDTO();
        updateDTO.setNome("Dr. João Atualizado");

        Veterinario savedVeterinario = createDefaultVeterinario();
        savedVeterinario.setNome("Dr. João Atualizado");

        when(repository.findById(id)).thenReturn(Optional.of(existingVeterinario));
        when(repository.save(existingVeterinario)).thenReturn(savedVeterinario);

        VeterinarioDTO result = service.save(id, updateDTO);

        assertNotNull(result);
        assertEquals("Dr. João Atualizado", result.getNome());

        verify(repository).findById(id);
        verify(repository).save(existingVeterinario);
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        Long id = 99L;
        VeterinarioDTO updateDTO = createDefaultVeterinarioDTO();

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.save(id, updateDTO)
        );

        assertEquals("Veterinário com ID 99 não encontrado!", exception.getMessage());

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void ShouldReturnPageOfVeterinarioMatchSpecifiedCrmv() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Veterinario> veterinarioList = List.of(veterinario);
        Page<Veterinario> veterinarioPage = new PageImpl<>(veterinarioList, pageable, veterinarioList.size());

        when(repository.findAll(ArgumentMatchers.<Specification<Veterinario>>any(), eq(pageable))).thenReturn(veterinarioPage);

        Page<VeterinarioDTO> result = service.filterVeterinarios(
                null, null, "CRMV123", null, pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Dr. João", result.getContent().get(0).getNome());
        assertEquals("CRMV123", result.getContent().get(0).getCrmv());

        verify(repository).findAll(ArgumentMatchers.<Specification<Veterinario>>any(), eq(pageable));
    }

    @Test
    void shouldReturnPageWithVeterinariosWhenDataContratacaoGreaterThanEqual() {
        Pageable pageable = PageRequest.of(0, 10);

        Veterinario vet1 = Veterinario.builder()
                .id(1L).nome("Dr. João").crmv("CRMV1")
                .dataContratacao(LocalDate.of(2024, 1, 1)).build();

        Veterinario vet2 = Veterinario.builder()
                .id(2L).nome("Dr. Carlos").crmv("CRMV2")
                .dataContratacao(LocalDate.of(2025, 1, 1)).build();

        Veterinario vet3 = Veterinario.builder()
                .id(3L).nome("Dra. Ana").crmv("CRMV3")
                .dataContratacao(LocalDate.of(2023, 1, 1)).build();

        List<Veterinario> filteredList = List.of(vet1, vet2);
        Page<Veterinario> veterinarioPage = new PageImpl<>(filteredList, pageable, filteredList.size());

        when(repository.findVeterinarioByDataContratacaoGreaterThanEqual(ArgumentMatchers.<LocalDate>any(), eq(pageable)))
                .thenReturn(veterinarioPage);

        Page<VeterinarioDTO> result = service.findByDataContratacaoGreaterThanEqual(LocalDate.of(2024, 1, 1), pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().anyMatch(v -> v.getNome().equals("Dr. João")));
        assertTrue(result.getContent().stream().anyMatch(v -> v.getNome().equals("Dr. Carlos")));
        assertFalse(result.getContent().stream().anyMatch(v -> v.getNome().equals("Dra. Ana")));

        verify(repository).findVeterinarioByDataContratacaoGreaterThanEqual(ArgumentMatchers.<LocalDate>any(), eq(pageable));
    }

    @Test
    void shouldReturnEmptyPageWhenNoVeterinariosFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Veterinario> emptyPage = Page.empty(pageable);

        when(repository.findVeterinarioByDataContratacaoGreaterThanEqual(ArgumentMatchers.<LocalDate>any(), eq(pageable)))
                .thenReturn(emptyPage);

        Page<VeterinarioDTO> result = service.findByDataContratacaoGreaterThanEqual(LocalDate.of(2030, 1, 1), pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findVeterinarioByDataContratacaoGreaterThanEqual(ArgumentMatchers.<LocalDate>any(), eq(pageable));
    }

    private Veterinario createDefaultVeterinario() {
        return Veterinario.builder()
                .id(1L)
                .nome("Dr. João")
                .especialidade("Clínico")
                .crmv("CRMV123")
                .dataContratacao(LocalDate.of(2024, 1,1))
                .build();
    }

    private VeterinarioDTO createDefaultVeterinarioDTO() {
        return VeterinarioDTO.builder()
                .id(1L)
                .nome("Dr. João")
                .especialiade("Cirurgião")
                .crmv("CRMV123")
                .dataContratacao(LocalDate.of(2024, 1, 1))
                .build();
    }

}



