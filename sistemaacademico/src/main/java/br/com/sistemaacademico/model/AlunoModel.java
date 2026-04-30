package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private String raAluno;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private CursoModel curso;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno")
    private List<MatriculaModel> matriculas;
}