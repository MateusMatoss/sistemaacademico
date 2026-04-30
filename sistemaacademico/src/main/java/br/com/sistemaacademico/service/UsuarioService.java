package br.com.sistemaacademico.service;

import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
}