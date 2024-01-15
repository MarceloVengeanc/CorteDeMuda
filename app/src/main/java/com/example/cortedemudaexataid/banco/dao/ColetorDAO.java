package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cortedemudaexataid.banco.modelos.Coletor;
import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface ColetorDAO extends ModeloDAO<Coletor> {

    @Query("SELECT * FROM CM_COLETORES ORDER BY numero DESC LIMIT 1")
    ListenableFuture<Coletor> buscarAtivo();

    @Query("SELECT * FROM CM_COLETORES WHERE numero = :numero")
    ListenableFuture<Coletor> buscarPorNumero(Long numero);
}

