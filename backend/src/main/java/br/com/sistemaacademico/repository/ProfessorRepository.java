package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.ProfessorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {

    List<ProfessorModel> findByNomePessoaContainingIgnoreCase(String nomePessoa);

    Optional<ProfessorModel> findByCpfPessoa(String cpfPessoa);
}