package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proway_upskilling.clinica_veterinaria_api.config.SecurityConfig;
import com.proway_upskilling.clinica_veterinaria_api.exception.GlobalExceptionHandler;
import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.security.JwtUtil;
import com.proway_upskilling.clinica_veterinaria_api.service.VeterinarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeterinarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
public class VeterinarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeterinarioService service;

    private VeterinarioDTO dto;
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void ShouldReturnPageOfVeterinariosMatchSpecifiedEspecialidade() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);

        VeterinarioDTO vet1 = createVeterinarioDTO(1L, "Dr. João", "CRMV1", "Cirurgião", LocalDate.of(2024, 1, 1));
        VeterinarioDTO vet2 = createVeterinarioDTO(2L, "Dra. Ana", "CRMV2", "Dermatologista", LocalDate.of(2025, 2, 1));
        VeterinarioDTO vet3 = createVeterinarioDTO(3L, "Dr. Carlos", "CRMV3", "Cirurgião", LocalDate.of(2023, 3, 1));

        List<VeterinarioDTO> filteredList = List.of(vet1, vet2);
        Page<VeterinarioDTO> page = new PageImpl<>(filteredList, pageable, filteredList.size());

        when(service.filterVeterinarios(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/veterinario")
                        .param("especialidade", "Cirurgião")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nome").value("Dr. João"))
                .andExpect(jsonPath("$.content[1].nome").value("Dra. Ana"));

    }

    @Test
    void shouldReturnVeterinarioWhenFindByID() throws Exception {
        VeterinarioDTO dto = createVeterinarioDTO(1L, "Dr. João", "CRMV123", "Cirurgião", LocalDate.of(2024, 1, 1));

        when(service.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/veterinario/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Dr. João"))
                .andExpect(jsonPath("$.crmv").value("CRMV123"))
                .andExpect(jsonPath("$.especialiade").value("Cirurgião"));

        verify(service).findById(1L);
    }

    @Test
    void shouldReturnNotFoundWhenVeterinarioDoesNotExist() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Veterinário com ID 99 não encontrado!"));


        mockMvc.perform(get("/veterinario/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service).findById(99L);
    }

    @Test
    void shouldReturnFilteredVeterinariosWhenfindByDataContratacao() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);

        VeterinarioDTO vet1 = createVeterinarioDTO(1L, "Dr. João", "CRMV1", "Cirurgião", LocalDate.of(2024, 1, 1));
        VeterinarioDTO vet2 = createVeterinarioDTO(2L, "Dra. Ana", "CRMV2", "Dermatologista", LocalDate.of(2025, 2, 1));
        VeterinarioDTO vet3 = createVeterinarioDTO(3L, "Dr. Carlos", "CRMV3", "Cirurgião", LocalDate.of(2023, 3, 1));

        List<VeterinarioDTO> filteredList = List.of(vet1, vet2);
        Page<VeterinarioDTO> page = new PageImpl<>(filteredList, pageable, filteredList.size());

        when(service.findByDataContratacaoGreaterThanEqual(eq(LocalDate.of(2024, 1, 1)), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/veterinario/contratacaoAPartirDe")
                        .param("dataContratacao", "2024-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nome").value("Dr. João"))
                .andExpect(jsonPath("$.content[1].nome").value("Dra. Ana"));

        verify(service).findByDataContratacaoGreaterThanEqual(eq(LocalDate.of(2024, 1, 1)), any(Pageable.class));
    }

    @Test
    void shouldReturnEmptyPageWhenNoVeterinariosFound() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        Page<VeterinarioDTO> emptyPage = Page.empty(pageable);

        when(service.findByDataContratacaoGreaterThanEqual(eq(LocalDate.of(2030, 1, 1)), any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/veterinario/contratacaoAPartirDe")
                        .param("dataContratacao", "2030-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));

        verify(service).findByDataContratacaoGreaterThanEqual(eq(LocalDate.of(2030, 1, 1)), any(Pageable.class));
    }

    @Test
    void shouldCreateVeterinarioSuccessfully() throws Exception {
        VeterinarioDTO dto = createVeterinarioDTO(1L, "Dr. João", "CRMV123", "Cirurgião", LocalDate.of(2024, 1, 1));

        when(service.create(any(VeterinarioDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/veterinario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "veterinario1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Dr. João"))
                .andExpect(jsonPath("$.crmv").value("CRMV123"))
                .andExpect(jsonPath("$.especialiade").value("Cirurgião"));

        verify(service).create(any(VeterinarioDTO.class));

    }

    @Test
    void shouldReturnNotFoundWhenCrmvAlreadyExists() throws Exception {
        VeterinarioDTO dto = new VeterinarioDTO();
        dto.setNome("Dr. João");
        dto.setCrmv("CRMV123");
        dto.setEspecialiade( "Cirurgião");
        dto.setDataContratacao(LocalDate.of(2024, 1, 1));

        when(service.create(any(VeterinarioDTO.class)))
                .thenThrow(new ResourceNotFoundException("CRMV já cadastrado!"));

        mockMvc.perform(post("/veterinario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("CRMV já cadastrado!"));

        verify(service).create(any(VeterinarioDTO.class));
    }

    @Test
    void shouldReturnUpdatedVeterinarioWhenIdExists() throws Exception {
        Long id = 1L;
        VeterinarioDTO updatedVeterinario = createVeterinarioDTO(id, "Carlos Silva", "CRMV1234", "Cirurgiao", LocalDate.of(2023, 1, 10));

        when(service.save(eq(id), any(VeterinarioDTO.class))).thenReturn(updatedVeterinario);

        mockMvc.perform(put("/veterinario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVeterinario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Carlos Silva"))
                .andExpect(jsonPath("$.especialiade").value("Cirurgiao"))
                .andExpect(jsonPath("$.crmv").value("CRMV1234"));
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExistUpdate() throws Exception {
        Long id = 99L;
        VeterinarioDTO dto = new VeterinarioDTO();
        dto.setNome("Dr. João");
        dto.setCrmv("CRMV123");
        dto.setEspecialiade( "Cirurgião");
        dto.setDataContratacao(LocalDate.of(2024, 1, 1));

        when(service.save(eq(id), any(VeterinarioDTO.class)))
                .thenThrow(new ResourceNotFoundException("Veterinário com ID 99 não encontrado!"));

        mockMvc.perform(put("/veterinario/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veterinário com ID 99 não encontrado!"));
    }


    @Test
    void shouldReturnOkWhenVeterinarioExist() throws Exception {
        Message message = Message.builder()
                .title("Veterinário removido com sucesso.")
                .build();

        when(service.delete(1L)).thenReturn(ResponseEntity.ok(message));

        mockMvc.perform(delete("/veterinario/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Veterinário removido com sucesso."));

        verify(service).delete(1L);
    }

    @Test
    void shouldReturnNotFoundWhenVeterinarioDoesNotExistDelete() throws Exception {
        when(service.delete(99L)).thenThrow(new ResourceNotFoundException("Veterinário com ID 99 não encontrado!"));

        mockMvc.perform(delete("/veterinario/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veterinário com ID 99 não encontrado!"));

        verify(service).delete(99L);
    }

    private VeterinarioDTO createVeterinarioDTO(long id, String nome, String crmv, String especialidade, LocalDate dataContratacao) {
        return VeterinarioDTO.builder()
                .id(id)
                .nome(nome)
                .crmv(crmv)
                .especialiade(especialidade)
                .dataContratacao(dataContratacao)
                .build();
    }

}
