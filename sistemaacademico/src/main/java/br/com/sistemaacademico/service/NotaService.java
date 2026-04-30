package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.NotaRequestDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.MatriculaModel;
import br.com.sistemaacademico.model.NotaModel;
import br.com.sistemaacademico.repository.MatriculaRepository;
import br.com.sistemaacademico.repository.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaService {

    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;

    public NotaService(NotaRepository notaRepository, MatriculaRepository matriculaRepository) {
        this.notaRepository = notaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<NotaModel> listarPorMatricula(Long idMatricula) {
        return notaRepository.findByMatriculaIdMatricula(idMatricula);
    }

    public List<NotaModel> listarTodos() {
        return notaRepository.findAll();
    }

    public NotaModel buscarPorId(Long id) {
        return notaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nota não encontrada"));
    }

    public NotaModel salvar(NotaRequestDTO dto) {
        MatriculaModel matricula = matriculaRepository.findById(dto.getIdMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada"));

        NotaModel nota = new NotaModel();
        nota.setValorNota(dto.getValorNota());
        nota.setTipoAvaliacao(dto.getTipoAvaliacao());
        nota.setMatricula(matricula);

        return notaRepository.save(nota);
    }

    public NotaModel atualizar(Long id, NotaRequestDTO dto) {
        NotaModel notaExistente = buscarPorId(id);

        MatriculaModel matricula = matriculaRepository.findById(dto.getIdMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada"));

        notaExistente.setValorNota(dto.getValorNota());
        notaExistente.setTipoAvaliacao(dto.getTipoAvaliacao());
        notaExistente.setMatricula(matricula);

        return notaRepository.save(notaExistente);
    }

    public void deletar(Long id) {
        NotaModel nota = buscarPorId(id);
        notaRepository.delete(nota);
    }
}