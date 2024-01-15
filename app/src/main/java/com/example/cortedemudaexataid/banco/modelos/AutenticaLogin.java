package com.example.cortedemudaexataid.banco.modelos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CM_AUTENTICALOGIN")
public class AutenticaLogin implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    @NonNull
    private String usuario;
    @NonNull
    private String senha;


    public AutenticaLogin(@NonNull String usuario, @NonNull String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public AutenticaLogin() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NonNull String usuario) {
        this.usuario = usuario;
    }

    @NonNull
    public String getSenha() {
        return senha;
    }

    public void setSenha(@NonNull String senha) {
        this.senha = senha;
    }
}