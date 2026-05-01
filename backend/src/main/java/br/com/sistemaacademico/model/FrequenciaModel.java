package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "frequencias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FrequenciaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFrequencia;

    private LocalDate dataAula;

    @NotNull(message = "Informe se o aluno está presente ou ausente")
    private Boolean presente;

    @ManyToOne
    @JoinColumn(name = "id_matricula")
    private MatriculaModel matricula;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}