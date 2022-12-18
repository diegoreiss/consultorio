package br.com.crud.impl;

import br.com.crud.connection.ConnectionManager;
import br.com.crud.dao.EnderecoDAO;
import br.com.crud.entities.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAOImpl implements EnderecoDAO {
    @Override
    public void criar(Endereco endereco, long idPaciente) {

    }

    @Override
    public Endereco alterar(Endereco endereco) {
        return null;
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
