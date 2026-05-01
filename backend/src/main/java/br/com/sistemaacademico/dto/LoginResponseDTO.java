package br.com.sistemaacademico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String perfil;
    private Boolean senhaTemporaria;
}
