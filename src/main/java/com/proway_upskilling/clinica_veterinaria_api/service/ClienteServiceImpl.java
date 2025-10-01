package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Cliente;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ClienteDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository userRepository) {
        this.clienteRepository = userRepository;
    }

    @Override
    public ResponseEntity<Cliente> findClienteById(long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado."));

        return ResponseEntity.ok(cliente);
    }

    @Override
    public ResponseEntity<Page<Cliente>> searchClientByName(String name, Pageable pageable){

        Specification<Cliente> clienteSpecification = (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nome")),
                    "%" + name.toLowerCase() + "%"
            );
        };

        Page<Cliente> result = clienteRepository.findAll(clienteSpecification, pageable);

        return ResponseEntity.ok(result);
    }

    @Override
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

    @Override
    public ResponseEntity<Cliente> editClient(Cliente cliente){
        return ResponseEntity.ok(cliente);
    }

    @Override
    public ResponseEntity<Message> removeClient(long clienteId) throws ResourceNotFoundException{
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + clienteId + " não encontrado."));
        clienteRepository.delete(cliente);

        return ResponseEntity.ok(Message.builder().title("Removido com sucesso").build());
    }

}
