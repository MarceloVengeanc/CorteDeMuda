package com.example.cortedemudaexataid.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CM_FUNCIONARIO")
public class Funcionario implements Serializable {
    @PrimaryKey
    private long id;
    private long matricula;
    private String nome;
    private long turma;
    private String pis;
    private String ativo;
    private String login;
    private String senha;

    public Funcionario() {
    }

    public Funcionario(long id, long matricula, String nome, long turma, String pis, String ativo, String login, String senha) {
        this.id = id;
        this.matricula = matricula;
        this.nome = nome;
        this.turma = turma;
        this.pis = pis;
        this.ativo = ativo;
        this.login = login;
        this.senha = senha;
    }

    public long getTurma() {
        return turma;
    }

    public void setTurma(long turma) {
        this.turma = turma;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMatricula() {
        return matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
