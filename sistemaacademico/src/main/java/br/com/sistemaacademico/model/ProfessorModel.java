package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "professor")
    private List<DisciplinaModel> disciplinas;
}