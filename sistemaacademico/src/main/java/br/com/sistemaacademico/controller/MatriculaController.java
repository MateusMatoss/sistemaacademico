package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.MatriculaModel;
import br.com.sistemaacademico.service.MatriculaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    public List<MatriculaModel> listarTodos() {
        return matriculaService.listarTodos();
    }

    @GetMapping("/{id}")
    public MatriculaModel buscarPorId(@PathVariable Long id) {
        return matriculaService.buscarPorId(id);
    }

    @PostMapping
    public MatriculaModel salvar(@RequestBody MatriculaModel matricula) {
        return matriculaService.salvar(matricula);
    }

    @PutMapping("/{id}")
    public MatriculaModel atualizar(@PathVariable Long id, @RequestBody MatriculaModel matricula) {
        return matriculaService.atualizar(id, matricula);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        matriculaService.deletar(id);
    }
}