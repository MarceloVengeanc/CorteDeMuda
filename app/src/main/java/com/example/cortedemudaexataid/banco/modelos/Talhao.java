package com.example.cortedemudaexataid.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CM_TALHAO")
public class Talhao implements Serializable {
    @PrimaryKey
    private long id;
    private int codFazenda;
    private int codTalhao;
    private float area;

    public Talhao(long id, int codFazenda, int codTalhao, float area) {
        this.id = id;
        this.codFazenda = codFazenda;
        this.codTalhao = codTalhao;
        this.area = area;
    }

    public Talhao() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCodFazenda() {
        return codFazenda;
    }

    public void setCodFazenda(int codFazenda) {
        this.codFazenda = codFazenda;
    }

    public int getCodTalhao() {
        return codTalhao;
    }

    public void setCodTalhao(int codTalhao) {
        this.codTalhao = codTalhao;
    }

    public float getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }
}
