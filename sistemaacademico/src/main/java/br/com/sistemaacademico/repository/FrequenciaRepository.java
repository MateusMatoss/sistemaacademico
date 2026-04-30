package br.com.sistemaacademico.repository;

import br.com.sistemaacademico.model.FrequenciaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequenciaRepository extends JpaRepository<FrequenciaModel, Long> {
}