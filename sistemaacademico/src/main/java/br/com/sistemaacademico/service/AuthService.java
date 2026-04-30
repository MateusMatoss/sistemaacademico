package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.LoginDTO;
import br.com.sistemaacademico.dto.UsuarioRegistroDTO;
import br.com.sistemaacademico.exception.BusinessException;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioModel registrar(UsuarioRegistroDTO dto) {

        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new BusinessException("Já existe um usuário cadastrado com esse username");
        }

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

    public Map<String, String> login(LoginDTO dto) {
        UsuarioModel usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new BusinessException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario.getUsername());

        return Map.of(
                "token", token,
                "perfil", usuario.getPerfil().name()
        );
    }
}