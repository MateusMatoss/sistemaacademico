package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class NotaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNota;

    private Double valorNota;

    private String tipoAvaliacao;

    @ManyToOne
    @JoinColumn(name = "id_matricula")
    private MatriculaModel matricula;
}