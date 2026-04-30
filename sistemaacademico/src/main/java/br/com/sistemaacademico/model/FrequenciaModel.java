package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private Boolean presente;

    @ManyToOne
    @JoinColumn(name = "id_matricula")
    private MatriculaModel matricula;
}