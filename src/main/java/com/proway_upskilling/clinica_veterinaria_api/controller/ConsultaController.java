package com.proway_upskilling.clinica_veterinaria_api.controller;

import java.util.List;
import java.util.Map;


import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaDTO> criar(@RequestBody @Valid ConsultaDTO dto,
                                             Authentication authentication) {
        Map<String, Object> dadosToken = (Map<String, Object>) authentication.getDetails();
        Long veterinarioId = null;
        if (dadosToken != null && dadosToken.get("veterinario_id") != null) {
            veterinarioId = Long.parseLong(dadosToken.get("veterinario_id").toString());
        }
        dto.setVeterinarioId(veterinarioId);
        return ResponseEntity.ok(consultaService.criarConsulta(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> atualizar(@PathVariable Long id,
                                                 @RequestBody @Valid ConsultaDTO dto,
                                                 Authentication authentication) {
        Map<String, Object> dadosToken = (Map<String, Object>) authentication.getDetails();
        Long veterinarioId = Long.parseLong(dadosToken.get("veterinario_id").toString());

        if (dto.getVeterinarioId() != null && !dto.getVeterinarioId().equals(veterinarioId)) {
            return ResponseEntity.status(403).build();
        }

        dto.setVeterinarioId(veterinarioId);
        return ResponseEntity.ok(consultaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> dadosToken = (Map<String, Object>) authentication.getDetails();
        Long veterinarioId = Long.parseLong(dadosToken.get("veterinario_id").toString());

        // opcional: validar propriedade antes de deletar na service
        consultaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}