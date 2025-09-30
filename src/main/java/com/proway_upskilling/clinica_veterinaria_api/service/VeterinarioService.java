package com.proway_upskilling.clinica_veterinaria_api.service;

import com.proway_upskilling.clinica_veterinaria_api.dto.VeterinarioDTO;
import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;

import java.util.List;

public interface VeterinarioService {

    VeterinarioDTO create(VeterinarioDTO veterinarioDTO);

    List<VeterinarioDTO> findAll();

    void delete(long id);

    VeterinarioDTO findById(long id);

    VeterinarioDTO save(VeterinarioDTO veterinarioDTO);
}
