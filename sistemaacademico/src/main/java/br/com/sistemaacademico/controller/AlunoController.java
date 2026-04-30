package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.AlunoResumoDTO;
import br.com.sistemaacademico.dto.AlunoRequestDTO;
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

    @GetMapping("/{id}/resumo")
    public List<AlunoResumoDTO> gerarResumoAluno(@PathVariable Long id) {
        return alunoService.gerarResumoAluno(id);
    }

    @GetMapping("/curso/{idCurso}")
    public List<AlunoModel> listarPorCurso(@PathVariable Long idCurso) {
        return alunoService.listarPorCurso(idCurso);
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
}