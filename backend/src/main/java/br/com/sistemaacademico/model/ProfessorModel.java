package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "A formação do professor é obrigatória")
    private String formacaoProfessor;

    @NotBlank(message = "A titulação do professor é obrigatória")
    private String titulacaoProfessor;

    @JsonIgnore
    @OneToMany(mappedBy = "professor")
    private List<DisciplinaModel> disciplinas;
}