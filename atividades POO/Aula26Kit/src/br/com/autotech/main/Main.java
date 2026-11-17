package br.com.autotech.main;

import br.com.autotech.dao.PecaDao;
import br.com.autotech.model.Peca;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PecaDao estoque = new PecaDao();
        try {
            estoque.criarTabelaSeNaoExistir();
        } catch (IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return;
        }
        if (args.length != 0 && args[0].equals("--demo")) {
            demonstrar(estoque);
        } else {
            abrirMenu(estoque);
        }
    }

    private static void abrirMenu(PecaDao estoque) {
        try (Scanner entrada = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                System.out.println("\nAUTOTECH - ESTOQUE LOCAL");
                System.out.println("1 Listar | 2 Cadastrar | 3 Buscar por código | 4 Dar baixa | 5 Semear | 0 Sair");
                System.out.print("Opção: ");
                if (!entrada.hasNextLine()) {
                    return;
                }
                String opcao = entrada.nextLine().trim();
                try {
                    switch (opcao) {
                        case "0" -> continuar = false;
                        case "1" -> listar(estoque);
                        case "2" -> cadastrar(estoque, entrada);
                        case "3" -> buscar(estoque, entrada);
                        case "4" -> retirar(estoque, entrada);
                        case "5" -> semear(estoque);
                        default -> System.out.println("Opção inválida.");
                    }
                } catch (IllegalArgumentException | IllegalStateException erro) {
                    System.out.println("Não foi possível concluir: " + erro.getMessage());
                }
            }
        }
    }

    private static void listar(PecaDao estoque) {
        List<Peca> pecas = estoque.listarTodas();
        if (pecas.isEmpty()) {
            System.out.println("Nenhuma peça cadastrada.");
        }
        for (Peca peca : pecas) {
            System.out.println(peca);
        }
    }

    private static void cadastrar(PecaDao estoque, Scanner entrada) {
        String codigo = ler(entrada, "Código: ").toUpperCase(Locale.ROOT);
        String nome = ler(entrada, "Nome: ");
        double preco = Double.parseDouble(ler(entrada, "Preço: ").replace(',', '.'));
        int quantidade = Integer.parseInt(ler(entrada, "Estoque: "));
        Peca nova = new Peca(codigo, nome, preco, quantidade);
        System.out.println("Cadastrada: " + estoque.salvar(nova));
    }

    private static void buscar(PecaDao estoque, Scanner entrada) {
        String codigo = ler(entrada, "Código: ").toUpperCase(Locale.ROOT);
        Peca peca = estoque.buscarPorCodigo(codigo);
        System.out.println(peca == null ? "Peça não encontrada." : peca);
    }

    private static void retirar(PecaDao estoque, Scanner entrada) {
        long id = Long.parseLong(ler(entrada, "ID: "));
        int quantidade = Integer.parseInt(ler(entrada, "Quantidade: "));
        boolean confirmado = estoque.darBaixaEstoque(id, quantidade);
        System.out.println(confirmado ? "Baixa confirmada." : "ID inexistente ou estoque insuficiente.");
    }

    private static String ler(Scanner entrada, String rotulo) {
        System.out.print(rotulo);
        if (entrada.hasNextLine()) {
            return entrada.nextLine().trim();
        }
        throw new IllegalArgumentException("Entrada encerrada.");
    }

    private static void semear(PecaDao estoque) {
        List<Peca> iniciais = List.of(
                new Peca("PAS-101", "Pastilha de Freio Dianteira Bosch", 145.50, 12),
                new Peca("FIL-202", "Filtro de Óleo Motor TecFil", 38.90, 30),
                new Peca("VEL-303", "Jogo 4 Velas de Ignição NGK", 112.00, 15));
        for (Peca peca : iniciais) {
            boolean existe = estoque.buscarPorCodigo(peca.getCodigo()) != null;
            if (!existe) {
                estoque.salvar(peca);
            }
        }
        System.out.println("Peças padrão disponíveis; códigos existentes foram preservados.");
    }

    private static void demonstrar(PecaDao estoque) {
        semear(estoque);
        Peca peca = estoque.buscarPorCodigo("PAS-101");
        System.out.println("Localizada: " + peca);
        System.out.println("Baixa de 2: " + estoque.darBaixaEstoque(peca.getId(), 2));
        System.out.println("Baixa acima do saldo: "
                + estoque.darBaixaEstoque(peca.getId(), Integer.MAX_VALUE));
        estoque.listarTodas().forEach(System.out::println);
    }
}
