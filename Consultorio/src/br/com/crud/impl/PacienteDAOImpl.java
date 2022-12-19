package br.com.crud.impl;

import br.com.crud.connection.ConnectionManager;
import br.com.crud.dao.EnderecoDAO;
import br.com.crud.dao.PacienteDAO;
import br.com.crud.entities.Endereco;
import br.com.crud.entities.Paciente;

import java.sql.*;
import java.util.ArrayList;
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
        if (pesquisarTodos().isEmpty()) {
            System.err.println("Sem pacientes para alterar!");
        } else {
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = ConnectionManager.abrirConexao();

                preparedStatement = connection.prepareStatement(
                        "UPDATE paciente " +
                                "SET " +
                                "nome = ?," +
                                "cpf = ?," +
                                "nascimento = ? " +
                                "WHERE " +
                                "id = ?;"
                );

                preparedStatement.setString(1, paciente.getNome());
                preparedStatement.setString(2, paciente.getCpf());
                preparedStatement.setDate(3, Date.valueOf(paciente.getNascimento()));
                preparedStatement.setLong(4, paciente.getId());

                preparedStatement.executeUpdate();

                System.out.printf("Paciente id = %d atualizado%n", paciente.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionManager.fecharConexao(connection, preparedStatement);
            }
        }

        return paciente;
    }

    @Override
    public Paciente pesquisarPorId(long id) {
        EnderecoDAO enderecoDAO = new EnderecoDAOImpl();

        Paciente paciente = null;
        List<Endereco> enderecosPaciente = enderecoDAO.pesquisarTodosPaciente(id);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.abrirConexao();

            preparedStatement = connection.prepareStatement(
                    "SELECT " +
                            "nome, cpf, nascimento " +
                            "FROM paciente " +
                            "WHERE " +
                            "id = ?;"
            );

            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                paciente = new Paciente();
                paciente.setId(id);
                paciente.setNome(resultSet.getString("nome"));
                paciente.setCpf(resultSet.getString("cpf"));
                paciente.setNascimento(resultSet.getDate("nascimento").toLocalDate());
                paciente.setEnderecos(enderecosPaciente);
            }
        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement, resultSet);
        }

        return paciente;
    }

    @Override
    public List<Paciente> pesquisarTodos() {
        Paciente paciente;
        List<Paciente> pacientes = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.abrirConexao();

            preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM paciente;"
            );

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                paciente = new Paciente();
                paciente.setId(resultSet.getLong("id"));
                paciente.setNome(resultSet.getString("nome"));
                paciente.setCpf(resultSet.getString("cpf"));
                paciente.setNascimento(resultSet.getDate("nascimento").toLocalDate());

                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement, resultSet);
        }

        return pacientes;
    }

    @Override
    public void excluir(long id) {
        Paciente pacienteEncontrado = pesquisarPorId(id);

        if (pacienteEncontrado != null) {
            EnderecoDAO enderecoDAO = new EnderecoDAOImpl();

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = ConnectionManager.abrirConexao();

                preparedStatement = connection.prepareStatement(
                        "DELETE FROM paciente " +
                                "WHERE " +
                                "id = ?;"
                );

                preparedStatement.setLong(1, id);

                for (Endereco enderecoPaciente : enderecoDAO.pesquisarTodosPaciente(id)) {
                    enderecoDAO.excluir(enderecoPaciente.getId());
                }

                preparedStatement.executeUpdate();

                System.out.printf("Paciente %s(id = %d) deletado do banco!",
                        pacienteEncontrado.getNome(), id);
            } catch (SQLException e ) {
                e.printStackTrace();
            } finally {
                ConnectionManager.fecharConexao(connection, preparedStatement);
            }
        } else {
            System.err.println("Não existe paciente registrado com esse id!");
        }
    }
}
