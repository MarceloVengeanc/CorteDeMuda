package com.example.cortedemudaexataid.banco.modelos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CM_CONFIGWS")
public class ConfigWS implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String url;
    @NonNull
    private Integer porta;
    @NonNull
    private Integer empresa;
    @NonNull
    private Integer prioridade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public Integer getPorta() {
        return porta;
    }

    public void setPorta(@NonNull Integer porta) {
        this.porta = porta;
    }

    @NonNull
    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(@NonNull Integer empresa) {
        this.empresa = empresa;
    }

    @NonNull
    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(@NonNull Integer prioridade) {
        this.prioridade = prioridade;
    }
}
