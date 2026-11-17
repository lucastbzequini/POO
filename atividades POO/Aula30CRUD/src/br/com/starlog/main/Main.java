package br.com.starlog.main;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProdutoDAO estoque = new ProdutoDAOSqlite();
        Produto teclado = obterTeclado(estoque);
        Produto mousepad = new Produto("Mousepad Speed XL", 79.90, 8);
        estoque.salvar(mousepad);
        System.out.printf("CREATE - IDs: Teclado=%d, Mousepad=%d%n",
                teclado.getId(), mousepad.getId());
        System.out.println("\nREAD - ID | NOME | PREÇO | ESTOQUE");
        imprimirProdutos(estoque.listarTodos());
        ajustarTeclado(estoque, teclado);
        demonstrarExclusoes(estoque, mousepad.getId());
        System.out.println("\nESTADO FINAL");
        List<Produto> produtos = estoque.listarTodos();
        imprimirProdutos(produtos);
        System.out.println("Total de produtos: " + produtos.size());
    }

    private static Produto obterTeclado(ProdutoDAO estoque) {
        for (Produto produto : estoque.listarTodos()) {
            if (produto.getNome().equals("Teclado Mecânico RGB")) {
                return produto;
            }
        }
        Produto teclado = new Produto("Teclado Mecânico RGB", 319.90, 5);
        estoque.salvar(teclado);
        return teclado;
    }

    private static void imprimirProdutos(List<Produto> produtos) {
        for (Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    private static void ajustarTeclado(ProdutoDAO estoque, Produto teclado) {
        teclado.setEstoque(15);
        teclado.setPreco(289.90);
        System.out.println("\nUPDATE - Alteração confirmada: " + estoque.atualizar(teclado));
        estoque.buscarPorId(teclado.getId()).ifPresent(System.out::println);
    }

    private static void demonstrarExclusoes(ProdutoDAO estoque, Long idMousepad) {
        System.out.println("\nDELETE - Mousepad removido: " + estoque.excluir(idMousepad));
        long idLivre = encontrarIdLivre(estoque, 888L);
        boolean removido = estoque.excluir(idLivre);
        System.out.println("DELETE - ID inexistente " + idLivre + ": " + removido);
        if (!removido) {
            System.out.println("Registro não encontrado; nenhuma outra linha foi alterada.");
        }
    }

    private static long encontrarIdLivre(ProdutoDAO estoque, long inicio) {
        long candidato = inicio;
        for (; estoque.buscarPorId(candidato).isPresent(); candidato++) {
            // Consulta antes de excluir para preservar produtos que já existam.
        }
        return candidato;
    }
}
