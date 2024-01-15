package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cortedemudaexataid.banco.modelos.AutenticaLogin;
import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface AutenticaLoginDAO extends ModeloDAO<AutenticaLogin> {
    @Query("SELECT * FROM CM_AUTENTICALOGIN ORDER BY ID DESC LIMIT 1")
    ListenableFuture<com.example.cortedemudaexataid.banco.modelos.AutenticaLogin> buscarAtivo();

    @Query("SELECT * FROM CM_AUTENTICALOGIN WHERE id = :id")
    ListenableFuture<AutenticaLogin> buscarPorId(Long id);
}
