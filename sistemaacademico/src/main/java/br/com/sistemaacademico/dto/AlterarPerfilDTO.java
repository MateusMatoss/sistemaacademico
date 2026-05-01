package br.com.sistemaacademico.dto;

import br.com.sistemaacademico.model.PerfilUsuario;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlterarPerfilDTO {

    @NotNull(message = "O perfil é obrigatório")
    private PerfilUsuario perfil;
}