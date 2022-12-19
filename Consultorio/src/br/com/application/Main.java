package br.com.application;

import br.com.crud.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

    }

    public static void startDb() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String dropTables = "DROP TABLE IF EXISTS paciente, endereco";

        String createPaciente = "CREATE TABLE IF NOT EXISTS paciente (\n" +
                "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                "    nome VARCHAR(255) NOT NULL,\n" +
                "    cpf VARCHAR(45) NOT NULL,\n" +
                "    nascimento DATE NOT NULL\n" +
                ");";

        String createEndereco = "CREATE TABLE IF NOT EXISTS endereco (\n" +
                "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
                "    logradouro VARCHAR(255) NOT NULL,\n" +
                "    cep VARCHAR(15) NULL,\n" +
                "    id_paciente INT NOT NULL,\n" +
                "    numero INT NULL,\n" +
                "    CONSTRAINT fk_endereco_paciente\n" +
                "        FOREIGN KEY (id_paciente)\n" +
                "        REFERENCES paciente (id)\n" +
                "        ON DELETE NO ACTION\n" +
                "        ON UPDATE NO ACTION\n" +
                ");";

        try {
            connection = ConnectionManager.abrirConexao();

            preparedStatement = connection.prepareStatement(dropTables);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(createPaciente);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(createEndereco);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement);
        }
    }
}
