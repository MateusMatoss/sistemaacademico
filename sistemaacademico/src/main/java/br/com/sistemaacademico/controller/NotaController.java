package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.NotaRequestDTO;
import br.com.sistemaacademico.model.NotaModel;
import br.com.sistemaacademico.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/paginado")
    public Page<NotaModel> listarPaginado(
            @PageableDefault(size = 10, sort = "valorNota", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return notaService.listarPaginado(pageable);
    }

    @GetMapping("/{id}")
    public NotaModel buscarPorId(@PathVariable Long id) {
        return notaService.buscarPorId(id);
    }

    @GetMapping("/matricula/{idMatricula}")
    public List<NotaModel> listarPorMatricula(@PathVariable Long idMatricula) {
        return notaService.listarPorMatricula(idMatricula);
    }

    @PostMapping
    public NotaModel salvar(@RequestBody @Valid NotaRequestDTO notaDTO) {
        return notaService.salvar(notaDTO);
    }

    @PutMapping("/{id}")
    public NotaModel atualizar(@PathVariable Long id, @RequestBody @Valid NotaRequestDTO notaDTO) {
        return notaService.atualizar(id, notaDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        notaService.deletar(id);
    }
}