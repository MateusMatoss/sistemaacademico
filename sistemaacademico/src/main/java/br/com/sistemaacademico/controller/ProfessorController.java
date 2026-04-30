package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.ProfessorRequestDTO;
import br.com.sistemaacademico.dto.ProfessorResumoDTO;
import br.com.sistemaacademico.model.ProfessorModel;
import br.com.sistemaacademico.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public List<ProfessorModel> listarTodos() {
        return professorService.listarTodos();
    }

    @GetMapping("/paginado")
    public Page<ProfessorModel> listarPaginado(
            @PageableDefault(size = 10, sort = "nomePessoa", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return professorService.listarPaginado(pageable);
    }

    @GetMapping("/{id}")
    public ProfessorModel buscarPorId(@PathVariable Long id) {
        return professorService.buscarPorId(id);
    }

    @GetMapping("/resumo/{id}")
    public List<ProfessorResumoDTO> gerarResumoProfessor(@PathVariable Long id) {
        return professorService.gerarResumoProfessor(id);
    }

    @PostMapping
    public ProfessorModel salvar(@RequestBody @Valid ProfessorRequestDTO professorDTO) {
        return professorService.salvar(professorDTO);
    }

    @PutMapping("/{id}")
    public ProfessorModel atualizar(@PathVariable Long id, @RequestBody @Valid ProfessorRequestDTO professorDTO) {
        return professorService.atualizar(id, professorDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        professorService.deletar(id);
    }
}