package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/veterinario")
public class VeterinarioController {

    private final VeterinarioService service;

    public VeterinarioController(VeterinarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Pesquisar veterinário", description = "Retorna uma lista dos veterinários cadastrados conforme filtrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veterinários retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar veterinários")
    })
    public Page<VeterinarioDTO> getAll(
            @Parameter(description = "Nome do veterinário") @RequestParam(required = false) String nome,
            @Parameter(description = "Especialidade do veterinário") @RequestParam(required = false) String especialidade,
            @Parameter(description = "CRMV do veterinário") @RequestParam(required = false) String crmv,
            @Parameter(description = "Data de contratação do veterinário") @RequestParam(required = false) LocalDate dataContratacao,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return service.filterVeterinarios(nome, especialidade, crmv, dataContratacao, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pesquisar veterinário por id", description = "Retorna um veterinário cadastrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinário encontrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar veterinário")
    })
    public ResponseEntity<VeterinarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/contratacaoAPartirDe")
    @Operation(summary = "Pesquisar veterinários a partir da data de contratação", description = "Retorna veterinários contratados a partir da data informada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinários encontrados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar veterinários")
    })
    public Page<VeterinarioDTO> findByDataContratacao(
            @RequestParam("dataContratacao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataContratacao,
            @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return service.findByDataContratacaoGreaterThanEqual(dataContratacao, pageable);
    }

    @PostMapping
    @Operation(summary = "Salvar um novo veterinário", description = "Salva e retorna o veterinário com um id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Novo veterinário cadastrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar veterinário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )
            )
    })
    public ResponseEntity<VeterinarioDTO> create(@Valid @RequestBody VeterinarioDTO dto) {
        dto = service.create(dto);
        return ResponseEntity.created(URI.create("veterinario" + dto.getId())).body(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um veterinário", description = "Atualiza um veterinário já cadastrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinário atualizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar veterinário",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )
            )
    })
    public ResponseEntity<VeterinarioDTO> update(@PathVariable Long id, @Valid @RequestBody VeterinarioDTO dto) {
        return ResponseEntity.ok(service.save(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover veterinário", description = "Remove um veterinário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veterinário removido com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Veterinário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )
            )
    })
    public ResponseEntity<Message> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
