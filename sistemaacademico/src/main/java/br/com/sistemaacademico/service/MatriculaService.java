package br.com.sistemaacademico.service;

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

    public MatriculaModel buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
    }

    public MatriculaModel salvar(MatriculaModel matricula) {
        AlunoModel aluno = alunoRepository.findById(matricula.getAluno().getIdPessoa())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        DisciplinaModel disciplina = disciplinaRepository.findById(matricula.getDisciplina().getIdDisciplina())
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);

        if (matricula.getDataMatricula() == null) {
            matricula.setDataMatricula(LocalDate.now());
        }

        return matriculaRepository.save(matricula);
    }

    public MatriculaModel atualizar(Long id, MatriculaModel matriculaAtualizada) {
        MatriculaModel matriculaExistente = buscarPorId(id);

        if (matriculaAtualizada.getDataMatricula() != null) {
            matriculaExistente.setDataMatricula(matriculaAtualizada.getDataMatricula());
        }

        if (matriculaAtualizada.getAluno() != null && matriculaAtualizada.getAluno().getIdPessoa() != null) {
            AlunoModel aluno = alunoRepository.findById(matriculaAtualizada.getAluno().getIdPessoa())
                    .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            matriculaExistente.setAluno(aluno);
        }

        if (matriculaAtualizada.getDisciplina() != null && matriculaAtualizada.getDisciplina().getIdDisciplina() != null) {
            DisciplinaModel disciplina = disciplinaRepository.findById(matriculaAtualizada.getDisciplina().getIdDisciplina())
                    .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
            matriculaExistente.setDisciplina(disciplina);
        }

        return matriculaRepository.save(matriculaExistente);
    }

    public void deletar(Long id) {
        MatriculaModel matricula = buscarPorId(id);
        matriculaRepository.delete(matricula);
    }
}