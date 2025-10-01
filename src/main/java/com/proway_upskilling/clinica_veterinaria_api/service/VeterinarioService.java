package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import com.proway_upskilling.clinica_veterinaria_api.model.dto.VeterinarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface VeterinarioService {

    VeterinarioDTO create(VeterinarioDTO veterinarioDTO);

    ResponseEntity<Message> delete(long id);

    VeterinarioDTO findById(long id);

    VeterinarioDTO save(VeterinarioDTO veterinarioDTO);

    Page<VeterinarioDTO> filterVeterinarios(String nome, String especialidade, String crmv, LocalDate dataContratacao, Pageable page);

    Page<VeterinarioDTO> findByDataContratacaoGreaterThanEqual(LocalDate dataContratacao, Pageable page);
}
