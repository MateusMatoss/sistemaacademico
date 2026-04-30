package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import lombok.*;

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
}