package br.com.sistemaacademico.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FrequenciaRequestDTO {

    private LocalDate dataAula;

    @NotNull(message = "Informe se o aluno está presente ou ausente")
    private Boolean presente;

    @NotNull(message = "A matrícula é obrigatória")
    private Long idMatricula;
}