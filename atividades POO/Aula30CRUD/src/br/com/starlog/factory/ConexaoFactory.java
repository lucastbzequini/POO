package br.com.starlog.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoFactory {
    private static final String PROPRIEDADE_URL = "br.com.starlog.jdbc.url";
    private static final String BANCO_PADRAO = "jdbc:sqlite:almoxarifado.db";

    private ConexaoFactory() {
    }

    public static Connection obterConexao() throws SQLException {
        String endereco = System.getProperty(PROPRIEDADE_URL);
        if (endereco == null) {
            endereco = BANCO_PADRAO;
        }
        return DriverManager.getConnection(endereco);
    }
}
