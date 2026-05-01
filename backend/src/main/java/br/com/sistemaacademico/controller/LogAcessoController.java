package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.LogAcessoModel;
import br.com.sistemaacademico.service.LogAcessoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/paginado")
    public Page<LogAcessoModel> listarPaginado(
            @PageableDefault(
                    size = 10,
                    sort = "dataHoraLogin",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return logAcessoService.listarPaginado(pageable);
    }

    @GetMapping("/usuario/{username}")
    public List<LogAcessoModel> listarPorUsername(@PathVariable String username) {
        return logAcessoService.listarPorUsername(username);
    }

    @GetMapping("/perfil/{perfil}")
    public List<LogAcessoModel> listarPorPerfil(@PathVariable String perfil) {
        return logAcessoService.listarPorPerfil(perfil);
    }

    @GetMapping("/periodo")
    public List<LogAcessoModel> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return logAcessoService.listarPorPeriodo(inicio, fim);
    }
}