package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.dto.ApiResponseDTO;
import java.time.LocalDateTime;
import br.com.sistemaacademico.dto.FrequenciaRequestDTO;
import br.com.sistemaacademico.model.FrequenciaModel;
import br.com.sistemaacademico.service.FrequenciaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frequencias")
public class FrequenciaController {

    private final FrequenciaService frequenciaService;

    public FrequenciaController(FrequenciaService frequenciaService) {
        this.frequenciaService = frequenciaService;
    }

    @GetMapping
    public List<FrequenciaModel> listarTodos() {
        return frequenciaService.listarTodos();
    }

    @GetMapping("/paginado")
    public Page<FrequenciaModel> listarPaginado(
            @PageableDefault(size = 10, sort = "dataAula", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return frequenciaService.listarPaginado(pageable);
    }

    @GetMapping("/{id}")
    public FrequenciaModel buscarPorId(@PathVariable Long id) {
        return frequenciaService.buscarPorId(id);
    }

    @GetMapping("/matricula/{idMatricula}")
    public List<FrequenciaModel> listarPorMatricula(@PathVariable Long idMatricula) {
        return frequenciaService.listarPorMatricula(idMatricula);
    }

    @PostMapping
    public FrequenciaModel salvar(@RequestBody @Valid FrequenciaRequestDTO frequenciaDTO) {
        return frequenciaService.salvar(frequenciaDTO);
    }

    @PutMapping("/{id}")
    public FrequenciaModel atualizar(@PathVariable Long id, @RequestBody @Valid FrequenciaRequestDTO frequenciaDTO) {
        return frequenciaService.atualizar(id, frequenciaDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponseDTO<Void> deletar(@PathVariable Long id) {
        frequenciaService.deletar(id);

        return new ApiResponseDTO<>(
                LocalDateTime.now(),
                200,
                "Frequência excluída com sucesso",
                null
        );
    }
}