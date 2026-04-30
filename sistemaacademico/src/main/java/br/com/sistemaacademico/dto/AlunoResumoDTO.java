package br.com.sistemaacademico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlunoResumoDTO {

    private Long idAluno;
    private String nomeAluno;
    private String raAluno;
    private String curso;
    private String disciplina;
    private Double mediaNotas;
    private Long totalPresencas;
    private Long totalFaltas;
}