package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.CursoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<CursoModel, Long> {

    List<CursoModel> findByNomeCursoContainingIgnoreCase(String nomeCurso);
}