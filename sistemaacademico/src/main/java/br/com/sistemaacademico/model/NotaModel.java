package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "O valor da nota é obrigatório")
    @DecimalMin(value = "0.0", message = "A nota não pode ser menor que 0")
    @DecimalMax(value = "10.0", message = "A nota não pode ser maior que 10")
    private Double valorNota;

    @NotBlank(message = "O tipo da avaliação é obrigatório")
    private String tipoAvaliacao;

    @ManyToOne
    @JoinColumn(name = "id_matricula")
    private MatriculaModel matricula;
}