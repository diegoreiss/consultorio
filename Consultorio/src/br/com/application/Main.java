package br.com.application;

import br.com.crud.connection.ConnectionManager;
import br.com.crud.dao.EnderecoDAO;
import br.com.crud.dao.PacienteDAO;
import br.com.crud.entities.Endereco;
import br.com.crud.entities.Paciente;
import br.com.crud.impl.EnderecoDAOImpl;
import br.com.crud.impl.PacienteDAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        startDb();

        PacienteDAO pacienteDAO = new PacienteDAOImpl();
        EnderecoDAO enderecoDAO = new EnderecoDAOImpl();

        Paciente diego = new Paciente();
        diego.setNome("Diego Reis");
        diego.setCpf("546.057.391-22");
        diego.setNascimento(LocalDate.of(2002, Month.OCTOBER, 17));
        diego.setEnderecos(new ArrayList<>());

        Paciente agnaldo = new Paciente();
        agnaldo.setNome("Agnaldo Silva");
        agnaldo.setCpf("636.768.808-03");
        agnaldo.setNascimento(LocalDate.of(1980, Month.JANUARY, 1));
        agnaldo.setEnderecos(new ArrayList<>());

        Paciente ana = new Paciente();
        ana.setNome("Ana Schmitz");
        ana.setCpf("994.584.620-53");
        ana.setNascimento(LocalDate.of(1981, Month.FEBRUARY, 2));
        ana.setEnderecos(new ArrayList<>());

        Paciente wendel = new Paciente();
        wendel.setNome("Wendel Clarindo");
        wendel.setCpf("615.899.932-68");
        wendel.setNascimento(LocalDate.of(1982, Month.MARCH, 3));
        wendel.setEnderecos(new ArrayList<>());

        Paciente matheus = new Paciente();
        matheus.setNome("Matheus Amaral");
        matheus.setCpf("105.180.691-76");
        matheus.setNascimento(LocalDate.of(1983, Month.APRIL, 4));
        matheus.setEnderecos(new ArrayList<>());

        List<Paciente> pacientes = Arrays.asList(diego, agnaldo, ana, wendel, matheus);

        for (Paciente paciente : pacientes) {
            pacienteDAO.criar(paciente);
        }

        System.out.println("\n");

        Paciente pacienteEncontrado = pacienteDAO.pesquisarPorId(4);
        pacienteEncontrado.setNome("Wendel Clarindo Dos Santos");
        pacienteEncontrado.setCpf("168.763.821-76");
        pacienteEncontrado.setNascimento(LocalDate.of(2002, Month.DECEMBER, 10));

        pacienteDAO.alterar(pacienteEncontrado);

        System.out.println("\n");

        for (Paciente p : pacienteDAO.pesquisarTodos()) {
            System.out.printf("%d | %s | %s | %s%n",
                    p.getId(), p.getNome(), p.getCpf(),
                    p.getNascimento());
        }

        System.out.println("\n");

        Endereco endereco1 = new Endereco();
        endereco1.setCep("88135-065");
        endereco1.setLogradouro("Servidão José Pereira");
        endereco1.setNumero(280);

        Endereco endereco2 = new Endereco();
        endereco2.setCep("65086-770");
        endereco2.setLogradouro("Rua Tancredo Neves");
        endereco2.setNumero(398);

        Endereco endereco3 = new Endereco();
        endereco3.setCep("68377-436");
        endereco3.setLogradouro("Alameda Bom Jardim II");
        endereco3.setNumero(683);

        Endereco endereco4 = new Endereco();
        endereco4.setCep("69911-178");
        endereco4.setLogradouro("Rua 4 De Outubro");
        endereco4.setNumero(924);

        Endereco endereco5 = new Endereco();
        endereco5.setCep("8705-110");
        endereco5.setLogradouro("Avenida Pintassilgo");
        endereco5.setNumero(362);

        Endereco endereco6 = new Endereco();
        endereco6.setCep("69054-759");
        endereco6.setLogradouro("Rua Maurice Ravel");
        endereco6.setNumero(909);

        enderecoDAO.criar(endereco1, diego.getId());
        enderecoDAO.criar(endereco2, diego.getId());
        enderecoDAO.criar(endereco3, agnaldo.getId());
        enderecoDAO.criar(endereco4, ana.getId());
        enderecoDAO.criar(endereco5, wendel.getId());
        enderecoDAO.criar(endereco6, matheus.getId());

        System.out.println("\n");

        Endereco enderecoEncontrado = enderecoDAO.pesquisarPorId(endereco1.getId());
        enderecoEncontrado.setLogradouro("Rua Doutor Dório Silva");
        enderecoEncontrado.setCep("29103-480");
        enderecoEncontrado.setNumero(569);
        enderecoDAO.alterar(enderecoEncontrado);

        System.out.println("\n");

        System.out.println("Endereços do Paciente " + diego.getNome());
        for (Endereco endereco : enderecoDAO.pesquisarTodosPaciente(diego.getId())) {
            System.out.println(endereco);
        }

        System.out.println("\n");

        enderecoDAO.excluir(endereco6.getId());

        System.out.println("\n");

        pacienteDAO.excluir(diego.getId());
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
