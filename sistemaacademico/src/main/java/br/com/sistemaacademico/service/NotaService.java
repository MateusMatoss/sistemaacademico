package br.com.sistemaacademico.service;

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

    public List<NotaModel> listarTodos() {
        return notaRepository.findAll();
    }

    public NotaModel buscarPorId(Long id) {
        return notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
    }

    public NotaModel salvar(NotaModel nota) {
        MatriculaModel matricula = matriculaRepository.findById(nota.getMatricula().getIdMatricula())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        nota.setMatricula(matricula);

        return notaRepository.save(nota);
    }

    public NotaModel atualizar(Long id, NotaModel notaAtualizada) {
        NotaModel notaExistente = buscarPorId(id);

        notaExistente.setValorNota(notaAtualizada.getValorNota());
        notaExistente.setTipoAvaliacao(notaAtualizada.getTipoAvaliacao());

        if (notaAtualizada.getMatricula() != null && notaAtualizada.getMatricula().getIdMatricula() != null) {
            MatriculaModel matricula = matriculaRepository.findById(notaAtualizada.getMatricula().getIdMatricula())
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
            notaExistente.setMatricula(matricula);
        }

        return notaRepository.save(notaExistente);
    }

    public void deletar(Long id) {
        NotaModel nota = buscarPorId(id);
        notaRepository.delete(nota);
    }
}