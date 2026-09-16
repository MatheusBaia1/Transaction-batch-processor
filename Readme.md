# Transaction Batch Processor

Exercício de concorrência em Java puro: processa um arquivo de transações em paralelo, usando um pool de threads, com tratamento de erro isolado por linha e um relatório final.

## Objetivo

Praticar `ExecutorService`, estruturas de dados thread-safe e I/O de arquivos antes de estudar Spring Batch. Não é uma ferramenta de produção — é um projeto de estudo, testado com casos de linha malformada para confirmar o tratamento de erro.

## O que faz

1. Lê `transacoes.txt`, no formato `ID_CLIENTE;TIPO_OPERACAO;VALOR`.
2. Distribui cada linha entre um pool fixo de threads (`ExecutorService`).
3. Transações com valor maior que R$ 100,00 entram no relatório final.
4. Linhas malformadas (campo faltando, valor não numérico) são registradas como erro no console, sem interromper o processamento das demais.
5. Ao final, escreve `relatorio_transacoes.txt` e imprime um resumo (total processado, total com erro).

## Tecnologias e conceitos

- `ExecutorService` com pool fixo de threads (`Executors.newFixedThreadPool`)
- `ConcurrentLinkedDeque` para acumular resultados de forma thread-safe, sem `synchronized` manual
- `AtomicInteger` para contadores incrementados por múltiplas threads
- `Files.lines()` + `try-with-resources` para leitura de arquivo
- `BufferedWriter` para escrita do relatório
- Tratamento de exceção isolado por tarefa (`NumberFormatException`, `ArrayIndexOutOfBoundsException`), sem derrubar o processamento das demais linhas

## Estrutura

```
Transacao.java              → modelo de dados (id, tipo de operação, valor)
ProcessadorTransacoes.java  → lógica de parsing e acumulação do relatório
Main.java                   → orquestra: lê o arquivo, distribui para as threads, escreve o relatório
```

## Como rodar

1. JDK 17+ instalado.
2. Crie um arquivo `transacoes.txt` na raiz do projeto:
```
101;DEPOSITO;500.00
102;SAQUE;abc
103;SAQUE;1200.50
104;DEPOSITO;80.00
105;DEPOSITO;150.75
```
3. Execute `Main.java` pela sua IDE.
4. Confira `relatorio_transacoes.txt` gerado na raiz, e o resumo impresso no console.

## Limitações conhecidas / próximos passos

- Não mede performance real (sequencial vs paralelo) — é um próximo passo natural para validar o ganho de concorrência de fato.
- Ignora o campo `TIPO_OPERACAO` na regra de negócio (lê, mas não diferencia saque de depósito).
- Nome do arquivo e tamanho do pool de threads estão fixos no código, não configuráveis via argumento.