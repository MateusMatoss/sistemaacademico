package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.DisciplinaRequestDTO;
import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/paginado")
    public Page<DisciplinaModel> listarPaginado(
            @PageableDefault(size = 10, sort = "nomeDisciplina", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return disciplinaService.listarPaginado(pageable);
    }

    @GetMapping("/{id}")
    public DisciplinaModel buscarPorId(@PathVariable Long id) {
        return disciplinaService.buscarPorId(id);
    }

    @GetMapping("/curso/{idCurso}")
    public List<DisciplinaModel> listarPorCurso(@PathVariable Long idCurso) {
        return disciplinaService.listarPorCurso(idCurso);
    }

    @GetMapping("/professor/{idProfessor}")
    public List<DisciplinaModel> listarPorProfessor(@PathVariable Long idProfessor) {
        return disciplinaService.listarPorProfessor(idProfessor);
    }

    @PostMapping
    public DisciplinaModel salvar(@RequestBody @Valid DisciplinaRequestDTO disciplinaDTO) {
        return disciplinaService.salvar(disciplinaDTO);
    }

    @PutMapping("/{id}")
    public DisciplinaModel atualizar(@PathVariable Long id, @RequestBody @Valid DisciplinaRequestDTO disciplinaDTO) {
        return disciplinaService.atualizar(id, disciplinaDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        disciplinaService.deletar(id);
    }
}