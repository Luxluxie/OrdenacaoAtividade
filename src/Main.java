import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

public class Main {

    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int chave = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > chave) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = chave;
        }
    }

    // QUICK SORT ITERATIVO (sem recursão) - Evita StackOverflowError
    public static void quickSortIterativo(int[] arr) {
        Stack<int[]> pilha = new Stack<>();
        pilha.push(new int[]{0, arr.length - 1});

        while (!pilha.isEmpty()) {
            int[] intervalo = pilha.pop();
            int inicio = intervalo[0];
            int fim = intervalo[1];

            if (inicio < fim) {
                int pivo = particionar(arr, inicio, fim);

                // Empilha primeiro a partição maior para otimizar (menos uso de memória)
                if (pivo - inicio > fim - pivo) {
                    pilha.push(new int[]{inicio, pivo - 1});
                    pilha.push(new int[]{pivo + 1, fim});
                } else {
                    pilha.push(new int[]{pivo + 1, fim});
                    pilha.push(new int[]{inicio, pivo - 1});
                }
            }
        }
    }

    private static int particionar(int[] arr, int inicio, int fim) {
        // Escolhe o pivô como o elemento do meio (evita pior caso em arrays ordenados)
        int meio = inicio + (fim - inicio) / 2;
        int pivo = arr[meio];

        // Move o pivô para o final
        int temp = arr[meio];
        arr[meio] = arr[fim];
        arr[fim] = temp;

        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (arr[j] <= pivo) {
                i++;
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Coloca o pivô na posição correta
        temp = arr[i + 1];
        arr[i + 1] = arr[fim];
        arr[fim] = temp;

        return i + 1;
    }

    // VERSÃO RECURSIVA (pode causar StackOverflow para 1M elementos)
    // Mantida apenas para referência, mas NÃO USE para 1M
    public static void quickSortRecursivo(int[] arr, int inicio, int fim) {
        if (inicio < fim) {
            int indicePivo = particionar(arr, inicio, fim);
            quickSortRecursivo(arr, inicio, indicePivo - 1);
            quickSortRecursivo(arr, indicePivo + 1, fim);
        }
    }

    private static int[] carregarDados(String nomeArquivo) throws FileNotFoundException {
        File file = new File(nomeArquivo);
        Scanner scanner = new Scanner(file);
        ArrayList<Integer> lista = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine().trim();
            if (linha.isEmpty()) continue;

            try {
                lista.add(Integer.parseInt(linha));
            } catch (NumberFormatException e) {
                // Ignora cabeçalho
            }
        }
        scanner.close();

        int[] resultado = new int[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            resultado[i] = lista.get(i);
        }
        return resultado;
    }

    private static void exibirPrimeiros15(int[] array) {
        int limite = Math.min(array.length, 15);
        for (int i = 0; i < limite; i++) {
            System.out.print(array[i] + " ");
        }
        if (array.length > 15) System.out.print("...");
        System.out.println();
    }

    public static void main(String[] args) {
        // Testa cada arquivo individualmente
        String[][] arquivos = {
                {"Data\\dados_melhor_1k.csv", "Melhor Caso", "1.000"},
                {"Data\\dados_melhor_1M.csv", "Melhor Caso", "1.000.000"},
                {"Data\\dados_pior_1k.csv", "Pior Caso", "1.000"},
                {"Data\\dados_pior_1M.csv", "Pior Caso", "1.000.000"},
                {"Data\\dados_medio_1k.csv", "Caso Médio", "1.000"},
                {"Data\\dados_medio_1M.csv", "Caso Médio", "1.000.000"}
        };

        System.out.println("=".repeat(80));
        System.out.println("RELATÓRIO DE PERFORMANCE - ORDENAÇÃO");
        System.out.println("Insertion Sort (O(n²)) vs Quick Sort Iterativo (O(n log n))");
        System.out.println("=".repeat(80));
        System.out.printf("%-20s %-12s %-25s %-25s\n", "CASO", "TAMANHO", "INSERTION SORT (ms)", "QUICK SORT (ms)");
        System.out.println("-".repeat(80));

        for (String[] arquivo : arquivos) {
            String nomeArquivo = arquivo[0];
            String caso = arquivo[1];
            String tamanho = arquivo[2];

            try {
                System.out.printf("%-20s %-12s", caso, tamanho);

                int[] dados = carregarDados(nomeArquivo);

                // Testa Insertion Sort
                int[] arrInsertion = new int[dados.length];
                for (int i = 0; i < dados.length; i++) arrInsertion[i] = dados[i];

                long startInsertion = System.nanoTime();
                insertionSort(arrInsertion);
                long endInsertion = System.nanoTime();
                double tempoInsertion = (endInsertion - startInsertion) / 1_000_000.0;

                // Testa Quick Sort Iterativo
                int[] arrQuick = new int[dados.length];
                for (int i = 0; i < dados.length; i++) arrQuick[i] = dados[i];

                long startQuick = System.nanoTime();
                quickSortIterativo(arrQuick);
                long endQuick = System.nanoTime();
                double tempoQuick = (endQuick - startQuick) / 1_000_000.0;

                System.out.printf(" %-23.2f %-25.2f\n", tempoInsertion, tempoQuick);

            } catch (FileNotFoundException e) {
                System.out.printf(" %-23s %-25s\n", "ARQUIVO NÃO ENCONTRADO", "-");
                System.out.println("Erro: " + nomeArquivo);
            }
        }

        System.out.println("=".repeat(80));
        System.out.println("\nObservações:");
        System.out.println("- Quick Sort iterativo evita StackOverflowError para grandes entradas");
        System.out.println("- Melhor caso do Insertion Sort é O(n), por isso foi muito rápido (6ms para 1M)");
        System.out.println("- Quick Sort com pivô mediano tem melhor desempenho geral");
    }
}