package br.com.sistemaacademico.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "matricula")
    private List<NotaModel> notas;

    @JsonIgnore
    @OneToMany(mappedBy = "matricula")
    private List<FrequenciaModel> frequencias;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}