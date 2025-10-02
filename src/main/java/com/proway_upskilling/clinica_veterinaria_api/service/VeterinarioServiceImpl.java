package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import com.proway_upskilling.clinica_veterinaria_api.specification.VeterinarioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepository repository;

    public VeterinarioServiceImpl(VeterinarioRepository repository) {
        this.repository = repository;
    }

    private VeterinarioDTO toDto(Veterinario veterinario) {
        return VeterinarioDTO.builder()
                .id(veterinario.getId())
                .especialiade(veterinario.getEspecialidade())
                .crmv(veterinario.getCrmv())
                .nome(veterinario.getNome())
                .dataContratacao(veterinario.getDataContratacao())
                .consultas(veterinario.getConsultas())
                .build();
    }

    private Veterinario toEntity(VeterinarioDTO dto) {
        return Veterinario.builder()
                .id(dto.getId())
                .especialidade(dto.getEspecialiade())
                .crmv(dto.getCrmv())
                .nome(dto.getNome())
                .dataContratacao(dto.getDataContratacao())
                .consultas(dto.getConsultas())
                .build();
    }

    @Override
    public VeterinarioDTO create(VeterinarioDTO veterinarioDTO) {

        if (repository.existsByCrmv(veterinarioDTO.getCrmv())) {
            throw new ResourceNotFoundException("CRMV já cadastrado!");
        }

        veterinarioDTO.setDataContratacao(LocalDate.now());
        Veterinario veterinario = repository.save(toEntity(veterinarioDTO));
        return toDto(veterinario);
    }

    @Override
    public ResponseEntity<Message> delete(long id) {

        Veterinario veterinario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Veterinário com ID %s não encontrado!", id)));

        repository.delete(veterinario);

        return ResponseEntity.ok(Message.builder().title("Veterinário removido com sucesso.").build());
    }

    @Override
    public VeterinarioDTO findById(long id) {

        Veterinario veterinario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Veterinário com ID %s não encontrado!", id)));

        return toDto(veterinario);
    }

    @Override
    public VeterinarioDTO save(long id, VeterinarioDTO veterinarioDTO) {

        Veterinario veterinario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Veterinário com ID %s não encontrado!", id)));

        if (veterinarioDTO.getNome() != null) veterinario.setNome(veterinarioDTO.getNome());
        if (veterinarioDTO.getEspecialiade() != null) veterinario.setEspecialidade(veterinarioDTO.getEspecialiade());
        if (veterinarioDTO.getCrmv() != null) veterinario.setCrmv(veterinarioDTO.getCrmv());
        if (veterinarioDTO.getDataContratacao() != null) veterinario.setDataContratacao(veterinarioDTO.getDataContratacao());

        return toDto(repository.save(veterinario));
    }

    @Override
    public Page<VeterinarioDTO> filterVeterinarios(String nome, String especialidade, String crmv, LocalDate dataContratacao, Pageable page) {

        Specification<Veterinario> spec = Specification.anyOf();

        spec = spec.and(VeterinarioSpecification.comNome(nome))
                .and(VeterinarioSpecification.comEspecialidade(especialidade))
                .and(VeterinarioSpecification.comCrmv(crmv))
                .and(VeterinarioSpecification.comDataContratacao(dataContratacao));

        Page<Veterinario> veterinarios = repository.findAll(spec, page);

        return veterinarios.map(this::toDto);
    }

    @Override
    public Page<VeterinarioDTO> findByDataContratacaoGreaterThanEqual(LocalDate dataContratacao, Pageable page) {
        Page<Veterinario> veterinarios = repository.findVeterinarioByDataContratacaoGreaterThanEqual(dataContratacao, page);
        return veterinarios.map(this::toDto);
    }
}
