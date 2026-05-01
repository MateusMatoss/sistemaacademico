package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.AlterarPerfilDTO;
import br.com.sistemaacademico.dto.AlterarSenhaDTO;
import br.com.sistemaacademico.dto.UsuarioAtualizacaoDTO;
import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioModel> listarTodos() {
        return usuarioService.listarTodos();
    }

    @PutMapping("/{id}/alterar-senha")
    public UsuarioModel alterarSenha(@PathVariable Long id, @RequestBody @Valid AlterarSenhaDTO dto) {
        return usuarioService.alterarSenha(id, dto);
    }

    @GetMapping("/{id}")
    public UsuarioModel buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}/alterar-perfil")
    public UsuarioModel alterarPerfil(@PathVariable Long id, @RequestBody @Valid AlterarPerfilDTO dto) {
        return usuarioService.alterarPerfil(id, dto);
    }
    
    @GetMapping("/username/{username}")
    public UsuarioModel buscarPorUsername(@PathVariable String username) {
        return usuarioService.buscarPorUsername(username);
    }

    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizacaoDTO usuarioDTO) {
        return usuarioService.atualizar(id, usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }
}