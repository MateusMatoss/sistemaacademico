package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.LoginDTO;
import br.com.sistemaacademico.dto.UsuarioRegistroDTO;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public UsuarioModel registrar(@RequestBody @Valid UsuarioRegistroDTO dto) {
        return authService.registrar(dto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid LoginDTO dto) {
        return authService.login(dto);
    }
}