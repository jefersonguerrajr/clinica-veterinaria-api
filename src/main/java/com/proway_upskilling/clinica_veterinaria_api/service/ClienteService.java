package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository userRepository) {
        this.clienteRepository = userRepository;
    }

    public ResponseEntity<List<Cliente>> findAllClients(){
        return ResponseEntity.ok(clienteRepository.findAll());
    }

}
