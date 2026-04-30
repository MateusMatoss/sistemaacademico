package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DisciplinaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDisciplina;

    private String nomeDisciplina;

    private Double notaDisciplina;

    private Integer cargaHoraria;

    private Integer presencaDisciplina;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private CursoModel curso;

    @ManyToOne
    @JoinColumn(name = "id_professor")
    private ProfessorModel professor;

    @JsonIgnore
    @OneToMany(mappedBy = "disciplina")
    private List<MatriculaModel> matriculas;
}