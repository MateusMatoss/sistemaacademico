package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.NotaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaRepository extends JpaRepository<NotaModel, Long> {
}