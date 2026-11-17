package br.com.bookstore.dao;

import br.com.bookstore.model.Livro;
import java.util.List;
import java.util.Optional;

/** Operações disponíveis para o catálogo, independentemente do banco utilizado. */
public interface LivroDAO {
    List<Livro> listarTodos();
    Optional<Livro> buscarPorId(Long id);

    /** Persiste uma obra e atribui a ela o identificador gerado. */
    void salvar(Livro livro);
}
