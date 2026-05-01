package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlunoModel extends PessoaModel {

    @NotBlank(message = "O RA do aluno é obrigatório")
    private String raAluno;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private CursoModel curso;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno")
    private List<MatriculaModel> matriculas;
}