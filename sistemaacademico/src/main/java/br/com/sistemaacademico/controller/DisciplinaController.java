package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public List<DisciplinaModel> listarTodos() {
        return disciplinaService.listarTodos();
    }

    @GetMapping("/{id}")
    public DisciplinaModel buscarPorId(@PathVariable Long id) {
        return disciplinaService.buscarPorId(id);
    }

    @PostMapping
    public DisciplinaModel salvar(@RequestBody @Valid DisciplinaModel disciplina) {
        return disciplinaService.salvar(disciplina);
    }

    @PutMapping("/{id}")
    public DisciplinaModel atualizar(@PathVariable Long id, @RequestBody @Valid DisciplinaModel disciplina) {
        return disciplinaService.atualizar(id, disciplina);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        disciplinaService.deletar(id);
    }
}