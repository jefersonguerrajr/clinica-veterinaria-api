package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ClienteService {

    ResponseEntity<Cliente> findClienteById(long id);

    ResponseEntity<Page<Cliente>> searchClientByName(String name, Pageable pageable);

    ResponseEntity<Cliente> saveClient(ClienteDTO clientDTO);

    ResponseEntity<Cliente> editClient(Cliente cliente);

    ResponseEntity<Message> removeClient(long clienteId) throws ResourceNotFoundException;
}
