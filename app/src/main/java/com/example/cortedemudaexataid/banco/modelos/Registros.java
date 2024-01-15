package com.example.cortedemudaexataid.banco.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CM_REGISTROS")
public class Registros implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long coletor;
    private String nomeFuncionario;
    private long matriculaFuncionario;
    private String dataRegistro;
    private String dataRegistroSalvo;
    private int codFazenda;
    private String nomeFazenda;
    private int codTalhao;
    private float area;
    private String totalOuParcial;
    private float estimativa;
    private String ultimoCorte;
    private String enviado;
    private String dataEnviado;

    public Registros() {
    }

    public Registros(long id, long coletor, String nomeFuncionario, long matriculaFuncionario, String dataRegistro, String dataRegistroSalvo, int codFazenda, String nomeFazenda, int codTalhao, float area, String totalOuParcial, float estimativa, String ultimoCorte, String enviado, String dataEnviado) {
        this.id = id;
        this.coletor = coletor;
        this.nomeFuncionario = nomeFuncionario;
        this.matriculaFuncionario = matriculaFuncionario;
        this.dataRegistro = dataRegistro;
        this.dataRegistroSalvo = dataRegistroSalvo;
        this.codFazenda = codFazenda;
        this.nomeFazenda = nomeFazenda;
        this.codTalhao = codTalhao;
        this.area = area;
        this.totalOuParcial = totalOuParcial;
        this.estimativa = estimativa;
        this.ultimoCorte = ultimoCorte;
        this.enviado = enviado;
        this.dataEnviado = dataEnviado;
    }

    public long getColetor() {
        return coletor;
    }

    public void setColetor(long coletor) {
        this.coletor = coletor;
    }

    public String getDataEnviado() {
        return dataEnviado;
    }

    public void setDataEnviado(String dataEnviado) {
        this.dataEnviado = dataEnviado;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public long getMatriculaFuncionario() {
        return matriculaFuncionario;
    }

    public void setMatriculaFuncionario(long matriculaFuncionario) {
        this.matriculaFuncionario = matriculaFuncionario;
    }

    public String getDataRegistroSalvo() {
        return dataRegistroSalvo;
    }

    public void setDataRegistroSalvo(String dataRegistroSalvo) {
        this.dataRegistroSalvo = dataRegistroSalvo;
    }

    public String getUltimoCorte() {
        return ultimoCorte;
    }

    public void setUltimoCorte(String ultimoCorte) {
        this.ultimoCorte = ultimoCorte;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public int getCodFazenda() {
        return codFazenda;
    }

    public void setCodFazenda(int codFazenda) {
        this.codFazenda = codFazenda;
    }

    public String getNomeFazenda() {
        return nomeFazenda;
    }

    public void setNomeFazenda(String nomeFazenda) {
        this.nomeFazenda = nomeFazenda;
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

    public void setArea(float area) {
        this.area = area;
    }

    public String getTotalOuParcial() {
        return totalOuParcial;
    }

    public void setTotalOuParcial(String totalOuParcial) {
        this.totalOuParcial = totalOuParcial;
    }

    public float getEstimativa() {
        return estimativa;
    }

    public void setEstimativa(float estimativa) {
        this.estimativa = estimativa;
    }
}
