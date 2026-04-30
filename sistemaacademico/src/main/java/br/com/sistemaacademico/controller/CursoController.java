package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.CursoRequestDTO;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<CursoModel> listarTodos() {
        return cursoService.listarTodos();
    }

    @GetMapping("/{id}")
    public CursoModel buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    @PostMapping
    public CursoModel salvar(@RequestBody @Valid CursoRequestDTO cursoDTO) {
        return cursoService.salvar(cursoDTO);
    }

    @PutMapping("/{id}")
    public CursoModel atualizar(@PathVariable Long id, @RequestBody @Valid CursoRequestDTO cursoDTO) {
        return cursoService.atualizar(id, cursoDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        cursoService.deletar(id);
    }
}