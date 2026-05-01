package br.com.sistemaacademico.dto;

import br.com.sistemaacademico.model.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioAtualizacaoDTO {

    @NotBlank(message = "O username é obrigatório")
    private String username;

    private String password;

    private PerfilUsuario perfil;
}