package br.com.sistemaacademico.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_acesso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogAcessoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    private String username;

    private String perfil;

    private LocalDateTime dataHoraLogin;
}