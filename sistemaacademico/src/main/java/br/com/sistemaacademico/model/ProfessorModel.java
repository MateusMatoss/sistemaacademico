package br.com.sistemaacademico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "professores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfessorModel extends PessoaModel {

    private String formacaoProfessor;

    private String titulacaoProfessor;
}