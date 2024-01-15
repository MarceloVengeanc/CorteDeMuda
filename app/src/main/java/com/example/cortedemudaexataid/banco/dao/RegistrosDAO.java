package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface RegistrosDAO extends ModeloDAO<Registros> {

    @Query("SELECT * FROM CM_REGISTROS")
    ListenableFuture<List<Registros>> buscarTodos();

    @Query("SELECT * FROM CM_REGISTROS WHERE nomeFuncionario=:funcionario")
    ListenableFuture<List<Registros>>buscarPorFUncionario(String funcionario);

    @Query("SELECT * FROM CM_REGISTROS r WHERE r.nomeFuncionario=:funcionario and r.dataRegistro between :dataInicial and :dataFinal order by id desc")
    ListenableFuture<List<Registros>> buscarRegistrosPorData(String funcionario, String dataInicial, String dataFinal);

    @Query("SELECT * FROM CM_REGISTROS r WHERE r.matriculaFuncionario=:matriculaFuncionario order by r.dataRegistro desc")
    ListenableFuture<List<Registros>> buscaTodosRegistrosFuncionário(long matriculaFuncionario);

    @Query("SELECT * FROM CM_REGISTROS r where r.enviado=:enviado and r.nomeFuncionario=:funcionario")
    ListenableFuture<List<Registros>>buscarPorNaoEnviado(String enviado, String funcionario );
}
