package com.desafio.processador.batch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ProcessadorTransacoes processador = new ProcessadorTransacoes();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try (Stream<String> linhas = Files.lines(Paths.get("transacoes.txt"))) {
            linhas.forEach(linha -> executor.execute(() -> processador.processarLinha(linha)));
        } catch (IOException e) {
            System.err.println("Erro crítico: Não foi possível ler o arquivo transacoes.txt");
        }
        executor.shutdown();
        boolean concluido = executor.awaitTermination(1, TimeUnit.MINUTES);
        if (!concluido) {
            System.err.println("Atenção: nem todas as transações foram processadas a tempo.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("relatorio_transacoes.txt"))) {
            for (Transacao linhaRelatorio : processador.getRelatorio()) {
                writer.write(linhaRelatorio.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Total processadas: " + processador.getTotalProcessadas());
        System.out.println("Total com erro: " + processador.getTotalComErro());
    }

}
