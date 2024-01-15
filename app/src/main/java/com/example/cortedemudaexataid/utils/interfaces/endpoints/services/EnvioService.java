package com.example.cortedemudaexataid.utils.interfaces.endpoints.services;

import com.example.cortedemudaexataid.banco.modelos.Registros;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EnvioService {
    //ENVIO
    @POST("post")
    Call<Registros> enviarRegistros(@Body Registros registros);

}