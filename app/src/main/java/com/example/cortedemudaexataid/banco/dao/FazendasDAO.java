package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface FazendasDAO extends ModeloDAO<Fazendas> {
    @Query("SELECT * FROM CM_FAZENDAS")
    ListenableFuture<List<Fazendas>> buscarTodas();
}
