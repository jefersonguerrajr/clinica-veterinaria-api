package com.proway_upskilling.clinica_veterinaria_api.controller.docs;



import com.proway_upskilling.clinica_veterinaria_api.repository.VeterinarioRepository;
import com.proway_upskilling.clinica_veterinaria_api.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final VeterinarioRepository veterinarioRepository;
    private final JwtUtil jwtUtil;

    public AuthController(VeterinarioRepository veterinarioRepository, JwtUtil jwtUtil) {
        this.veterinarioRepository = veterinarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String crmv) {
        return veterinarioRepository.findByCrmv(crmv)
                .map(vet -> ResponseEntity.ok(jwtUtil.generateToken(vet.getCrmv())))
                .orElse(ResponseEntity.status(401).body("CRMV inválido"));
    }
}
