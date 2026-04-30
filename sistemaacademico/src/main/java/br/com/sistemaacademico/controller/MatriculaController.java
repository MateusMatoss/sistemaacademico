package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.MatriculaRequestDTO;
import br.com.sistemaacademico.model.MatriculaModel;
import br.com.sistemaacademico.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/paginado")
    public Page<MatriculaModel> listarPaginado(
            @PageableDefault(size = 10, sort = "dataMatricula", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return matriculaService.listarPaginado(pageable);
    }

    @GetMapping("/{id}")
    public MatriculaModel buscarPorId(@PathVariable Long id) {
        return matriculaService.buscarPorId(id);
    }

    @PostMapping
    public MatriculaModel salvar(@RequestBody @Valid MatriculaRequestDTO matriculaDTO) {
        return matriculaService.salvar(matriculaDTO);
    }

    @PutMapping("/{id}")
    public MatriculaModel atualizar(@PathVariable Long id, @RequestBody @Valid MatriculaRequestDTO matriculaDTO) {
        return matriculaService.atualizar(id, matriculaDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        matriculaService.deletar(id);
    }
}