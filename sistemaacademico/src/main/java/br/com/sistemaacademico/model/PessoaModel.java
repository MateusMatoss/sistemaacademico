package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "pessoas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public class PessoaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPessoa;

    @NotBlank(message = "O nome é obrigatório")
    private String nomePessoa;

    @NotBlank(message = "O CPF é obrigatório")
    @Column(unique = true)
    private String cpfPessoa;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    private String emailPessoa;
}