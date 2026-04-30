package br.com.sistemaacademico.service;

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

    public DisciplinaModel salvar(DisciplinaModel disciplina) {
        if (disciplina.getCurso() != null && disciplina.getCurso().getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(disciplina.getCurso().getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            disciplina.setCurso(curso);
        }

        if (disciplina.getProfessor() != null && disciplina.getProfessor().getIdPessoa() != null) {
            ProfessorModel professor = professorRepository.findById(disciplina.getProfessor().getIdPessoa())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
            disciplina.setProfessor(professor);
        }

        return disciplinaRepository.save(disciplina);
    }

    public DisciplinaModel atualizar(Long id, DisciplinaModel disciplinaAtualizada) {
        DisciplinaModel disciplinaExistente = buscarPorId(id);

        disciplinaExistente.setNomeDisciplina(disciplinaAtualizada.getNomeDisciplina());
        disciplinaExistente.setNotaDisciplina(disciplinaAtualizada.getNotaDisciplina());
        disciplinaExistente.setCargaHoraria(disciplinaAtualizada.getCargaHoraria());
        disciplinaExistente.setPresencaDisciplina(disciplinaAtualizada.getPresencaDisciplina());

        if (disciplinaAtualizada.getCurso() != null && disciplinaAtualizada.getCurso().getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(disciplinaAtualizada.getCurso().getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            disciplinaExistente.setCurso(curso);
        }

        if (disciplinaAtualizada.getProfessor() != null && disciplinaAtualizada.getProfessor().getIdPessoa() != null) {
            ProfessorModel professor = professorRepository.findById(disciplinaAtualizada.getProfessor().getIdPessoa())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
            disciplinaExistente.setProfessor(professor);
        }

        return disciplinaRepository.save(disciplinaExistente);
    }

    public void deletar(Long id) {
        DisciplinaModel disciplina = buscarPorId(id);
        disciplinaRepository.delete(disciplina);
    }
}