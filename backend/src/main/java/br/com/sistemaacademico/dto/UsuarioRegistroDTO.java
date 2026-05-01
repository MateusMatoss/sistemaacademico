package br.com.sistemaacademico.dto;

import br.com.sistemaacademico.model.PerfilUsuario;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRegistroDTO {

    private String username;

    @jakarta.validation.constraints.NotBlank(message = "A senha é obrigatória")
    private String password;

    private PerfilUsuario perfil;

    private String nomePessoa;

    private String cpfPessoa;

    @Email(message = "Informe um e-mail válido")
    private String emailPessoa;

    private Long idCurso;
}
