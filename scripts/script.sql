DROP TABLE IF EXISTS paciente, endereco;

CREATE TABLE IF NOT EXISTS paciente (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(45) NOT NULL,
    nascimento DATE NOT NULL
);

CREATE TABLE endereco (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    logradouro VARCHAR(255) NOT NULL,
    cep VARCHAR(15) NULL,
    id_paciente INT NOT NULL,
    numero INT NULL,
    CONSTRAINT fk_endereco_paciente
        FOREIGN KEY (id_paciente)
        REFERENCES paciente (id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);
