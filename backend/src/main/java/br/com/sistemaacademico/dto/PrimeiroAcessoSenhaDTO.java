package br.com.sistemaacademico.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrimeiroAcessoSenhaDTO {

    @NotBlank(message = "A nova senha é obrigatória")
    private String novaSenha;

    @NotBlank(message = "A confirmação da senha é obrigatória")
    private String confirmarSenha;
}
