package br.com.sistemaacademico.dto;

import br.com.sistemaacademico.model.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRegistroDTO {

    @NotBlank(message = "O usuário é obrigatório")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    private PerfilUsuario perfil;
}