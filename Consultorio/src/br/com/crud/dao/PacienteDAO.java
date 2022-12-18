package br.com.crud.dao;

import br.com.crud.entities.Paciente;

import java.util.List;

public interface PacienteDAO {
    Paciente criar(Paciente paciente);
    Paciente alterar(Paciente paciente);
    Paciente pesquisarPorId(long id);
    List<Paciente> pesquisarTodos();
    void excluir(long id);
}
