package br.com.sistemaacademico.service;

import br.com.sistemaacademico.model.FrequenciaModel;
import br.com.sistemaacademico.model.MatriculaModel;
import br.com.sistemaacademico.repository.FrequenciaRepository;
import br.com.sistemaacademico.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FrequenciaService {

    private final FrequenciaRepository frequenciaRepository;
    private final MatriculaRepository matriculaRepository;

    public FrequenciaService(FrequenciaRepository frequenciaRepository, MatriculaRepository matriculaRepository) {
        this.frequenciaRepository = frequenciaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<FrequenciaModel> listarTodos() {
        return frequenciaRepository.findAll();
    }

    public FrequenciaModel buscarPorId(Long id) {
        return frequenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Frequência não encontrada"));
    }

    public FrequenciaModel salvar(FrequenciaModel frequencia) {
        MatriculaModel matricula = matriculaRepository.findById(frequencia.getMatricula().getIdMatricula())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        frequencia.setMatricula(matricula);

        if (frequencia.getDataAula() == null) {
            frequencia.setDataAula(LocalDate.now());
        }

        return frequenciaRepository.save(frequencia);
    }

    public FrequenciaModel atualizar(Long id, FrequenciaModel frequenciaAtualizada) {
        FrequenciaModel frequenciaExistente = buscarPorId(id);

        frequenciaExistente.setDataAula(frequenciaAtualizada.getDataAula());
        frequenciaExistente.setPresente(frequenciaAtualizada.getPresente());

        if (frequenciaAtualizada.getMatricula() != null && frequenciaAtualizada.getMatricula().getIdMatricula() != null) {
            MatriculaModel matricula = matriculaRepository.findById(frequenciaAtualizada.getMatricula().getIdMatricula())
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
            frequenciaExistente.setMatricula(matricula);
        }

        return frequenciaRepository.save(frequenciaExistente);
    }

    public void deletar(Long id) {
        FrequenciaModel frequencia = buscarPorId(id);
        frequenciaRepository.delete(frequencia);
    }
}