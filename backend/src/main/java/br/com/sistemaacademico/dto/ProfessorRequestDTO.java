package br.com.sistemaacademico.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nomePessoa;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpfPessoa;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String emailPessoa;

    @NotBlank(message = "A formação do professor é obrigatória")
    private String formacaoProfessor;

    @NotBlank(message = "A titulação do professor é obrigatória")
    private String titulacaoProfessor;
}