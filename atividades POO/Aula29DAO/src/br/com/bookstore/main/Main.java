package br.com.bookstore.main;

import br.com.bookstore.dao.LivroDAO;
import br.com.bookstore.dao.LivroDAOSqlite;
import br.com.bookstore.model.Livro;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        LivroDAO catalogo = new LivroDAOSqlite();
        cadastrarExemplosSeVazio(catalogo);
        mostrarBusca(catalogo, 2L, "Livro 2 não encontrado.");
        mostrarBusca(catalogo, 999L,
                "Livro 999 não encontrado; ausência tratada com Optional.");
        System.out.println("\nID   | TÍTULO               | AUTOR                  | PREÇO");
        for (Livro livro : catalogo.listarTodos()) {
            System.out.println(livro);
        }
    }

    private static void cadastrarExemplosSeVazio(LivroDAO catalogo) {
        if (!catalogo.listarTodos().isEmpty()) {
            System.out.println("Banco existente: reutilizando as obras persistidas.");
            return;
        }
        Livro[] obras = {
            new Livro("Clean Code", "Robert C. Martin", 129.90),
            new Livro("Design Patterns", "Erich Gamma et al.", 159.90),
            new Livro("Refactoring", "Martin Fowler", 149.90)
        };
        for (Livro obra : obras) {
            catalogo.salvar(obra);
            System.out.println("Obra salva. ID gerado: " + obra.getId());
        }
    }

    private static void mostrarBusca(LivroDAO catalogo, Long id, String mensagemAusente) {
        System.out.println("\nBUSCA POR ID " + id + ":");
        Optional<Livro> resultado = catalogo.buscarPorId(id);
        if (resultado.isPresent()) {
            System.out.println(resultado.get());
        } else {
            System.out.println(mensagemAusente);
        }
    }
}
