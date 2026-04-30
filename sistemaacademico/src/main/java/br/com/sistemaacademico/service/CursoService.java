package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.CursoRequestDTO;
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

    public CursoModel salvar(CursoRequestDTO dto) {
        CursoModel curso = new CursoModel();

        curso.setNomeCurso(dto.getNomeCurso());
        curso.setDescricaoCurso(dto.getDescricaoCurso());

        return cursoRepository.save(curso);
    }

    public CursoModel atualizar(Long id, CursoRequestDTO dto) {
        CursoModel cursoExistente = buscarPorId(id);

        cursoExistente.setNomeCurso(dto.getNomeCurso());
        cursoExistente.setDescricaoCurso(dto.getDescricaoCurso());

        return cursoRepository.save(cursoExistente);
    }

    public void deletar(Long id) {
        CursoModel curso = buscarPorId(id);
        cursoRepository.delete(curso);
    }
}