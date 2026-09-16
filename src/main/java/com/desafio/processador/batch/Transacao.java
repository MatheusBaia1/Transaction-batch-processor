package com.desafio.processador.batch;

public class Transacao {
    private String idCliente;
    private String tipoOperacao;
    private double valor;

    @Override
    public String toString() {
        return idCliente + ";" + tipoOperacao + ";" + valor;
    }

    public Transacao(String idCliente, String tipoOperacao, double valor) {
        this.idCliente = idCliente;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public double getValor() {
        return valor;
    }
}