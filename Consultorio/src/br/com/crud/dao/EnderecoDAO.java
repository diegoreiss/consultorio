package br.com.crud.dao;

import br.com.crud.entities.Endereco;

import java.util.List;

public interface EnderecoDAO {
    void criar(Endereco endereco, long idPaciente);
    Endereco alterar(Endereco endereco);
    Endereco pesquisarPorId(long id);
    List<Endereco> pesquisarTodosPaciente(long idPaciente);
    void excluir(long id);
}
