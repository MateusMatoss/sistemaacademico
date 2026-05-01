package br.com.sistemaacademico.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.MatriculaRequestDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.model.MatriculaModel;
import br.com.sistemaacademico.repository.AlunoRepository;
import br.com.sistemaacademico.repository.DisciplinaRepository;
import br.com.sistemaacademico.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<MatriculaModel> listarTodos() {
        return matriculaRepository.findAll();
    }

    public List<MatriculaModel> listarPorDisciplina(Long idDisciplina) {
        return matriculaRepository.findByDisciplinaIdDisciplina(idDisciplina);
    }

    public MatriculaModel buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada"));
    }

    public Page<MatriculaModel> listarPaginado(Pageable pageable) {
        return matriculaRepository.findAll(pageable);
    }

    public MatriculaModel salvar(MatriculaRequestDTO dto) {
        AlunoModel aluno = alunoRepository.findById(dto.getIdAluno())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        DisciplinaModel disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada"));

        MatriculaModel matricula = new MatriculaModel();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);

        if (dto.getDataMatricula() != null) {
            matricula.setDataMatricula(dto.getDataMatricula());
        } else {
            matricula.setDataMatricula(LocalDate.now());
        }

        return matriculaRepository.save(matricula);
    }

    public MatriculaModel atualizar(Long id, MatriculaRequestDTO dto) {
        MatriculaModel matriculaExistente = buscarPorId(id);

        if (dto.getDataMatricula() != null) {
            matriculaExistente.setDataMatricula(dto.getDataMatricula());
        }

        AlunoModel aluno = alunoRepository.findById(dto.getIdAluno())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        DisciplinaModel disciplina = disciplinaRepository.findById(dto.getIdDisciplina())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada"));

        matriculaExistente.setAluno(aluno);
        matriculaExistente.setDisciplina(disciplina);

        return matriculaRepository.save(matriculaExistente);
    }

    public void deletar(Long id) {
        MatriculaModel matricula = buscarPorId(id);
        matriculaRepository.delete(matricula);
    }
}
