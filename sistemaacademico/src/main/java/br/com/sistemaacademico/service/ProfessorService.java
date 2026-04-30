package br.com.sistemaacademico.service;

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

    public ProfessorModel salvar(ProfessorModel professor) {
        return professorRepository.save(professor);
    }

    public ProfessorModel atualizar(Long id, ProfessorModel professorAtualizado) {
        ProfessorModel professorExistente = buscarPorId(id);

        professorExistente.setNomePessoa(professorAtualizado.getNomePessoa());
        professorExistente.setCpfPessoa(professorAtualizado.getCpfPessoa());
        professorExistente.setEmailPessoa(professorAtualizado.getEmailPessoa());
        professorExistente.setFormacaoProfessor(professorAtualizado.getFormacaoProfessor());
        professorExistente.setTitulacaoProfessor(professorAtualizado.getTitulacaoProfessor());

        return professorRepository.save(professorExistente);
    }

    public void deletar(Long id) {
        ProfessorModel professor = buscarPorId(id);
        professorRepository.delete(professor);
    }
}