package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.AlunoRequestDTO;
import br.com.sistemaacademico.dto.AlunoResumoDTO;
import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/paginado")
    public Page<AlunoModel> listarPaginado(
            @PageableDefault(
                    size = 10,
                    sort = "nomePessoa",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return alunoService.listarPaginado(pageable);
    }

    @GetMapping("/{id}")
    public AlunoModel buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @GetMapping("/curso/{idCurso}")
    public List<AlunoModel> listarPorCurso(@PathVariable Long idCurso) {
        return alunoService.listarPorCurso(idCurso);
    }

    @GetMapping("/resumo/{id}")
    public List<AlunoResumoDTO> gerarResumoAluno(@PathVariable Long id) {
        return alunoService.gerarResumoAluno(id);
    }

    @PostMapping
    public AlunoModel salvar(@RequestBody @Valid AlunoRequestDTO alunoDTO) {
        return alunoService.salvar(alunoDTO);
    }

    @PutMapping("/{id}")
    public AlunoModel atualizar(@PathVariable Long id, @RequestBody @Valid AlunoRequestDTO alunoDTO) {
        return alunoService.atualizar(id, alunoDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        alunoService.deletar(id);
    }

    @GetMapping("/buscar")
    public List<AlunoModel> buscarPorNome(@RequestParam String nome) {
        return alunoService.buscarPorNome(nome);
    }
}