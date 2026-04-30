package br.com.sistemaacademico.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.CursoRequestDTO;
import br.com.sistemaacademico.dto.CursoResumoDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.repository.AlunoRepository;
import br.com.sistemaacademico.repository.CursoRepository;
import br.com.sistemaacademico.repository.DisciplinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public CursoService(
            CursoRepository cursoRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.cursoRepository = cursoRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public CursoModel buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
    }

    public Page<CursoModel> listarPaginado(Pageable pageable) {
        return cursoRepository.findAll(pageable);
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

    public CursoResumoDTO gerarResumoCurso(Long idCurso) {
        CursoModel curso = buscarPorId(idCurso);

        Long totalAlunos = alunoRepository.countByCursoIdCurso(idCurso);
        Long totalDisciplinas = disciplinaRepository.countByCursoIdCurso(idCurso);

        return new CursoResumoDTO(
                curso.getIdCurso(),
                curso.getNomeCurso(),
                curso.getDescricaoCurso(),
                totalAlunos,
                totalDisciplinas
        );
    }
}