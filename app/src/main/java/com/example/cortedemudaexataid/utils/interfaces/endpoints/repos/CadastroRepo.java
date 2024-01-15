package com.example.cortedemudaexataid.utils.interfaces.endpoints.repos;

import java.util.concurrent.TimeUnit;

import com.example.cortedemudaexataid.utils.interfaces.endpoints.services.CadastrosService;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroRepo {
    private static CadastroRepo instance;
    private final CadastrosService service;


    public static CadastroRepo getInstance(ConfigWS servidor) {
        if (instance == null) {
            instance = new CadastroRepo(servidor);
        }
        return instance;
    }

    public CadastroRepo(ConfigWS servidor) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                // .baseUrl(String.format("%s:%s/",servidor.getUrl(),servidor.getPorta()))
//                .baseUrl(String.format("https://httpbin.org/"))
                .baseUrl(String.format("https://api.npoint.io/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CadastrosService.class);
    }

    public CadastrosService getService() {
        return service;
    }
}