package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.NotaModel;
import br.com.sistemaacademico.service.NotaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notas")
public class NotaController {

    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    @GetMapping
    public List<NotaModel> listarTodos() {
        return notaService.listarTodos();
    }

    @GetMapping("/{id}")
    public NotaModel buscarPorId(@PathVariable Long id) {
        return notaService.buscarPorId(id);
    }

    @PostMapping
    public NotaModel salvar(@RequestBody NotaModel nota) {
        return notaService.salvar(nota);
    }

    @PutMapping("/{id}")
    public NotaModel atualizar(@PathVariable Long id, @RequestBody NotaModel nota) {
        return notaService.atualizar(id, nota);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        notaService.deletar(id);
    }
}