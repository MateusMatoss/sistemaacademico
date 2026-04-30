package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.DisciplinaRequestDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.model.ProfessorModel;
import br.com.sistemaacademico.repository.CursoRepository;
import br.com.sistemaacademico.repository.DisciplinaRepository;
import br.com.sistemaacademico.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final CursoRepository cursoRepository;
    private final ProfessorRepository professorRepository;

    public DisciplinaService(
            DisciplinaRepository disciplinaRepository,
            CursoRepository cursoRepository,
            ProfessorRepository professorRepository
    ) {
        this.disciplinaRepository = disciplinaRepository;
        this.cursoRepository = cursoRepository;
        this.professorRepository = professorRepository;
    }

    public List<DisciplinaModel> listarTodos() {
        return disciplinaRepository.findAll();
    }

    public DisciplinaModel buscarPorId(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada"));
    }

    public DisciplinaModel salvar(DisciplinaRequestDTO dto) {
        DisciplinaModel disciplina = new DisciplinaModel();

        disciplina.setNomeDisciplina(dto.getNomeDisciplina());
        disciplina.setCargaHoraria(dto.getCargaHoraria());

        if (dto.getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            disciplina.setCurso(curso);
        }

        if (dto.getIdProfessor() != null) {
            ProfessorModel professor = professorRepository.findById(dto.getIdProfessor())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
            disciplina.setProfessor(professor);
        }

        return disciplinaRepository.save(disciplina);
    }

    public DisciplinaModel atualizar(Long id, DisciplinaRequestDTO dto) {
        DisciplinaModel disciplinaExistente = buscarPorId(id);

        disciplinaExistente.setNomeDisciplina(dto.getNomeDisciplina());
        disciplinaExistente.setCargaHoraria(dto.getCargaHoraria());

        if (dto.getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            disciplinaExistente.setCurso(curso);
        } else {
            disciplinaExistente.setCurso(null);
        }

        if (dto.getIdProfessor() != null) {
            ProfessorModel professor = professorRepository.findById(dto.getIdProfessor())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
            disciplinaExistente.setProfessor(professor);
        } else {
            disciplinaExistente.setProfessor(null);
        }

        return disciplinaRepository.save(disciplinaExistente);
    }

    public void deletar(Long id) {
        DisciplinaModel disciplina = buscarPorId(id);
        disciplinaRepository.delete(disciplina);
    }
}