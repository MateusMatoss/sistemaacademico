package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import br.com.sistemaacademico.service.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioModel usuario) {

        UsuarioModel user = usuarioRepository.findByUsername(usuario.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getPassword().equals(usuario.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        return jwtService.gerarToken(user.getUsername());
    }
}