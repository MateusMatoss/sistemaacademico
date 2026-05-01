package br.com.sistemaacademico.exception;

import java.time.LocalDateTime;

public class ErroResponse {

    private LocalDateTime dataHora;
    private Integer status;
    private String erro;
    private String mensagem;
    private String caminho;

    public ErroResponse(LocalDateTime dataHora, Integer status, String erro, String mensagem, String caminho) {
        this.dataHora = dataHora;
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.caminho = caminho;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Integer getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getCaminho() {
        return caminho;
    }
}