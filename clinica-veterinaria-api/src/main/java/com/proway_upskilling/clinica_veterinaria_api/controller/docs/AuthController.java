package com.proway_upskilling.clinica_veterinaria_api.controller.docs;



import com.proway_upskilling.clinica_veterinaria_api.model.Veterinario;
import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import com.proway_upskilling.clinica_veterinaria_api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final VeterinarioRepository veterinarioRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestParam String crmv) {
        Veterinario vet = veterinarioRepository.findByCrmv(crmv);

        if (vet == null) {
            return ResponseEntity.status(401).body("CRMV inválido");
        }

        String token = jwtUtil.generateToken(vet.getCrmv(), vet.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("veterinario_id", vet.getId());
        response.put("crmv", vet.getCrmv());

        return ResponseEntity.ok(response);
    }
}
