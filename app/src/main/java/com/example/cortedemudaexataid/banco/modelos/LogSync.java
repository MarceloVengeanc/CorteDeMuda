package com.example.cortedemudaexataid.banco.modelos;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "CM_LOGSSYNC", indices = {@Index(value = {"tipo"}, name = "IDX_LOGSINC", unique = true)})
public class LogSync {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String tipo;
    private String data;
    private Long registros;
    private String status;

    public LogSync(String tipo, String data, Long registros, String status) {
        this.tipo = tipo;
        this.data = data;
        this.registros = registros;
        this.status = status;
    }

    public LogSync() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getRegistros() {
        return registros;
    }

    public void setRegistros(Long registros) {
        this.registros = registros;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}