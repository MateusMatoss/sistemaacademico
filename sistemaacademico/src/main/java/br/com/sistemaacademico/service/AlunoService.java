package br.com.sistemaacademico.service;

import br.com.sistemaacademico.model.AlunoModel;
import br.com.sistemaacademico.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public List<AlunoModel> listarTodos() {
        return alunoRepository.findAll();
    }

    public AlunoModel buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    public AlunoModel salvar(AlunoModel aluno) {
        return alunoRepository.save(aluno);
    }

    public AlunoModel atualizar(Long id, AlunoModel alunoAtualizado) {
        AlunoModel alunoExistente = buscarPorId(id);

        alunoExistente.setNomePessoa(alunoAtualizado.getNomePessoa());
        alunoExistente.setCpfPessoa(alunoAtualizado.getCpfPessoa());
        alunoExistente.setEmailPessoa(alunoAtualizado.getEmailPessoa());
        alunoExistente.setRaAluno(alunoAtualizado.getRaAluno());

        return alunoRepository.save(alunoExistente);
    }

    public void deletar(Long id) {
        AlunoModel aluno = buscarPorId(id);
        alunoRepository.delete(aluno);
    }
}