package br.com.autotech.dao;

import br.com.autotech.factory.ConexaoFactory;
import br.com.autotech.model.Peca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaDao {
    private static final String CONSULTA = "SELECT id, codigo, nome, preco, estoque FROM pecas";

    public void criarTabelaSeNaoExistir() {
        String estrutura = "CREATE TABLE IF NOT EXISTS pecas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT UNIQUE NOT NULL, "
                + "nome TEXT NOT NULL, preco REAL NOT NULL CHECK (preco > 0), "
                + "estoque INTEGER NOT NULL CHECK (estoque >= 0));";
        try (Connection conexao = ConexaoFactory.obterConexao();
             Statement comando = conexao.createStatement()) {
            comando.execute(estrutura);
        } catch (SQLException causa) {
            throw falha("Erro ao criar tabela de peças: ", causa);
        }
    }

    public Peca salvar(Peca peca) {
        String insercao = "INSERT INTO pecas (codigo, nome, preco, estoque) VALUES (?, ?, ?, ?);";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(insercao, Statement.RETURN_GENERATED_KEYS)) {
            preencherCadastro(comando, peca);
            comando.executeUpdate();
            peca.setId(lerIdGerado(comando));
            return peca;
        } catch (SQLException causa) {
            throw falha("Erro ao salvar peça (o código deve ser único): ", causa);
        }
    }

    private void preencherCadastro(PreparedStatement comando, Peca peca) throws SQLException {
        comando.setString(1, peca.getCodigo());
        comando.setString(2, peca.getNome());
        comando.setDouble(3, peca.getPreco());
        comando.setInt(4, peca.getEstoque());
    }

    private long lerIdGerado(PreparedStatement comando) throws SQLException {
        try (ResultSet chaves = comando.getGeneratedKeys()) {
            if (chaves.next()) {
                return chaves.getLong(1);
            }
            throw new SQLException("ID não foi gerado.");
        }
    }

    public List<Peca> listarTodas() {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement consulta = conexao.prepareStatement(CONSULTA + " ORDER BY nome;")) {
            return lerResultado(consulta);
        } catch (SQLException causa) {
            throw falha("Erro ao listar peças: ", causa);
        }
    }

    public Peca buscarPorCodigo(String codigo) {
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement consulta = conexao.prepareStatement(CONSULTA + " WHERE codigo = ?;")) {
            consulta.setString(1, codigo);
            List<Peca> encontradas = lerResultado(consulta);
            return encontradas.isEmpty() ? null : encontradas.get(0);
        } catch (SQLException causa) {
            throw falha("Erro ao buscar peça: ", causa);
        }
    }

    private List<Peca> lerResultado(PreparedStatement consulta) throws SQLException {
        List<Peca> pecas = new ArrayList<>();
        try (ResultSet linha = consulta.executeQuery()) {
            while (linha.next()) {
                pecas.add(new Peca(linha.getLong("id"), linha.getString("codigo"),
                        linha.getString("nome"), linha.getDouble("preco"), linha.getInt("estoque")));
            }
        }
        return pecas;
    }

    public boolean darBaixaEstoque(Long id, int quantidadeRetirada) {
        validarRetirada(id, quantidadeRetirada);
        // A condição no próprio UPDATE impede que o estoque fique negativo.
        String baixa = "UPDATE pecas SET estoque = estoque - ? WHERE id = ? AND estoque >= ?;";
        try (Connection conexao = ConexaoFactory.obterConexao();
             PreparedStatement comando = conexao.prepareStatement(baixa)) {
            comando.setInt(1, quantidadeRetirada);
            comando.setLong(2, id);
            comando.setInt(3, quantidadeRetirada);
            int alteradas = comando.executeUpdate();
            return alteradas > 0;
        } catch (SQLException causa) {
            throw falha("Erro ao dar baixa: ", causa);
        }
    }

    private void validarRetirada(Long id, int quantidade) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("ID inválido.");
        }
        if (quantidade < 1) {
            throw new IllegalArgumentException("A retirada deve ser maior que zero.");
        }
    }

    private IllegalStateException falha(String contexto, SQLException causa) {
        return new IllegalStateException(contexto + causa.getMessage(), causa);
    }
}
