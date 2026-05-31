import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Altere o nome do arquivo conforme o cenário que deseja testar
        String nomeArquivo = "Data\\dados_medio_1k.csv";

        try {
            int[] dados = carregarDados(nomeArquivo);

            // Criando cópias para testar os dois algoritmos com os mesmos dados
            int[] arrayParaSimples = Arrays.copyOf(dados, dados.length);
            int[] arrayParaEficiente = Arrays.copyOf(dados, dados.length);

            // --- TESTE ALGORITMO SIMPLES ---
            System.out.println("--- Executando Algoritmo Simples ---");
            exibirPrimeiros15(arrayParaSimples);
            long inicioSimples = System.currentTimeMillis();

            // TODO: Chame aqui o método do seu algoritmo (ex: bubbleSort(arrayParaSimples);)

            long fimSimples = System.currentTimeMillis();
            exibirPrimeiros15(arrayParaSimples);
            System.out.println("Tempo decorrido: " + (fimSimples - inicioSimples) + " ms\n");

            // --- TESTE ALGORITMO EFICIENTE ---
            System.out.println("--- Executando Algoritmo Eficiente ---");
            exibirPrimeiros15(arrayParaEficiente);
            long inicioEficiente = System.currentTimeMillis();

            // TODO: Chame aqui o método do seu algoritmo (ex: quickSort(arrayParaEficiente, 0, n-1);)

            long fimEficiente = System.currentTimeMillis();
            exibirPrimeiros15(arrayParaEficiente);
            System.out.println("Tempo decorrido: " + (fimEficiente - inicioEficiente) + " ms");

        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo não encontrado. Verifique o nome e a localização.");
        }
    }

    // Método para ler o CSV e retornar o array
    private static int[] carregarDados(String nomeArquivo) throws FileNotFoundException {
        File file = new File(nomeArquivo);
        Scanner scanner = new Scanner(file);

        // Pula o cabeçalho (assumindo que o arquivo tem uma linha de título)
        if (scanner.hasNextLine()) scanner.nextLine();

        // Não sabemos o tamanho exato de antemão de forma dinâmica facilmente,
        // mas sabemos que são 1.000 ou 1.000.000, podemos instanciar fixo ou contar linhas:
        // Aqui assumimos que sabemos o tamanho para simplificar para o aluno:
        int tamanho = nomeArquivo.contains("1M") ? 1000000 : 1000;
        int[] array = new int[tamanho];

        int i = 0;
        while (scanner.hasNextLine() && i < tamanho) {
            array[i] = Integer.parseInt(scanner.nextLine().trim());
            i++;
        }
        scanner.close();
        return array;
    }

    // Método para exibir os 15 primeiros elementos
    private static void exibirPrimeiros15(int[] array) {
        System.out.print("Dados: ");
        int limite = Math.min(array.length, 15);
        for (int i = 0; i < limite; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("...");
    }
}
