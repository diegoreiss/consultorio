package br.com.crud.impl;

import br.com.crud.connection.ConnectionManager;
import br.com.crud.dao.PacienteDAO;
import br.com.crud.entities.Paciente;

import java.sql.*;
import java.util.List;

public class PacienteDAOImpl implements PacienteDAO {
    @Override
    public Paciente criar(Paciente paciente) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.abrirConexao();

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO paciente " +
                            "(nome, cpf, nascimento) " +
                            "VALUES " +
                            "(?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, paciente.getNome());
            preparedStatement.setString(2, paciente.getCpf());
            preparedStatement.setDate(3, Date.valueOf(paciente.getNascimento()));

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                paciente.setId(resultSet.getLong(1));
            }

            System.out.println("Paciente " + paciente.getNome() + " inserido!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement, resultSet);
        }

        return paciente;
    }

    @Override
    public Paciente alterar(Paciente paciente) {
        return null;
    }

    @Override
    public Paciente pesquisarPorId(long id) {
        return null;
    }

    @Override
    public List<Paciente> pesquisarTodos() {
        return null;
    }

    @Override
    public void excluir(long id) {

    }
}
