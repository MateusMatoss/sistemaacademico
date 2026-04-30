package br.com.sistemaacademico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfessorResumoDTO {

    private Long idProfessor;
    private String nomeProfessor;
    private String formacaoProfessor;
    private String titulacaoProfessor;
    private String disciplina;
    private String curso;
    private Integer cargaHoraria;
}