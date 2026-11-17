package br.com.starlog.dao;

import br.com.starlog.model.Produto;
import java.util.List;
import java.util.Optional;

/** Contrato de persistência do almoxarifado. */
public interface ProdutoDAO {
    List<Produto> listarTodos();
    Optional<Produto> buscarPorId(Long id);
    void salvar(Produto produto);

    /** Retorna true quando um registro foi efetivamente atualizado. */
    boolean atualizar(Produto produto);

    /** Retorna false quando o identificador não existe. */
    boolean excluir(Long id);
}
