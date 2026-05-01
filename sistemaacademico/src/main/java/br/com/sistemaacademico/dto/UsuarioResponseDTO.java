package br.com.sistemaacademico.dto;

import br.com.sistemaacademico.model.PerfilUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long idUsuario;
    private String username;
    private PerfilUsuario perfil;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}