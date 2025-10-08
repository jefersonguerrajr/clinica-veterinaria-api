package com.proway_upskilling.clinica_veterinaria_api.controller.docs;



import com.proway_upskilling.clinica_veterinaria_api.model.dto.ConsultaDTO;
import com.proway_upskilling.clinica_veterinaria_api.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaControllerSecurity {

    private final ConsultaService consultaService;

    @PreAuthorize("hasAuthority('ROLE_VET')")
    @PostMapping
    public ResponseEntity<ConsultaDTO> criar(@RequestBody @Valid ConsultaDTO dto,
                                             Authentication authentication) {
        Map<String, Object> dadosToken = (Map<String, Object>) authentication.getDetails();
        Long veterinarioId = null;
        if (dadosToken != null && dadosToken.get("veterinario_id") != null) {
            veterinarioId = Long.parseLong(dadosToken.get("veterinario_id").toString());
        }
        dto.setVeterinarioId(veterinarioId);
        var saved = consultaService.criarConsulta(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @PreAuthorize("hasAuthority('ROLE_VET')")
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

    @PreAuthorize("hasAuthority('ROLE_VET')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> dadosToken = (Map<String, Object>) authentication.getDetails();
        Long veterinarioId = Long.parseLong(dadosToken.get("veterinario_id").toString());

        // opcional: validar propriedade antes de deletar na service
        consultaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
