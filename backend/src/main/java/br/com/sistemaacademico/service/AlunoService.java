package br.com.sistemaacademico.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.AlunoRequestDTO;
import br.com.sistemaacademico.dto.AlunoResumoDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.model.CursoModel;
import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.model.FrequenciaModel;
import br.com.sistemaacademico.model.MatriculaModel;
import br.com.sistemaacademico.model.NotaModel;
import br.com.sistemaacademico.repository.AlunoRepository;
import br.com.sistemaacademico.repository.CursoRepository;
import br.com.sistemaacademico.repository.FrequenciaRepository;
import br.com.sistemaacademico.repository.MatriculaRepository;
import br.com.sistemaacademico.repository.NotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final NotaRepository notaRepository;
    private final FrequenciaRepository frequenciaRepository;

    public AlunoService(
            AlunoRepository alunoRepository,
            CursoRepository cursoRepository,
            MatriculaRepository matriculaRepository,
            NotaRepository notaRepository,
            FrequenciaRepository frequenciaRepository
    ) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
        this.notaRepository = notaRepository;
        this.frequenciaRepository = frequenciaRepository;
    }

    public Page<AlunoModel> listarPaginado(Pageable pageable) {
        return alunoRepository.findAll(pageable);
    }

    public List<AlunoModel> listarTodos() {
        return alunoRepository.findAll();
    }

    public AlunoModel buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    }

    public List<AlunoModel> buscarPorNome(String nome) {
        return alunoRepository.findByNomePessoaContainingIgnoreCase(nome);
    }

    public List<AlunoModel> listarPorCurso(Long idCurso) {
        return alunoRepository.findByCursoIdCurso(idCurso);
    }

    public AlunoModel buscarPorCpf(String cpf) {
        return alunoRepository.findByCpfPessoa(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado pelo CPF informado"));
    }

    public AlunoModel buscarPorRa(String ra) {
        return alunoRepository.findByRaAluno(ra)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado pelo RA informado"));
    }

    public AlunoModel buscarPorUsernameVinculado(String username) {
        return alunoRepository.findByRaAluno(username)
                .or(() -> alunoRepository.findByCpfPessoa(username))
                .or(() -> alunoRepository.findByEmailPessoaIgnoreCase(username))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nenhum aluno vinculado foi encontrado para o usuário logado. Use RA, CPF ou e-mail como username."
                ));
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

    public List<AlunoResumoDTO> gerarResumoAluno(Long idAluno) {
        AlunoModel aluno = buscarPorId(idAluno);

        List<MatriculaModel> matriculas = matriculaRepository.findByAlunoIdPessoa(idAluno);

        return matriculas.stream().map(matricula -> {
            Double mediaNotas = notaRepository.findByMatriculaIdMatricula(matricula.getIdMatricula())
                    .stream()
                    .mapToDouble(NotaModel::getValorNota)
                    .average()
                    .orElse(0.0);

            Long totalPresencas = frequenciaRepository.findByMatriculaIdMatricula(matricula.getIdMatricula())
                    .stream()
                    .filter(FrequenciaModel::getPresente)
                    .count();

            Long totalFaltas = frequenciaRepository.findByMatriculaIdMatricula(matricula.getIdMatricula())
                    .stream()
                    .filter(frequencia -> !frequencia.getPresente())
                    .count();

            String nomeCurso = aluno.getCurso() != null
                    ? aluno.getCurso().getNomeCurso()
                    : "Sem curso";

            String nomeDisciplina = matricula.getDisciplina() != null
                    ? matricula.getDisciplina().getNomeDisciplina()
                    : "Sem disciplina";

            return new AlunoResumoDTO(
                    aluno.getIdPessoa(),
                    aluno.getNomePessoa(),
                    aluno.getRaAluno(),
                    nomeCurso,
                    nomeDisciplina,
                    mediaNotas,
                    totalPresencas,
                    totalFaltas
            );
        }).toList();
    }

    public AlunoModel buscarMeuCadastro(String username) {
        return buscarPorUsernameVinculado(username);
    }

    public List<AlunoResumoDTO> gerarMeuResumo(String username) {
        AlunoModel aluno = buscarPorUsernameVinculado(username);
        return gerarResumoAluno(aluno.getIdPessoa());
    }

    public List<DisciplinaModel> listarMinhasDisciplinas(String username) {
        AlunoModel aluno = buscarPorUsernameVinculado(username);

        return matriculaRepository.findByAlunoIdPessoa(aluno.getIdPessoa())
                .stream()
                .map(MatriculaModel::getDisciplina)
                .filter(disciplina -> disciplina != null)
                .collect(Collectors.toMap(
                        DisciplinaModel::getIdDisciplina,
                        disciplina -> disciplina,
                        (atual, ignorar) -> atual
                ))
                .values()
                .stream()
                .toList();
    }

    public CursoModel buscarMeuCurso(String username) {
        AlunoModel aluno = buscarPorUsernameVinculado(username);
        return aluno.getCurso();
    }
}
