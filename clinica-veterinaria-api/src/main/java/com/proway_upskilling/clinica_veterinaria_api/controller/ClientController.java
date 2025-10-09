package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ClienteDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClienteService clientService;

    public ClientController(ClienteService clienteService) {
        this.clientService = clienteService;
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Pesquisar cliente por id", description = "Retorna um cliente cadastrado de acordo com o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente cadastrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Cliente> searchClientById(@RequestParam long id) {
        return clientService.findClienteById(id);
    }

    @GetMapping
    @Operation(summary = "Pesquisar cliente", description = "Retorna uma lista de todos os clientes cadastrados de acordo com a pesquisa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar usuários")
    })
    public ResponseEntity<Page<Cliente>> searchClients(@RequestParam(required = false) String nome,
                                                       @PageableDefault(size = 5, sort = "nome") Pageable pageable) {
        return clientService.searchClientByName(nome, pageable);
    }

    @PostMapping
    @Operation(summary = "Salvar um novo cliente", description = "Salva e retorna o cliente com um id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente salvo com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar cliente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )
            )
    })
    public ResponseEntity<Cliente> saveUser(@RequestBody @Valid ClienteDTO client) {
        return clientService.saveClient(client);
    }

    @PutMapping
    @Operation(summary = "Atualizar cliente", description = "Atualiza um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar cliente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )
            )
    })
    public ResponseEntity<Cliente> editUser(@RequestBody Cliente client) {
        return clientService.editClient(client);
    }

    @DeleteMapping("/{clientId}")
    @Operation(summary = "Remover cliente", description = "Remove um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente removido com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class)
                    )
            )
    })
    public ResponseEntity<Message> removeUser(@PathVariable long clientId) {
        return clientService.removeClient(clientId);
    }

}
