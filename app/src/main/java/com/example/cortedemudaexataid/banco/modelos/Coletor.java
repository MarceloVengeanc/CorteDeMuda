package com.example.cortedemudaexataid.banco.modelos;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CM_COLETORES")
public class Coletor implements Serializable {
    private static final long serialVersionUID = 1L;

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private Long numero;
    private String descricao;
    private Long empresaId;
    private String empregador;

    public Coletor() {
    }

    public Coletor(@NonNull Long numero, String descricao, Long empresaId, String empregador) {
        this.numero = numero;
        this.descricao = descricao;
        this.empresaId = empresaId;
        this.empregador = empregador;
    }

    public String getEmpregador() {
        return empregador;
    }

    public void setEmpregador(String empregador) {
        this.empregador = empregador;
    }

    @NonNull
    public Long getNumero() {
        return numero;
    }

    public void setNumero(@NonNull Long numero) {
        this.numero = numero;
    }

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }
}
