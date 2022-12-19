package br.com.crud.impl;

import br.com.crud.connection.ConnectionManager;
import br.com.crud.dao.EnderecoDAO;
import br.com.crud.dao.PacienteDAO;
import br.com.crud.entities.Endereco;
import br.com.crud.entities.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAOImpl implements EnderecoDAO {
    @Override
    public void criar(Endereco endereco, long idPaciente) {
        PacienteDAO pacienteDAO = new PacienteDAOImpl();
        Paciente pacienteEncontrado = pacienteDAO.pesquisarPorId(idPaciente);

        if (pacienteEncontrado != null) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                connection = ConnectionManager.abrirConexao();

                preparedStatement = connection.prepareStatement(
                        "INSERT INTO endereco " +
                                "(logadouro, cep, id_paciente, numero) " +
                                "VALUES " +
                                "(?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS
                );

                preparedStatement.setString(1, endereco.getLogradouro());
                preparedStatement.setString(2, endereco.getCep());
                preparedStatement.setLong(3, idPaciente);
                preparedStatement.setInt(4, endereco.getNumero());

                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();

                while (resultSet.next()) {
                    endereco.setId(resultSet.getInt(1));
                }

                System.out.printf("Endereço %s(id = %d) cadastrado no registro do paciente %s(id_paciente = %d)%n",
                        endereco.getLogradouro(), endereco.getId(), pacienteEncontrado.getNome(), pacienteEncontrado.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionManager.fecharConexao(connection, preparedStatement, resultSet);
            }
        } else {
            System.err.println("Não existe paciente cadastrado com esse id!");
        }
    }

    @Override
    public Endereco alterar(Endereco endereco) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionManager.abrirConexao();

            preparedStatement = connection.prepareStatement(
                    "UPDATE endereco " +
                            "SET " +
                            "logradouro = ?, " +
                            "cep = ?, " +
                            "numero = ? " +
                            "WHERE " +
                            "id = ?"
            );

            preparedStatement.setString(1, endereco.getLogradouro());
            preparedStatement.setString(2, endereco.getCep());
            preparedStatement.setInt(3, endereco.getNumero());
            preparedStatement.setLong(4, endereco.getId());

            preparedStatement.executeUpdate();

            System.out.printf("Endereço id = %d atualizado%n", endereco.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement);
        }

        return endereco;
    }

    @Override
    public Endereco pesquisarPorId(long id) {
        Endereco enderecoEncontrado = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.abrirConexao();

            preparedStatement = connection.prepareStatement(
                    "SELECT " +
                            "logradouro," +
                            "cep, " +
                            "numero " +
                            "FROM endereco " +
                            "WHERE " +
                            "id = ?"
            );

            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                enderecoEncontrado = new Endereco();
                enderecoEncontrado.setId(id);
                enderecoEncontrado.setLogradouro(resultSet.getString("logradouro"));
                enderecoEncontrado.setCep(resultSet.getString("cep"));
                enderecoEncontrado.setNumero(resultSet.getInt("numero"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement, resultSet);
        }

        return enderecoEncontrado;
    }

    @Override
    public List<Endereco> pesquisarTodosPaciente(long idPaciente) {
        Endereco enderecoEncontrado;
        List<Endereco> enderecos = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionManager.abrirConexao();
            preparedStatement = connection.prepareStatement(
                    "SELECT id, logradouro, cep, numero " +
                            "FROM endereco " +
                            "WHERE " +
                            "id_paciente = ?;"
            );

            preparedStatement.setLong(1, idPaciente);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                enderecoEncontrado = new Endereco();
                enderecoEncontrado.setId(resultSet.getInt("id"));
                enderecoEncontrado.setCep(resultSet.getString("cep"));
                enderecoEncontrado.setLogradouro(resultSet.getString("logradouro"));
                enderecoEncontrado.setNumero(resultSet.getInt("numero"));

                enderecos.add(enderecoEncontrado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.fecharConexao(connection, preparedStatement, resultSet);
        }

        return enderecos;
    }

    @Override
    public void excluir(long id) {

    }
}
