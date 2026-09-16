package com.desafio.processador.batch;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessadorTransacoes {
    private AtomicInteger totalProcessadas = new AtomicInteger(0);
    private AtomicInteger totalComErro = new AtomicInteger(0);
    private ConcurrentLinkedDeque<Transacao> relatorio = new ConcurrentLinkedDeque<>();

    public void processarLinha(String linha) {
        try {
            String[] partes = linha.split(";");
            double valor = Double.parseDouble(partes[2]);
            totalProcessadas.incrementAndGet();
            if (valor > 100.00) {
                Transacao transacao = new Transacao(partes[0], partes[1], valor);
                relatorio.add(transacao);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            totalComErro.incrementAndGet();
            System.err.println("Erro ao processar linha: " + linha);
        }
    }

    public ConcurrentLinkedDeque<Transacao> getRelatorio() {
        return relatorio;
    }

    public int getTotalProcessadas() {
        return totalProcessadas.get();
    }

    public int getTotalComErro() {
        return totalComErro.get();
    }
}