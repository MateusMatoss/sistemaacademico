package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.UsuarioResponseDTO;
import br.com.sistemaacademico.model.PerfilUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.AlterarPerfilDTO;
import br.com.sistemaacademico.dto.AlterarSenhaDTO;
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

    public Page<UsuarioModel> listarPaginado(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public List<UsuarioModel> listarPorPerfil(PerfilUsuario perfil) {
        return usuarioRepository.findByPerfil(perfil);
    }


    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel alterarSenha(Long id, AlterarSenhaDTO dto) {
        UsuarioModel usuario = buscarPorId(id);

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));

        return usuarioRepository.save(usuario);
    }

    public UsuarioModel ativarUsuario(Long id) {
        UsuarioModel usuario = buscarPorId(id);

        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    public UsuarioModel desativarUsuario(Long id) {
        UsuarioModel usuario = buscarPorId(id);

        usuario.setAtivo(false);

        return usuarioRepository.save(usuario);
    }

    public List<UsuarioModel> listarAtivos() {
        return usuarioRepository.findByAtivo(true);
    }

    public List<UsuarioModel> listarInativos() {
        return usuarioRepository.findByAtivo(false);
    }

    public UsuarioModel alterarPerfil(Long id, AlterarPerfilDTO dto) {
        UsuarioModel usuario = buscarPorId(id);

        usuario.setPerfil(dto.getPerfil());

        return usuarioRepository.save(usuario);
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
    public UsuarioResponseDTO converterParaResponseDTO(UsuarioModel usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                usuario.getDataCriacao(),
                usuario.getDataAtualizacao()
        );
    }
}