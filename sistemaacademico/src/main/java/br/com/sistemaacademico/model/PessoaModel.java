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

    @NotBlank(message = "Nome é obrigatório")
    private String nomePessoa;

    @Column(unique = true)
    private String cpfPessoa;

    @Email(message = "Email inválido")
    private String emailPessoa;
}