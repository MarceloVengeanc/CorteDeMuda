package com.example.cortedemudaexataid.utils.interfaces.endpoints.services;

import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.banco.modelos.Talhao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CadastrosService {

    //Busca Fazendas
    @GET("d6608f55116cdad8dbaa")
    Call<List<Fazendas>> baixarTodasFazendas();

    //Busca Talhões
    @GET("45de8105ab10c26f5f3a")
    Call<List<Talhao>> baixarTodosTalhoes();

    //Busca Funcionarios
    @GET("655afe3ab06b338099da")
    Call<List<Funcionario>> baixarTodosFuncionarios();

    @POST("post")
    Call<Registros> enviarRequisicaoFinalizada(@Body Registros registros);

}