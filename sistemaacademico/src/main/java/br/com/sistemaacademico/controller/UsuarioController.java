package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.UsuarioModel;
import br.com.sistemaacademico.service.UsuarioService;
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

    @GetMapping("/{id}")
    public UsuarioModel buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/username/{username}")
    public UsuarioModel buscarPorUsername(@PathVariable String username) {
        return usuarioService.buscarPorUsername(username);
    }
}