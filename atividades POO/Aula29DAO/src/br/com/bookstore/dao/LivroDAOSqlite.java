package br.com.bookstore.dao;

import br.com.bookstore.factory.ConexaoFactory;
import br.com.bookstore.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroDAOSqlite implements LivroDAO {
    private static final String CAMPOS = "id, titulo, autor, preco";

    public LivroDAOSqlite() {
        prepararCatalogo();
    }

    private void prepararCatalogo() {
        String estrutura = "CREATE TABLE IF NOT EXISTS livros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT NOT NULL, "
                + "autor TEXT NOT NULL, preco REAL NOT NULL CHECK (preco > 0));";
        try (Connection conexao = ConexaoFactory.obterConexao();
             Statement comando = conexao.createStatement()) {
            comando.execute(estrutura);
        } catch (SQLException causa) {
            throw falha("Erro ao criar tabela de livros: ", causa);
        }
    }

    @Override
    public void salvar(Livro livro) {
        String insercao = "INSERT INTO livros (titulo, autor, preco) VALUES (?, ?, ?);";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(insercao, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, livro.getTitulo());
            comando.setString(2, livro.getAutor());
            comando.setDouble(3, livro.getPreco());
            comando.executeUpdate();
            livro.setId(idDaInsercao(comando));
        } catch (SQLException causa) {
            throw falha("Erro ao salvar livro: ", causa);
        }
    }

    private long idDaInsercao(PreparedStatement comando) throws SQLException {
        try (ResultSet chaves = comando.getGeneratedKeys()) {
            if (chaves.next()) {
                return chaves.getLong(1);
            }
            throw new SQLException("Nenhuma chave gerada.");
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        String busca = "SELECT " + CAMPOS + " FROM livros WHERE id = ?;";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement consulta = conexao.prepareStatement(busca)) {
            consulta.setLong(1, id);
            List<Livro> resultado = lerLivros(consulta);
            return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.get(0));
        } catch (SQLException causa) {
            throw falha("Erro ao buscar livro: ", causa);
        }
    }

    @Override
    public List<Livro> listarTodos() {
        String listagem = "SELECT " + CAMPOS + " FROM livros ORDER BY titulo;";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement consulta = conexao.prepareStatement(listagem)) {
            return lerLivros(consulta);
        } catch (SQLException causa) {
            throw falha("Erro ao listar livros: ", causa);
        }
    }

    private List<Livro> lerLivros(PreparedStatement consulta) throws SQLException {
        List<Livro> encontrados = new ArrayList<>();
        try (ResultSet linha = consulta.executeQuery()) {
            while (linha.next()) {
                encontrados.add(new Livro(linha.getLong("id"), linha.getString("titulo"),
                        linha.getString("autor"), linha.getDouble("preco")));
            }
        }
        return encontrados;
    }

    private IllegalStateException falha(String contexto, SQLException causa) {
        return new IllegalStateException(contexto + causa.getMessage(), causa);
    }
}
