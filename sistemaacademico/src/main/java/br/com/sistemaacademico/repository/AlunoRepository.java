package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.AlunoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, Long> {

    List<AlunoModel> findByCursoIdCurso(Long idCurso);

    Long countByCursoIdCurso(Long idCurso);

    List<AlunoModel> findByNomePessoaContainingIgnoreCase(String nomePessoa);
}