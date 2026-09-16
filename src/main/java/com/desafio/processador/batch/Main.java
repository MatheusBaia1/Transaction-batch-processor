package com.desafio.processador.batch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Main {
    private static ConcurrentLinkedDeque<String> relatorio = new ConcurrentLinkedDeque<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try (Stream<String> linhas = Files.lines(Paths.get("transacoes.txt"))) {
            linhas.forEach(linha -> executor.execute(() -> processarLinha(linha)));
        } catch (IOException e) {
            System.err.println("Erro crítico: Não foi possível ler o arquivo transacoes.txt");
        }
        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("relatorio_transacoes.txt"))) {
            for (String linhaRelatorio : relatorio) {
                writer.write(linhaRelatorio);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processarLinha(String linha) {
        String[] partes = linha.split(";");
        try {
            double valor = Double.parseDouble(partes[2]);
            if (valor > 100.00) {
                String textoFormatado = partes[0] + ";" + partes[2] + ";" + Thread.currentThread().getName();
                relatorio.add(textoFormatado);
            }
        } catch (NumberFormatException e) {
            System.err.println(" Erro de conversão na linha: " + linha);
        }
    }
}
