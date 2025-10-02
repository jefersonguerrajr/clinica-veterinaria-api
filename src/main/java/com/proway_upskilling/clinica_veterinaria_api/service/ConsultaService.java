package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.Consulta;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConsultaService {
    ConsultaDTO criarConsulta(ConsultaDTO dto);
    ConsultaDTO buscarPorId(Long id);
    List<ConsultaDTO> listarTodas();
    ConsultaDTO atualizar(Long id, ConsultaDTO dto);
    void deletar(Long id);

    ConsultaDTO toDto(Consulta c);

    List<Consulta> buscarConsultasPorPetId(Long petId);
}
