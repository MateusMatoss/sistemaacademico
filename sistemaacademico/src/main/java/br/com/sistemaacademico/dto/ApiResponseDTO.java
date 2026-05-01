package br.com.sistemaacademico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private LocalDateTime dataHora;
    private Integer status;
    private String mensagem;
    private T dados;
}