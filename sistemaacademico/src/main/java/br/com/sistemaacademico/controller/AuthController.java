package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.LoginDTO;
import br.com.sistemaacademico.dto.UsuarioRegistroDTO;
import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import br.com.sistemaacademico.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registrar")
    public UsuarioModel registrar(@RequestBody @Valid UsuarioRegistroDTO dto) {

        UsuarioModel usuario = new UsuarioModel();

        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getPerfil() == null) {
            usuario.setPerfil(PerfilUsuario.ALUNO);
        } else {
            usuario.setPerfil(dto.getPerfil());
        }

        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid LoginDTO dto) {

        UsuarioModel user = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.gerarToken(user.getUsername());

        return Map.of(
                "token", token,
                "perfil", user.getPerfil().name()
        );
    }
}