package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.LogAcessoModel;
import br.com.sistemaacademico.service.LogAcessoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs-acesso")
public class LogAcessoController {

    private final LogAcessoService logAcessoService;

    public LogAcessoController(LogAcessoService logAcessoService) {
        this.logAcessoService = logAcessoService;
    }

    @GetMapping
    public List<LogAcessoModel> listarTodos() {
        return logAcessoService.listarTodos();
    }

    @GetMapping("/usuario/{username}")
    public List<LogAcessoModel> listarPorUsername(@PathVariable String username) {
        return logAcessoService.listarPorUsername(username);
    }
}