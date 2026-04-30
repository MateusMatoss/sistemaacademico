package br.com.sistemaacademico.service;

import br.com.sistemaacademico.dto.ProfessorRequestDTO;
import br.com.sistemaacademico.exception.ResourceNotFoundException;
import br.com.sistemaacademico.model.ProfessorModel;
import br.com.sistemaacademico.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
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

    public void deletar(Long id) {
        ProfessorModel professor = buscarPorId(id);
        professorRepository.delete(professor);
    }
}