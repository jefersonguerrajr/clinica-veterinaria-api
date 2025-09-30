package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepository repository;

    public VeterinarioServiceImpl(VeterinarioRepository repository) {
        this.repository = repository;
    }

    private VeterinarioDTO toDto(Veterinario veterinario) {
        if (veterinario == null) {
            return null;
        }
        return VeterinarioDTO.builder()
                .id(veterinario.getId())
                .especialiade(veterinario.getEspecialidade())
                .crmv(veterinario.getCrmv())
                .nome(veterinario.getNome())
                .build();
    }

    private Veterinario toEntity(VeterinarioDTO dto) {
        return Veterinario.builder()
                .id(dto.getId())
                .especialidade(dto.getEspecialiade())
                .crmv(dto.getCrmv())
                .nome(dto.getNome())
                .build();
    }

    @Override
    public VeterinarioDTO create(VeterinarioDTO veterinarioDTO) {
        Veterinario veterinario = toEntity(veterinarioDTO);
        veterinario = repository.save(veterinario);
        return toDto(veterinario);
    }

    @Override
    public List<VeterinarioDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public VeterinarioDTO findById(long id) {
        return toDto(repository.findById(id).orElse(null));
    }

    @Override
    public VeterinarioDTO save(VeterinarioDTO veterinarioDTO) {
        return toDto(repository.save(toEntity(veterinarioDTO)));
    }
}
