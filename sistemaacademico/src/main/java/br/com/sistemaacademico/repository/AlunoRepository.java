package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.AlunoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {

    List<AlunoModel> findByCursoIdCurso(Long idCurso);

    Long countByCursoIdCurso(Long idCurso);

    List<AlunoModel> findByNomePessoaContainingIgnoreCase(String nomePessoa);

    Optional<AlunoModel> findByCpfPessoa(String cpfPessoa);

    Optional<AlunoModel> findByRaAluno(String raAluno);
}