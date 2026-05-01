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

    @GetMapping("/ativos")
    public List<UsuarioModel> listarAtivos() {
        return usuarioService.listarAtivos();
    }

    @GetMapping("/inativos")
    public List<UsuarioModel> listarInativos() {
        return usuarioService.listarInativos();
    }

    @GetMapping("/{id}")
    public UsuarioModel buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/username/{username}")
    public UsuarioModel buscarPorUsername(@PathVariable String username) {
        return usuarioService.buscarPorUsername(username);
    }

    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizacaoDTO usuarioDTO) {
        return usuarioService.atualizar(id, usuarioDTO);
    }

    @PutMapping("/{id}/alterar-senha")
    public UsuarioModel alterarSenha(@PathVariable Long id, @RequestBody @Valid AlterarSenhaDTO dto) {
        return usuarioService.alterarSenha(id, dto);
    }

    @PutMapping("/{id}/alterar-perfil")
    public UsuarioModel alterarPerfil(@PathVariable Long id, @RequestBody @Valid AlterarPerfilDTO dto) {
        return usuarioService.alterarPerfil(id, dto);
    }

    @PutMapping("/{id}/ativar")
    public UsuarioModel ativarUsuario(@PathVariable Long id) {
        return usuarioService.ativarUsuario(id);
    }

    @PutMapping("/{id}/desativar")
    public UsuarioModel desativarUsuario(@PathVariable Long id) {
        return usuarioService.desativarUsuario(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }
}