package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "O nome da disciplina é obrigatório")
    private String nomeDisciplina;

    @Min(value = 1, message = "A carga horária deve ser maior que zero")
    private Integer cargaHoraria;

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