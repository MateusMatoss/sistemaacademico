package br.com.sistemaacademico.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.sistemaacademico.dto.ProfessorRequestDTO;
import br.com.sistemaacademico.dto.ProfessorResumoDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.DisciplinaModel;
import br.com.sistemaacademico.model.ProfessorModel;
import br.com.sistemaacademico.repository.DisciplinaRepository;
import br.com.sistemaacademico.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;

    public ProfessorService(
            ProfessorRepository professorRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.professorRepository = professorRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<ProfessorModel> listarTodos() {
        return professorRepository.findAll();
    }

    public ProfessorModel buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado"));
    }

    public ProfessorModel salvar(ProfessorRequestDTO dto) {
        ProfessorModel professor = new ProfessorModel();

        professor.setNomePessoa(dto.getNomePessoa());
        professor.setCpfPessoa(dto.getCpfPessoa());
        professor.setEmailPessoa(dto.getEmailPessoa());
        professor.setFormacaoProfessor(dto.getFormacaoProfessor());
        professor.setTitulacaoProfessor(dto.getTitulacaoProfessor());

        return professorRepository.save(professor);
    }

    public ProfessorModel atualizar(Long id, ProfessorRequestDTO dto) {
        ProfessorModel professorExistente = buscarPorId(id);

        professorExistente.setNomePessoa(dto.getNomePessoa());
        professorExistente.setCpfPessoa(dto.getCpfPessoa());
        professorExistente.setEmailPessoa(dto.getEmailPessoa());
        professorExistente.setFormacaoProfessor(dto.getFormacaoProfessor());
        professorExistente.setTitulacaoProfessor(dto.getTitulacaoProfessor());

        return professorRepository.save(professorExistente);
    }

    public Page<ProfessorModel> listarPaginado(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    public List<ProfessorModel> buscarPorNome(String nome) {
        return professorRepository.findByNomePessoaContainingIgnoreCase(nome);
    }

    public void deletar(Long id) {
        ProfessorModel professor = buscarPorId(id);
        professorRepository.delete(professor);
    }

    public List<ProfessorResumoDTO> gerarResumoProfessor(Long idProfessor) {
        ProfessorModel professor = buscarPorId(idProfessor);

        List<DisciplinaModel> disciplinas = disciplinaRepository.findByProfessorIdPessoa(idProfessor);

        return disciplinas.stream().map(disciplina -> {
            String nomeCurso = disciplina.getCurso() != null
                    ? disciplina.getCurso().getNomeCurso()
                    : "Sem curso";

            return new ProfessorResumoDTO(
                    professor.getIdPessoa(),
                    professor.getNomePessoa(),
                    professor.getFormacaoProfessor(),
                    professor.getTitulacaoProfessor(),
                    disciplina.getNomeDisciplina(),
                    nomeCurso,
                    disciplina.getCargaHoraria()
            );
        }).toList();
    }
}