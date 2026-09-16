# Transaction Batch Processor

Um processador assíncrono de transações financeiras em lote (Batch) desenvolvido em Java Core, com foco em alta performance, concorrência segura e resiliência de dados.

Este projeto foi construído do zero com o objetivo de consolidar conceitos avançados de manipulação de fluxos de dados e paralelismo no ecossistema Java moderno antes de avançar para arquiteturas baseadas em Spring Boot (Spring Batch).

## Tecnologias e Conceitos Aplicados

- **Java 23 / Concorrência Avançada:** Utilização de ExecutorService com um pool fixo de threads (FixedThreadPool) para processamento paralelo e assíncrono de dados sobre a máquina virtual Java moderna.
- **Estruturas de Dados Thread-Safe:** Implementação de ConcurrentLinkedDeque para garantir a integridade da coleta de dados manipulada por múltiplas threads simultaneamente, evitando condições de corrida (Race Conditions).
- **Java I/O e NIO Avançado:** Leitura de streams de arquivos de forma eficiente através do Files.lines() e escrita otimizada com BufferedWriter para mitigar gargalos de operações em disco rígido.
- **Gerenciamento Seguro de Recursos:** Utilização da estrutura try-with-resources para garantir o fechamento automatizado de canais de dados (Streams), eliminando riscos de vazamento de memória (Memory Leaks).
- **Resiliência a Falhas:** Arquitetura desenhada para isolar exceções de conversão de dados (NumberFormatException) no nível da tarefa. Ocorrências de erros em linhas inválidas do arquivo geram logs de alerta, mas não interrompem o processamento das demais transações do lote.

## Cenário de Negócio e Fluxo de Execução

O sistema simula a recepção de uma carga de dados financeiros no arquivo transacoes.txt utilizando o formato padrão ID_CLIENTE;TIPO_OPERACAO;VALOR.

1. O sistema mapeia o arquivo texto e distribui a carga de trabalho dinamicamente entre as threads configuradas.
2. Cada transação válida com valor estritamente superior a R\$ 100,00 é capturada e carimbada com o nome da thread responsável por sua execução.
3. Linhas com dados corrompidos ou formatos inválidos são tratadas de forma isolada, garantindo que a execução global permaneça estável.
4. Após a conclusão assíncrona de todas as tarefas de processamento, a thread principal (main) é liberada para consolidar as informações em memória e exportar o arquivo final relatorio_transacoes.txt.

## Como Executar o Projeto

1. Certifique-se de ter o JDK 23 instalado e configurado em seu ambiente de desenvolvimento.
2. Crie o arquivo transacoes.txt na raiz do projeto contendo a massa de dados conforme o exemplo abaixo:
   ```text
   101;DEPOSITO;500.00
   102;SAQUE;abc
   103;SAQUE;1200.50
   104;DEPOSITO;80.00
   105;DEPOSITO;150.75
   ```
3. Execute a classe principal Main.java através da sua IDE ou por linha de comando.
4. Confira as saídas de logs no console e verifique o arquivo gerado relatorio_transacoes.txt na pasta raiz.

---
Desenvolvido por Matheus Baia 
