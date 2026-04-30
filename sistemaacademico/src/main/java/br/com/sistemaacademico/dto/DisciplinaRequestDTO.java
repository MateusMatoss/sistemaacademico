package br.com.sistemaacademico.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisciplinaRequestDTO {

    @NotBlank(message = "O nome da disciplina é obrigatório")
    private String nomeDisciplina;

    @Min(value = 1, message = "A carga horária deve ser maior que zero")
    private Integer cargaHoraria;

    private Long idCurso;

    private Long idProfessor;
}