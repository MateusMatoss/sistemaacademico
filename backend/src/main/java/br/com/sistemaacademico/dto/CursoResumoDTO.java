package br.com.sistemaacademico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CursoResumoDTO {

    private Long idCurso;
    private String nomeCurso;
    private String descricaoCurso;
    private Long totalAlunos;
    private Long totalDisciplinas;
}