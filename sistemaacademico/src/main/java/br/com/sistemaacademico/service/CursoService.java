package br.com.sistemaacademico.service;

import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public CursoModel buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
    }

    public CursoModel salvar(CursoModel curso) {
        return cursoRepository.save(curso);
    }

    public CursoModel atualizar(Long id, CursoModel cursoAtualizado) {
        CursoModel cursoExistente = buscarPorId(id);

        cursoExistente.setNomeCurso(cursoAtualizado.getNomeCurso());
        cursoExistente.setDescricaoCurso(cursoAtualizado.getDescricaoCurso());

        return cursoRepository.save(cursoExistente);
    }

    public void deletar(Long id) {
        CursoModel curso = buscarPorId(id);
        cursoRepository.delete(curso);
    }
}