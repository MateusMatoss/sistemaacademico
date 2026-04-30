package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.DisciplinaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<DisciplinaModel, Long> {

    List<DisciplinaModel> findByCursoIdCurso(Long idCurso);

    List<DisciplinaModel> findByProfessorIdPessoa(Long idProfessor);

    Long countByCursoIdCurso(Long idCurso);

    List<DisciplinaModel> findByNomeDisciplinaContainingIgnoreCase(String nomeDisciplina);
}