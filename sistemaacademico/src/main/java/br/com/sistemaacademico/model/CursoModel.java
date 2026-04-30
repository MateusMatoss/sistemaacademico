package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CursoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @NotBlank(message = "O nome do curso é obrigatório")
    private String nomeCurso;

    private String descricaoCurso;

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private List<AlunoModel> alunos;

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private List<DisciplinaModel> disciplinas;
}