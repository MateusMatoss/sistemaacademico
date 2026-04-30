package br.com.sistemaacademico.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.FrequenciaRequestDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
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

    public List<FrequenciaModel> listarPorMatricula(Long idMatricula) {
        return frequenciaRepository.findByMatriculaIdMatricula(idMatricula);
    }

    public List<FrequenciaModel> listarTodos() {
        return frequenciaRepository.findAll();
    }

    public FrequenciaModel buscarPorId(Long id) {
        return frequenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Frequência não encontrada"));
    }

    public Page<FrequenciaModel> listarPaginado(Pageable pageable) {
        return frequenciaRepository.findAll(pageable);
    }

    public FrequenciaModel salvar(FrequenciaRequestDTO dto) {
        MatriculaModel matricula = matriculaRepository.findById(dto.getIdMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada"));

        FrequenciaModel frequencia = new FrequenciaModel();

        frequencia.setPresente(dto.getPresente());
        frequencia.setMatricula(matricula);

        if (dto.getDataAula() != null) {
            frequencia.setDataAula(dto.getDataAula());
        } else {
            frequencia.setDataAula(LocalDate.now());
        }

        return frequenciaRepository.save(frequencia);
    }

    public FrequenciaModel atualizar(Long id, FrequenciaRequestDTO dto) {
        FrequenciaModel frequenciaExistente = buscarPorId(id);

        MatriculaModel matricula = matriculaRepository.findById(dto.getIdMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada"));

        frequenciaExistente.setPresente(dto.getPresente());
        frequenciaExistente.setMatricula(matricula);

        if (dto.getDataAula() != null) {
            frequenciaExistente.setDataAula(dto.getDataAula());
        }

        return frequenciaRepository.save(frequenciaExistente);
    }

    public void deletar(Long id) {
        FrequenciaModel frequencia = buscarPorId(id);
        frequenciaRepository.delete(frequencia);
    }
}