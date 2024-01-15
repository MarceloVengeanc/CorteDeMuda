package com.example.cortedemudaexataid.utils;

import android.content.Context;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.banco.dao.AutenticaLoginDAO;
import com.example.cortedemudaexataid.banco.dao.ConfigWSDAO;
import com.example.cortedemudaexataid.banco.modelos.AutenticaLogin;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.example.cortedemudaexataid.telas.LoginActivity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConfigGerais {


    public List<ConfigWS> WS;
    public AutenticaLogin Autenticacao;
    public static Funcionario UsuarioLogado;
    public static ConfigGerais instance;

    public ConfigGerais(List<ConfigWS> ws, AutenticaLogin aut) {

        WS = ws;
        Autenticacao = aut;
    }

    public static ConfigGerais getInstance(Context ctx) throws ExecutionException, InterruptedException {
        if (instance != null) {
            return instance;
        }

        CMDatabase instance = CMDatabase.getInstance((LoginActivity) ctx);
        ConfigWSDAO wsDAO = instance.getConfigWSDAO();
        AutenticaLoginDAO autDao = instance.getAutenticaLoginDAO();
        List<ConfigWS> ws = wsDAO.buscarTodas().get();
        AutenticaLogin aut = autDao.buscarAtivo().get();

        if (ws != null && ws.size() > 0 && aut != null) {
            return new ConfigGerais(ws, aut);
        }
        return null;
    }
}