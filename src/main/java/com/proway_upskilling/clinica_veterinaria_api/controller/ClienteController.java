package com.proway_upskilling.clinica_veterinaria_api.controller;

import com.proway_upskilling.clinica_veterinaria_api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ClienteController {

    private final ClienteService userService;

    public ClienteController(ClienteService clienteService) {
        this.userService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os clientes cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar usuários")
    })
    public ResponseEntity<?> findAllUsers() {
        return userService.findAllClients();
    }

}
