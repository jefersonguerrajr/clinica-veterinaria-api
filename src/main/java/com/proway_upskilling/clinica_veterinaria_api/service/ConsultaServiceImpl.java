package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.exception.ResourceNotFoundException;
import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import com.proway_upskilling.clinica_veterinaria_api.model.Pet;
import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.ConsultaRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.PetRepository;
import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;

    @Override
    public ConsultaDTO criarConsulta(ConsultaDTO dto) {
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado: " + dto.getPetId()));
        Veterinario vet = veterinarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado: " + dto.getVeterinarioId()));

        Consulta consulta = new Consulta();
        consulta.setPet(pet);
        consulta.setVeterinario(vet);
        consulta.setMotivo(dto.getMotivo());
        consulta.setDiagnostico(dto.getDiagnostico());
        consulta.setTratamento(dto.getTratamento());
        consulta.setStatus(dto.getStatus());

        consulta = consultaRepository.save(consulta);
        return toDto(consulta);
    }

    @Override
    public ConsultaDTO buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada: " + id));
    }

    @Override
    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConsultaDTO atualizar(Long id, ConsultaDTO dto) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada: " + id));

        if (dto.getPetId() != null && !dto.getPetId().equals(consulta.getPet().getId())) {
            Pet pet = petRepository.findById(dto.getPetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado: " + dto.getPetId()));
            consulta.setPet(pet);
        }

        if (dto.getVeterinarioId() != null && !dto.getVeterinarioId().equals(consulta.getVeterinario().getId())) {
            Veterinario vet = veterinarioRepository.findById(dto.getVeterinarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado: " + dto.getVeterinarioId()));
            consulta.setVeterinario(vet);
        }

        consulta.setMotivo(dto.getMotivo());
        consulta.setDiagnostico(dto.getDiagnostico());
        consulta.setTratamento(dto.getTratamento());
        consulta.setStatus(dto.getStatus());

        consulta = consultaRepository.save(consulta);
        return toDto(consulta);
    }

    @Override
    public void deletar(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consulta não encontrada: " + id);
        }
        consultaRepository.deleteById(id);
    }
    @Override
    public ConsultaDTO toDto(Consulta c) {
        return ConsultaDTO.builder()
                .id(c.getId())
                .petId(c.getPet() != null ? c.getPet().getId() : null)
                .veterinarioId(c.getVeterinario() != null ? c.getVeterinario().getId() : null)
                .motivo(c.getMotivo())
                .diagnostico(c.getDiagnostico())
                .tratamento(c.getTratamento())
                .status(c.getStatus())
                .nomePet(c.getPet() != null ? c.getPet().getNome() : null)
                .nomeVeterinario(c.getVeterinario() != null ? c.getVeterinario().getNome() : null)
                .build();
    }

    @Override
    public List<Consulta> buscarConsultasPorPetId(Long petId) {
        return consultaRepository.findByPetId(petId);
    }
}
