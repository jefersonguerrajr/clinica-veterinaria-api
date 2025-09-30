package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ClienteDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository userRepository) {
        this.clienteRepository = userRepository;
    }

    public ResponseEntity<Page<Cliente>> searchClientByName(String name, Pageable pageable){
        return ResponseEntity.ok(clienteRepository.findByNomeLikeIgnoreCase(name, pageable));
    }

    public ResponseEntity<Cliente> saveClient(ClienteDTO clientDTO){
        Cliente client = Cliente.builder()
                .email(clientDTO.getEmail())
                .nome(clientDTO.getName())
                .telefone(clientDTO.getTelephone())
                .endereco(clientDTO.getAddress())
                .build();
        client = clienteRepository.save(client);
        return ResponseEntity.ok(client);
    }

    public ResponseEntity<Cliente> editClient(Cliente cliente){
        return ResponseEntity.ok(cliente);
    }

    public ResponseEntity<Message> removeClient(long clienteId) throws ResourceNotFoundException{
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + clienteId + " não encontrado."));
        clienteRepository.delete(cliente);

        return ResponseEntity.ok(Message.builder().title("Removido com sucesso").build());
    }

}
