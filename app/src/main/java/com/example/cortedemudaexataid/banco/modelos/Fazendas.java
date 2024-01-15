package com.example.cortedemudaexataid.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "CM_FAZENDAS")
public class Fazendas implements Serializable {
    @PrimaryKey
    private long id;
    private String nomeFazenda;
    private int codFazenda;

    public Fazendas() {
    }

    public Fazendas(long id, String nomeFazenda, int codFazenda) {
        this.id = id;
        this.nomeFazenda = nomeFazenda;
        this.codFazenda = codFazenda;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeFazenda() {
        return nomeFazenda;
    }

    public void setNomeFazenda(String nomeFazenda) {
        this.nomeFazenda = nomeFazenda;
    }

    public int getCodFazenda() {
        return codFazenda;
    }

    public void setCodFazenda(int codFazenda) {
        this.codFazenda = codFazenda;
    }
}
