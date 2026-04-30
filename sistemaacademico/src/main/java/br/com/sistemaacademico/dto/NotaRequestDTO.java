package br.com.sistemaacademico.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaRequestDTO {

    @NotNull(message = "O valor da nota é obrigatório")
    @DecimalMin(value = "0.0", message = "A nota não pode ser menor que 0")
    @DecimalMax(value = "10.0", message = "A nota não pode ser maior que 10")
    private Double valorNota;

    @NotBlank(message = "O tipo da avaliação é obrigatório")
    private String tipoAvaliacao;

    @NotNull(message = "A matrícula é obrigatória")
    private Long idMatricula;
}