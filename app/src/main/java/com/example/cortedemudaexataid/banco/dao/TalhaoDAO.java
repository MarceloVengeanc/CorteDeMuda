package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cortedemudaexataid.banco.modelos.Talhao;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface TalhaoDAO extends ModeloDAO<Talhao>{
    @Query("SELECT * FROM CM_TALHAO")
    ListenableFuture<List<Talhao>> buscarTodos();

    @Query("SELECT * FROM CM_TALHAO t WHERE t.codFazenda=:codFazenda")
    ListenableFuture<List<Talhao>> buscaTalhaoFazenda(int codFazenda);
}
