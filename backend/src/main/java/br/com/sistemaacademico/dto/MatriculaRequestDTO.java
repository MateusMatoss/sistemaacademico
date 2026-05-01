package br.com.sistemaacademico.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MatriculaRequestDTO {

    private LocalDate dataMatricula;

    @NotNull(message = "O aluno é obrigatório")
    private Long idAluno;

    @NotNull(message = "A disciplina é obrigatória")
    private Long idDisciplina;
}