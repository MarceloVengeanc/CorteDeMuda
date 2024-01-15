package com.example.cortedemudaexataid.telas;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.banco.modelos.LogSync;
import com.example.cortedemudaexataid.banco.modelos.Sinc;
import com.example.cortedemudaexataid.telas.adapters.SincAdapter;
import com.example.cortedemudaexataid.telas.configs.ConfigWebServiceActivity;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;
import com.example.cortedemudaexataid.utils.Sync;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class SincronizarActivity extends AppCompatActivity {

    private RecyclerView rvRegistros, rvCadastros;
    private TextView txtUltimoSinc;
    private Button btnSinc;
    private Context ctx;
    private ConfigWS listaURL;
    private List<Sinc> listaSync;
    private static List<LogSync> listaLogSyncCadastros, listaLogSyncRegistros;
    private int acrescentaRvCad = 0;
    private int acrescentaRvReg = 0;
    public static Boolean status = false;

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        ctx = this;

        carregarBotoes();
        carregarListener();
        carregarUltimaSinc();
        registerReceiver(AtualizarDataUltimoSync, new IntentFilter("AtualizarDataUltimoSinc"));
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AtualizarDataUltimoSync != null) unregisterReceiver(AtualizarDataUltimoSync);
    }

    private final BroadcastReceiver AtualizarDataUltimoSync = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Sinc s = new Sinc();
                s.setDataFimSinc(new Date());
                CMDatabase.getInstance(context).getSincDAO().inserir(s).get();
                if (listaSync.size() > 2000) {
                    CMDatabase.getInstance(context).getSincDAO().excluir(listaSync).get();
                }
                carregarBotoes();
                carregarUltimaSinc();
                carregarListener();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    final static class Sincronizando extends AsyncTask<Void, Void, String> {

        @SuppressLint("StaticFieldLeak")
        private Context context;
        private AlertDialog progressoDialog;
        public Sincronizando(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Context ctx = new ContextThemeWrapper(context, R.style.dialogMaterial);
            progressoDialog = new MaterialAlertDialogBuilder(ctx)
                    .setView(R.layout.progresso)
                    .create();
            progressoDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                new Sync(context).run();
                do {
                    Thread.sleep(2500);
                } while (status);
            } catch (InterruptedException e) {
                Log.e("SincronizarActivity", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String retorno) {
            super.onPostExecute(retorno);
            progressoDialog.dismiss();
            context.sendBroadcast(new Intent("AtualizarDataUltimoSinc"));
        }


    }
    void iniciarSinc() {
        new Sincronizando(this).execute();
    }
    @SuppressLint("SetTextI18n")
    private void alertAviso(String mensagem) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(SincronizarActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);
        btnCancela.setVisibility(View.VISIBLE);
        btnOk.setText("Configurar");
        msg.setText(mensagem);

        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent i = new Intent(SincronizarActivity.this, ConfigWebServiceActivity.class);
                startActivity(i);
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        alertDialog.show();
    }

    void setarRecyclerListaItens() {
        if (listaLogSyncCadastros != null) {
            SincAdapter listaAdapter = new SincAdapter(this, listaLogSyncCadastros);
            rvCadastros.setAdapter(listaAdapter);
            if (acrescentaRvCad == 0) {
                rvCadastros.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
                acrescentaRvCad++;
            }
            rvCadastros.setLayoutManager(new LinearLayoutManager(this));
        }

        if (listaLogSyncRegistros != null) {
            SincAdapter listaAdapter = new SincAdapter(this, listaLogSyncRegistros);
            rvRegistros.setAdapter(listaAdapter);
            if (acrescentaRvReg == 0) {
                rvRegistros.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
                acrescentaRvReg++;
            }
            rvRegistros.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void carregarListener() {
        btnSinc.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaURL != null) {
                    iniciarSinc();
                } else {
                    alertAviso("WebService não configurado. Deseja Configurar?");
                }
            }
        });
    }

    private void carregarUltimaSinc() {
        Sinc sinc = null;

        try {
            sinc = CMDatabase.getInstance(this).getSincDAO().buscarAtivo().get();
            listaSync = CMDatabase.getInstance(this).getSincDAO().buscaTodos().get();
            listaURL = CMDatabase.getInstance(this).getConfigWSDAO().buscarUm().get();
            listaLogSyncCadastros = CMDatabase.getInstance(this).getLogSyncDAO().buscarTodasPorTipo("CADASTROS").get();
            listaLogSyncRegistros = CMDatabase.getInstance(this).getLogSyncDAO().buscarTodasPorTipo("REGISTROS").get();
            setarRecyclerListaItens();
            if (sinc != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
                txtUltimoSinc.setText(getResources().getString(R.string.sync_ultimo_sinc,
                        dateFormat.format(sinc.getDataFimSinc())));
            } else {
                txtUltimoSinc.setText(getResources().getString(R.string.sync_ultimo_sinc, "Não Encontrado"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void carregarBotoes() {
        rvRegistros = findViewById(R.id.rvSincRegistros);
        rvCadastros = findViewById(R.id.rvSincCadastros);
        txtUltimoSinc = findViewById(R.id.txtSincCorteMuda);
        btnSinc = findViewById(R.id.btnSincTotal);
    }
}