package com.example.cortedemudaexataid;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;

import com.example.cortedemudaexataid.banco.dao.AutenticaLoginDAO;
import com.example.cortedemudaexataid.banco.dao.ColetorDAO;
import com.example.cortedemudaexataid.banco.dao.ConfigWSDAO;
import com.example.cortedemudaexataid.banco.dao.FazendasDAO;
import com.example.cortedemudaexataid.banco.dao.FuncionarioDAO;
import com.example.cortedemudaexataid.banco.dao.LogSyncDAO;
import com.example.cortedemudaexataid.banco.dao.RegistrosDAO;
import com.example.cortedemudaexataid.banco.dao.SincDAO;
import com.example.cortedemudaexataid.banco.dao.TalhaoDAO;
import com.example.cortedemudaexataid.banco.modelos.AutenticaLogin;
import com.example.cortedemudaexataid.banco.modelos.Coletor;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.example.cortedemudaexataid.banco.modelos.LogSync;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.banco.modelos.Sinc;
import com.example.cortedemudaexataid.banco.modelos.Talhao;
import com.example.cortedemudaexataid.utils.Conversores;

@Database(
        entities = {
                Fazendas.class,
                Talhao.class,
                Funcionario.class,
                Registros.class,
                ConfigWS.class,
                Sinc.class,
                LogSync.class,
                Coletor.class,
                AutenticaLogin.class
        },
        version = 10, exportSchema = false
)
@TypeConverters({Conversores.class})
public abstract class CMDatabase extends RoomDatabase {
    public static final String CM_DATABASE = "corteDeMuda_db";
    private static CMDatabase INSTANCE;

    public abstract FazendasDAO getFazendasDAO();

    public abstract TalhaoDAO getTalhaoDAO();

    public abstract FuncionarioDAO getFuncionarioDAO();

    public abstract RegistrosDAO getRegistrosDAO();

    public abstract ConfigWSDAO getConfigWSDAO();

    public abstract SincDAO getSincDAO();

    public abstract LogSyncDAO getLogSyncDAO();

    public abstract ColetorDAO getColetorDAO();

    public abstract AutenticaLoginDAO getAutenticaLoginDAO();

    public Migration[] migrations = {};

    public static CMDatabase getInstance(Context ctx) {
        return Room.databaseBuilder(ctx, CMDatabase.class, CM_DATABASE).fallbackToDestructiveMigration().build();
    }
}
