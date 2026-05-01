package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.UsuarioAtualizacaoDTO;
import br.com.sistemaacademico.exception.BusinessException;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.PerfilUsuario;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public UsuarioModel buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado pelo username informado"));
    }

    public UsuarioModel atualizar(Long id, UsuarioAtualizacaoDTO dto) {
        UsuarioModel usuarioExistente = buscarPorId(id);

        usuarioRepository.findByUsername(dto.getUsername()).ifPresent(usuario -> {
            if (!usuario.getIdUsuario().equals(id)) {
                throw new BusinessException("Já existe outro usuário com esse username");
            }
        });

        usuarioExistente.setUsername(dto.getUsername());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getPerfil() != null) {
            usuarioExistente.setPerfil(dto.getPerfil());
        } else if (usuarioExistente.getPerfil() == null) {
            usuarioExistente.setPerfil(PerfilUsuario.ALUNO);
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void deletar(Long id) {
        UsuarioModel usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}