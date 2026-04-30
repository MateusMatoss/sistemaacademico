package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MatriculaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMatricula;

    private LocalDate dataMatricula;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private AlunoModel aluno;

    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private DisciplinaModel disciplina;
}