package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.MatriculaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {

    List<MatriculaModel> findByAlunoIdPessoa(Long idAluno);

    List<MatriculaModel> findByDisciplinaIdDisciplina(Long idDisciplina);
}
