package br.com.starlog.dao;

import br.com.starlog.factory.ConexaoFactory;
import br.com.starlog.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOSqlite implements ProdutoDAO {
    private static final String SELECAO = "SELECT id, nome, preco, estoque FROM produtos";

    public ProdutoDAOSqlite() {
        inicializarTabela();
    }

    private void inicializarTabela() {
        String estrutura = "CREATE TABLE IF NOT EXISTS produtos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, "
                + "preco REAL NOT NULL CHECK (preco > 0), "
                + "estoque INTEGER NOT NULL CHECK (estoque >= 0));";
        try (Connection conexao = ConexaoFactory.obterConexao();
             Statement comando = conexao.createStatement()) {
            comando.execute(estrutura);
        } catch (SQLException causa) {
            throw falha("Erro ao criar tabela de produtos: ", causa);
        }
    }

    @Override
    public void salvar(Produto produto) {
        String insercao = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?);";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(insercao, Statement.RETURN_GENERATED_KEYS)) {
            preencherProduto(comando, produto);
            comando.executeUpdate();
            produto.setId(idGerado(comando));
        } catch (SQLException causa) {
            throw falha("Erro ao salvar produto: ", causa);
        }
    }

    private void preencherProduto(PreparedStatement comando, Produto produto) throws SQLException {
        comando.setString(1, produto.getNome());
        comando.setDouble(2, produto.getPreco());
        comando.setInt(3, produto.getEstoque());
    }

    private long idGerado(PreparedStatement comando) throws SQLException {
        try (ResultSet resultado = comando.getGeneratedKeys()) {
            if (resultado.next()) {
                return resultado.getLong(1);
            }
            throw new SQLException("Nenhuma chave gerada.");
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        String sql = SELECAO + " WHERE id = ?;";
        List<Produto> encontrados = consultar(sql, id, true, "Erro ao buscar produto: ");
        return encontrados.isEmpty() ? Optional.empty() : Optional.of(encontrados.get(0));
    }

    @Override
    public List<Produto> listarTodos() {
        return consultar(SELECAO + " ORDER BY nome ASC;", null, false, "Erro ao listar catálogo: ");
    }

    private List<Produto> consultar(String sql, Long id, boolean filtrarId, String erro) {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement consulta = conexao.prepareStatement(sql)) {
            if (filtrarId) {
                consulta.setLong(1, id);
            }
            List<Produto> encontrados = new ArrayList<>();
            try (ResultSet linha = consulta.executeQuery()) {
                while (linha.next()) {
                    encontrados.add(new Produto(linha.getLong("id"), linha.getString("nome"),
                            linha.getDouble("preco"), linha.getInt("estoque")));
                }
            }
            return encontrados;
        } catch (SQLException causa) {
            throw falha(erro, causa);
        }
    }

    @Override
    public boolean atualizar(Produto produto) {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("Produto sem ID para atualização.");
        }
        String alteracao = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE id = ?;";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(alteracao)) {
            preencherProduto(comando, produto);
            comando.setLong(4, produto.getId());
            return comando.executeUpdate() > 0;
        } catch (SQLException causa) {
            throw falha("Erro ao atualizar produto: ", causa);
        }
    }

    @Override
    public boolean excluir(Long id) {
        String remocao = "DELETE FROM produtos WHERE id = ?;";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(remocao)) {
            comando.setLong(1, id);
            return comando.executeUpdate() > 0;
        } catch (SQLException causa) {
            throw falha("Erro ao excluir produto: ", causa);
        }
    }

    private IllegalStateException falha(String contexto, SQLException causa) {
        return new IllegalStateException(contexto + causa.getMessage(), causa);
    }
}
