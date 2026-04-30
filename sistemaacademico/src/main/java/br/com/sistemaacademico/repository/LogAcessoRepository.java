package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.LogAcessoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogAcessoRepository extends JpaRepository<LogAcessoModel, Long> {

    List<LogAcessoModel> findByUsername(String username);
}