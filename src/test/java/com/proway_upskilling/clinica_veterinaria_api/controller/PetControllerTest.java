package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proway_upskilling.clinica_veterinaria_api.config.SecurityConfig;
import com.proway_upskilling.clinica_veterinaria_api.exception.GlobalExceptionHandler;
import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import com.proway_upskilling.clinica_veterinaria_api.security.JwtUtil;
import com.proway_upskilling.clinica_veterinaria_api.service.ConsultaService;
import com.proway_upskilling.clinica_veterinaria_api.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetService petService;

    @MockBean
    private ConsultaService consultaService;

    @MockBean
    private JwtUtil jwtUtil;

    private PetRequestDTO petRequestDTOPadrao;
    private PetResponseDTO petResponseDTOPadrao;

    @BeforeEach
    void setUp() {
        petRequestDTOPadrao = criarPetRequestPadrao();
        petResponseDTOPadrao = criarPetResponsePadrao();
    }

    @Test
    void shouldCreatePetSuccessfully() throws Exception {
        when(petService.create(any(PetRequestDTO.class))).thenReturn(petResponseDTOPadrao);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequestDTOPadrao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Fido"))
                .andExpect(jsonPath("$.especie").value("Cachorro"))
                .andExpect(jsonPath("$.raca").value("Labrador"))
                .andExpect(jsonPath("$.peso").value(12.5))
                .andExpect(jsonPath("$.dataNascimento").value("2022-01-01"))
                .andExpect(jsonPath("$.idade").value(3.10))
                .andExpect(jsonPath("$.clienteId").value(1L));

        verify(petService).create(any(PetRequestDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
        petRequestDTOPadrao.setNome(null);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequestDTOPadrao)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").value("Nome é obrigatório"));
    }

    @Test
    void shouldReturnBadRequestWhenBothBirthDateAndApproximateAgeAreProvided() throws Exception {
        petRequestDTOPadrao.setDataNascimento(LocalDate.of(2022, 1, 1));
        petRequestDTOPadrao.setIdadeAproximada(1.5);

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequestDTOPadrao)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dataNascimento")
                        .value("Informe apenas data de nascimento ou idade aproximada, nunca ambos."));
    }

    @Test
    void shouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        petRequestDTOPadrao.setClienteId(99L);
        when(petService.create(any(PetRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Cliente não encontrado com id " + petRequestDTOPadrao.getClienteId()));

        mockMvc.perform(post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequestDTOPadrao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente não encontrado com id " + petRequestDTOPadrao.getClienteId()));
    }

    @Test
    void shouldReturnPetByIdSuccessfully() throws Exception {
        when(petService.findById(1L)).thenReturn(petResponseDTOPadrao);

        mockMvc.perform(get("/pet/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Fido"))
                .andExpect(jsonPath("$.especie").value("Cachorro"))
                .andExpect(jsonPath("$.raca").value("Labrador"))
                .andExpect(jsonPath("$.dataNascimento").value("2022-01-01"))
                .andExpect(jsonPath("$.idade").value(3.10))
                .andExpect(jsonPath("$.peso").value(12.5))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    void shouldReturnNotFoundWhenPetDoesNotExist() throws Exception {
        when(petService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Pet não encontrado!"));

        mockMvc.perform(get("/pet/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pet não encontrado!"));
    }

    @Test
    void shouldReturnPagedListOfPets() throws Exception {
        PetResponseDTO pet1 = criarPetResponsePadrao();

        PetResponseDTO pet2 = criarPetResponsePadrao();
        pet2.setId(2L);
        pet2.setNome("Mia");
        pet2.setEspecie("Gato");
        pet2.setRaca("Siamês");
        pet2.setDataNascimento(LocalDate.of(2021, 6, 15));
        pet2.setIdade(2.3);
        pet2.setPeso(4.2);
        pet2.setClienteId(2L);

        when(petService.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(pet1, pet2)));

        mockMvc.perform(get("/pet")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Fido"))
                .andExpect(jsonPath("$.content[0].especie").value("Cachorro"))
                .andExpect(jsonPath("$.content[0].raca").value("Labrador"))
                .andExpect(jsonPath("$.content[0].dataNascimento").value("2022-01-01"))
                .andExpect(jsonPath("$.content[0].idade").value(3.10))
                .andExpect(jsonPath("$.content[0].peso").value(12.5))
                .andExpect(jsonPath("$.content[0].clienteId").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].nome").value("Mia"))
                .andExpect(jsonPath("$.content[1].especie").value("Gato"))
                .andExpect(jsonPath("$.content[1].raca").value("Siamês"))
                .andExpect(jsonPath("$.content[1].dataNascimento").value("2021-06-15"))
                .andExpect(jsonPath("$.content[1].idade").value(2.3))
                .andExpect(jsonPath("$.content[1].peso").value(4.2))
                .andExpect(jsonPath("$.content[1].clienteId").value(2L));
    }

    @Test
    void shouldUpdatePetSuccessfully() throws Exception {
        PetRequestDTO petRequestAtualizado = criarPetRequestPadrao();
        petRequestAtualizado.setNome("Rex");
        petRequestAtualizado.setPeso(15.0);

        PetResponseDTO petResponseAtualizado = criarPetResponsePadrao();
        petResponseAtualizado.setNome("Rex");
        petResponseAtualizado.setPeso(15.0);

        when(petService.update(eq(1L), any(PetRequestDTO.class)))
                .thenReturn(petResponseAtualizado);

        mockMvc.perform(put("/pet/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequestAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Rex"))
                .andExpect(jsonPath("$.especie").value("Cachorro"))
                .andExpect(jsonPath("$.raca").value("Labrador"))
                .andExpect(jsonPath("$.dataNascimento").value("2022-01-01"))
                .andExpect(jsonPath("$.idade").value(3.10))
                .andExpect(jsonPath("$.peso").value(15.0))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPet() throws Exception {
        PetRequestDTO petRequestAtualizado = criarPetRequestPadrao();

        when(petService.update(eq(99L), any(PetRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Pet não encontrado!"));

        mockMvc.perform(put("/pet/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequestAtualizado)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pet não encontrado!"));
    }

    @Test
    void shouldDeletePetSuccessfully() throws Exception {
        doNothing().when(petService).delete(1L);

        mockMvc.perform(delete("/pet/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentPet() throws Exception {
        doThrow(new ResourceNotFoundException("Pet não encontrado!"))
                .when(petService).delete(99L);

        mockMvc.perform(delete("/pet/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pet não encontrado!"));
    }

    @Test
    void shouldReturnPagedPetsByCliente() throws Exception {
        PetResponseDTO pet1 = criarPetResponsePadrao();
        PetResponseDTO pet2 = criarPetResponsePadrao();
        pet2.setId(2L);
        pet2.setNome("Mel");
        pet2.setClienteId(1L);

        when(petService.findByCliente(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(pet1, pet2)));

        mockMvc.perform(get("/pet/cliente/{clienteId}", 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Fido"))
                .andExpect(jsonPath("$.content[0].clienteId").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].nome").value("Mel"))
                .andExpect(jsonPath("$.content[1].clienteId").value(1L));
    }

    @Test
    void shouldReturnEmptyWhenClienteHasNoPets() throws Exception {
        when(petService.findByCliente(eq(2L), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/pet/cliente/{clienteId}", 2L)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void shouldReturnPetsFilteredBySpecies() throws Exception {
        PetResponseDTO petGato = new PetResponseDTO();
        petGato.setId(2L);
        petGato.setNome("Mia");
        petGato.setEspecie("Gato");
        petGato.setRaca("Siamês");
        petGato.setPeso(4.5);
        petGato.setClienteId(2L);

        when(petService.search(
                anyString(),
                eq("Gato"),
                anyString(),
                any(),
                any(),
                any(),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(petGato)));

        mockMvc.perform(get("/pet/search")
                        .param("nome", "")
                        .param("especie", "Gato")
                        .param("raca", "")
                        .param("peso", "")
                        .param("idadeMin", "")
                        .param("idadeMax", "")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome,asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(2L))
                .andExpect(jsonPath("$.content[0].nome").value("Mia"))
                .andExpect(jsonPath("$.content[0].especie").value("Gato"))
                .andExpect(jsonPath("$.content[0].raca").value("Siamês"))
                .andExpect(jsonPath("$.content[0].peso").value(4.5))
                .andExpect(jsonPath("$.content[0].clienteId").value(2L));

        verify(petService, times(1)).search(
                anyString(), eq("Gato"), anyString(), any(), any(), any(), any(Pageable.class)
        );
    }

    @Test
    void shouldReturnConsultasByPet() throws Exception {
        Long petId = 1L;

        ConsultaDTO consultaDTO = ConsultaDTO.builder()
                .id(1L)
                .petId(petId)
                .veterinarioId(10L)
                .motivo("Consulta de rotina")
                .diagnostico("Saudável")
                .tratamento("Nenhum")
                .status("Agendada")
                .nomePet("Fido")
                .nomeVeterinario("Dr. João")
                .build();

        when(consultaService.buscarConsultasPorPetId(eq(petId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(consultaDTO)));

        mockMvc.perform(get("/pet/{petId}/consultas", petId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].petId").value(petId))
                .andExpect(jsonPath("$.content[0].veterinarioId").value(10L))
                .andExpect(jsonPath("$.content[0].motivo").value("Consulta de rotina"))
                .andExpect(jsonPath("$.content[0].diagnostico").value("Saudável"))
                .andExpect(jsonPath("$.content[0].tratamento").value("Nenhum"))
                .andExpect(jsonPath("$.content[0].status").value("Agendada"))
                .andExpect(jsonPath("$.content[0].nomePet").value("Fido"))
                .andExpect(jsonPath("$.content[0].nomeVeterinario").value("Dr. João"));

        verify(consultaService, times(1)).buscarConsultasPorPetId(eq(petId), any(Pageable.class));
    }

    private PetRequestDTO criarPetRequestPadrao() {
        PetRequestDTO dto = new PetRequestDTO();
        dto.setNome("Fido");
        dto.setEspecie("Cachorro");
        dto.setRaca("Labrador");
        dto.setPeso(12.5);
        dto.setDataNascimento(LocalDate.of(2022, 1, 1));
        dto.setClienteId(1L);
        return dto;
    }

    private PetResponseDTO criarPetResponsePadrao() {
        PetResponseDTO dto = new PetResponseDTO();
        dto.setId(1L);
        dto.setNome("Fido");
        dto.setEspecie("Cachorro");
        dto.setRaca("Labrador");
        dto.setPeso(12.5);
        dto.setIdade(3.10);
        dto.setDataNascimento(LocalDate.of(2022, 1, 1));
        dto.setClienteId(1L);
        return dto;
    }

    private PetRequestDTO criarPetRequestComNome(String nome) {
        PetRequestDTO dto = criarPetRequestPadrao();
        dto.setNome(nome);
        return dto;
    }

}



