package br.com.crud.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String USUARIO_BD = "seuUsuario";
    private static final String SENHA_BD = "suaSenha";
    private static final String NOME_BANCO = "seuBD";
    private static final String TIMEZONE = "?useTimezone=true&serverTimezone=America/Sao_Paulo&zeroDateTimeBehavior=convertToNull";
    private static final String STR_CON = "jdbc:mysql://localhost:3306/" + NOME_BANCO + TIMEZONE;

    public static Connection abrirConexao() {
        try {
            Connection connection = DriverManager.getConnection(STR_CON, USUARIO_BD, SENHA_BD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Problemas de credenciais ou driver: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Problema de lógica ao abrir conexão: " + e.getMessage());
        }
    }
}
