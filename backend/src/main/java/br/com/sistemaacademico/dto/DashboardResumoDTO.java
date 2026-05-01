package br.com.sistemaacademico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardResumoDTO {

    private Long totalAlunos;
    private Long totalProfessores;
    private Long totalCursos;
    private Long totalDisciplinas;
    private Long totalMatriculas;
    private Long totalNotas;
    private Long totalFrequencias;
}