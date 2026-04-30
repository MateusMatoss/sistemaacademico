package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public List<AlunoModel> listarTodos() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public AlunoModel buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @PostMapping
    public AlunoModel salvar(@RequestBody @Valid AlunoModel aluno) {
        return alunoService.salvar(aluno);
    }

    @PutMapping("/{id}")
    public AlunoModel atualizar(@PathVariable Long id, @RequestBody @Valid AlunoModel aluno) {
        return alunoService.atualizar(id, aluno);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        alunoService.deletar(id);
    }
}