package com.example.cortedemudaexataid.banco.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

public interface ModeloDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> inserir(T valor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Long> inserirRetornar(T valor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> inserir(List<T> valor);

    @Update
    ListenableFuture<Void> atualizar(T valor);

    @Delete
    ListenableFuture<Void> excluir(T valor);

    @Delete
    ListenableFuture<Void> excluir(List<T> valor);
}
