package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.AlunoRequestDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.repository.AlunoRepository;
import br.com.sistemaacademico.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public AlunoService(AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<AlunoModel> listarPorCurso(Long idCurso) {
        return alunoRepository.findByCursoIdCurso(idCurso);
    }

    public List<AlunoModel> listarTodos() {
        return alunoRepository.findAll();
    }

    public AlunoModel buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    }

    public AlunoModel salvar(AlunoRequestDTO dto) {
        AlunoModel aluno = new AlunoModel();

        aluno.setNomePessoa(dto.getNomePessoa());
        aluno.setCpfPessoa(dto.getCpfPessoa());
        aluno.setEmailPessoa(dto.getEmailPessoa());
        aluno.setRaAluno(dto.getRaAluno());

        if (dto.getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            aluno.setCurso(curso);
        }

        return alunoRepository.save(aluno);
    }

    public AlunoModel atualizar(Long id, AlunoRequestDTO dto) {
        AlunoModel alunoExistente = buscarPorId(id);

        alunoExistente.setNomePessoa(dto.getNomePessoa());
        alunoExistente.setCpfPessoa(dto.getCpfPessoa());
        alunoExistente.setEmailPessoa(dto.getEmailPessoa());
        alunoExistente.setRaAluno(dto.getRaAluno());

        if (dto.getIdCurso() != null) {
            CursoModel curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
            alunoExistente.setCurso(curso);
        } else {
            alunoExistente.setCurso(null);
        }

        return alunoRepository.save(alunoExistente);
    }

    public void deletar(Long id) {
        AlunoModel aluno = buscarPorId(id);
        alunoRepository.delete(aluno);
    }
}