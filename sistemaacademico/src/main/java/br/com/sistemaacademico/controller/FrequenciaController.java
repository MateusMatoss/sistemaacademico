package br.com.sistemaacademico.controller;

import br.com.sistemaacademico.model.FrequenciaModel;
import br.com.sistemaacademico.service.FrequenciaService;
import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public FrequenciaModel buscarPorId(@PathVariable Long id) {
        return frequenciaService.buscarPorId(id);
    }

    @PostMapping
    public FrequenciaModel salvar(@RequestBody @Valid FrequenciaModel frequencia) {
        return frequenciaService.salvar(frequencia);
    }

    @PutMapping("/{id}")
    public FrequenciaModel atualizar(@PathVariable Long id, @RequestBody @Valid FrequenciaModel frequencia) {
        return frequenciaService.atualizar(id, frequencia);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        frequenciaService.deletar(id);
    }
}