package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.ProfessorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {

    List<ProfessorModel> findByNomePessoaContainingIgnoreCase(String nomePessoa);
}