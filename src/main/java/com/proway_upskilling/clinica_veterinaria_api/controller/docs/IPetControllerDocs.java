package com.proway_upskilling.clinica_veterinaria_api.controller.docs;

import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetRequestDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.PetResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pets", description = "Endpoints para gerenciamento de pets")
public interface IPetControllerDocs {

    @PostMapping
    @Operation(summary = "Criar um novo pet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    ResponseEntity<PetResponseDTO> create(@Valid @RequestBody PetRequestDTO dto);

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pet por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet encontrado"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    ResponseEntity<PetResponseDTO> getById(@PathVariable Long id);

    @GetMapping
    @Operation(summary = "Listar todos os pets com paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pets retornada com sucesso")
    })
    ResponseEntity<Page<PetResponseDTO>> getAll(
            @Parameter(description = "Número da página (começa em 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação no formato: campo,asc|desc", example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort
    );

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pet pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<PetResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PetRequestDTO dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover pet pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    ResponseEntity<Void> delete(@PathVariable Long id);

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pets de um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pets retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<Page<PetResponseDTO>> getAllByCliente(
            @PathVariable Long clienteId,
            @Parameter(description = "Número da página (começa em 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação no formato: campo,asc|desc", example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort);

    @GetMapping("/search")
    @Operation(summary = "Buscar pets por filtros (nome, espécie, raça, peso, idade)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    ResponseEntity<Page<PetResponseDTO>> searchPets(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) String raca,
            @RequestParam(required = false) Double peso,
            @RequestParam(required = false) Double idadeMin,
            @RequestParam(required = false) Double idadeMax,
            @Parameter(description = "Número da página (começa em 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação no formato: campo,asc|desc", example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort
    );

    @GetMapping(value = "/{petId}/consultas")
    @Operation(summary = "Listar consultas de um pet específico com paginação",
            description = "Retorna uma página com as consultas associadas ao pet informado pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultas retornadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pet não encontrado")
    })
    ResponseEntity<Page<ConsultaDTO>> getConsultasByPet(
            @PathVariable Long petId,
            @Parameter(description = "Número da página (começa em 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Ordenação no formato: campo,asc|desc", example = "id,asc") @RequestParam(defaultValue = "id,asc") String sort
    );

}
