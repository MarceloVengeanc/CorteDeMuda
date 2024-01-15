package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface FuncionarioDAO extends ModeloDAO<Funcionario> {

    @Query("SELECT * FROM CM_FUNCIONARIO")
    ListenableFuture<List<Funcionario>> buscarTodos();

    @Query("SELECT * FROM CM_FUNCIONARIO WHERE login = :login and senha = :senha")
    ListenableFuture<Funcionario> validarLogin(String login, String senha);
}
