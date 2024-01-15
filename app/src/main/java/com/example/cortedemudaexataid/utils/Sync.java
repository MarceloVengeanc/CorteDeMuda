package com.example.cortedemudaexataid.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.Nullable;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.example.cortedemudaexataid.banco.modelos.LogSync;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.banco.modelos.Talhao;
import com.example.cortedemudaexataid.utils.interfaces.endpoints.repos.CadastroRepo;
import com.example.cortedemudaexataid.utils.interfaces.endpoints.repos.EnvioRepo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sync extends Service implements Runnable {
    static int fazendasTerminou = 0;
    static int talhoesTerminou = 0;
    static int funcionariosTerminou = 0;
    static int eRegistros = 0;
    private static List<Registros> registros;
    private final Context context;
    private static Registros pacoteRegistros;

    public Sync(Context ctx) {
        context = ctx;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        // ValidaModulos(context);
        AtualizaCadastrosSistema(context);
    }

    public static String retornaDataAgora() {
        String Data;
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
        Data = formata.format(calendario.getTime());
        return Data;
    }

    public static void AtualizaCadastrosSistema(Context context) {
        ConfigWS configWS = null;
        try {
            configWS = CMDatabase.getInstance(context).getConfigWSDAO().buscarUm().get();
            CadastroRepo repo = null;
            EnvioRepo envioRepo = null;
            try {
                repo = CadastroRepo.getInstance(configWS);
                envioRepo = EnvioRepo.getInstance(configWS);
            } catch (Exception e) {
                return;
            }
//            CADASTRO DE FAZENDAS
            repo.getService().baixarTodasFazendas().enqueue(new Callback<List<Fazendas>>() {
                @Override
                public void onResponse(Call<List<Fazendas>> call, Response<List<Fazendas>> response) {
                    try {
                        if (response.body() != null) {
                            List<Fazendas> resposta = response.body();
                            CMDatabase.getInstance(context).getFazendasDAO().inserir(resposta).get();
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FAZENDAS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            fazendasTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FAZENDAS",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Fazendas>> call, Throwable t) {

                }
            });
//            CADASTRO TALHOES
            repo.getService().baixarTodosTalhoes().enqueue(new Callback<List<Talhao>>() {
                @Override
                public void onResponse(Call<List<Talhao>> call, Response<List<Talhao>> response) {
                    try {
                        if (response.body() != null) {
                            List<Talhao> resposta = response.body();
                            CMDatabase.getInstance(context).getTalhaoDAO().inserir(resposta).get();
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_TALHÕES",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            talhoesTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_TALHÕES",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Talhao>> call, Throwable t) {

                }
            });
//            CADASTRO FUNCIONÁRIOS
            repo.getService().baixarTodosFuncionarios().enqueue(new Callback<List<Funcionario>>() {
                @Override
                public void onResponse(Call<List<Funcionario>> call, Response<List<Funcionario>> response) {
                    try {
                        if (response.isSuccessful()) {
                            List<Funcionario> resposta = response.body();
                            List<Funcionario> ativos = resposta.stream().filter(funcionario -> funcionario.getAtivo().equalsIgnoreCase("Ativo")).collect(Collectors.toList());
                            List<Funcionario> inativos = resposta.stream().filter(funcionario -> funcionario.getAtivo().equalsIgnoreCase("Inativo")).collect(Collectors.toList());
                            CMDatabase.getInstance(context).getFuncionarioDAO().inserir(ativos).get();
                            if (inativos.size() > 0) {
                                CMDatabase.getInstance(context).getFuncionarioDAO().excluir(inativos).get();
                            }
                            CMDatabase.getInstance(context).getFuncionarioDAO().inserir(resposta).get();
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FUNCIONÁRIOS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            funcionariosTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FUNCIONÁRIOS",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Funcionario>> call, Throwable t) {

                }
            });
            CMDatabase db = CMDatabase.getInstance(context.getApplicationContext());
            //ENVIO DE REGISTRO/APONTAMENTO
            if (ConfigGerais.UsuarioLogado != null) {
                registros = CMDatabase.getInstance(context).getRegistrosDAO().buscarPorNaoEnviado("N", ConfigGerais.UsuarioLogado.getNome()).get();
                for (Registros re : registros) {
                    pacoteRegistros = re;
                }

                envioRepo.getService().enviarRegistros(pacoteRegistros).enqueue(new Callback<Registros>() {
                    @Override
                    public void onResponse(Call<Registros> call, Response<Registros> response) {
                        if (response.body() != null) {
                            if (registros.size() == 0) return;
                            for (int i = 0; i <= registros.size(); i++) {
                                Registros registrosOK = registros.stream().filter(u -> u.getEnviado().equalsIgnoreCase("N")).findFirst().orElse(null);
                                if (registrosOK != null) {
                                    registrosOK.setEnviado("S");
                                    registrosOK.setDataEnviado(U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD_HH_MM_SS_SSS));
                                    db.getRegistrosDAO().atualizar(registrosOK);
                                }
                            }
                            try {
                                CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                        new LogSync(
                                                "REGISTROS_REALIZADOS",
                                                retornaDataAgora(),
                                                Long.parseLong(String.valueOf(registros.size())),
                                                "FINALIZADO")
                                ).get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registros> call, Throwable t) {
                        t.printStackTrace();
                        try {
                            CMDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "REGISTROS_REALIZADOS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(registros.size())), "FALHOU")
                            ).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (ExecutionException | InterruptedException u) {
            u.printStackTrace();
        }
    }
}
