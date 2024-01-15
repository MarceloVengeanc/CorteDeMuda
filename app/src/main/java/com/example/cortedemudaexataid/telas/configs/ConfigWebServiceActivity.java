package com.example.cortedemudaexataid.telas.configs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.telas.ApontamentoCorteMuda;
import com.example.cortedemudaexataid.telas.ConsultaActivity;
import com.example.cortedemudaexataid.telas.adapters.ConfigWSAdapter;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;
import com.google.zxing.integration.android.IntentIntegrator;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConfigWebServiceActivity extends AppCompatActivity {

    private Button btnLerQRCode;
    private Button btnConfigManualmente;
    private RecyclerView rvListaConfigsWS;
    private List<ConfigWS> ListaConfigs;

    ActivityResultLauncher<Intent> AguardaResultado = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        carregarDados();
                        setarRecyclerListaItens();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_web_service);

        try {
            inicializarCampos();
            setarListeners();
            carregarDados();
            setarRecyclerListaItens();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void carregarDados() throws ExecutionException, InterruptedException {
        ListaConfigs = CMDatabase.getInstance(this).getConfigWSDAO().buscarTodas().get();
    }

    void setarListeners() {
        btnConfigManualmente.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent i = new Intent(v.getContext(), ConfigWebServiceManualmenteActivity.class);
                AguardaResultado.launch(i);
            }
        });

        btnLerQRCode.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator(ConfigWebServiceActivity.this);
                scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                scanIntegrator.setCameraId(0);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.setBeepEnabled(true);
                scanIntegrator.setPrompt("Aponte a câmera para o QRCode para ser lido.");
                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String conteudo = data.getStringExtra("SCAN_RESULT");
            if (conteudo != null) {
                if (!conteudo.equals("")) {
                    String decodificado = new String(Base64.decode(conteudo, Base64.DEFAULT), StandardCharsets.UTF_8);
                    CMDatabase.getInstance(this).getConfigWSDAO().limpar();
                    ListaConfigs.clear();

                    String[] configs = decodificado.replaceAll("\\s+", "").split(":");
                    String ip = configs[3].replace("//", "");
                    String porta = configs[4].replace("}", "");
                    String empresa = configs[1].replace(",enderecoPadrao", "");

                    ConfigWS wsc = new ConfigWS();
                    wsc.setUrl(ip);
                    wsc.setPorta(Integer.valueOf(porta));
                    wsc.setPrioridade(Integer.valueOf(empresa));
                    wsc.setEmpresa(Integer.valueOf(empresa));
                    CMDatabase.getInstance(this).getConfigWSDAO().inserir(wsc);
                    ListaConfigs.add(wsc);
                    Thread.sleep(1000);
//                            }
//                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setarRecyclerListaItens();
    }


    @SuppressLint("SetTextI18n")
    void abrirEscolhido(ConfigWS config) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfigWebServiceActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);

        btnCancela.setVisibility(View.VISIBLE);
        btnOk.setText("Editar");
        msg.setText("Deseja editar o Web Service Selecionado?");

        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent i = new Intent(v.getContext(), ConfigWebServiceManualmenteActivity.class);
                i.putExtra("ConfigWS", config);
                AguardaResultado.launch(i);
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

    void inicializarCampos() {
        btnLerQRCode = findViewById(R.id.btnLerQRCode);
        btnConfigManualmente = findViewById(R.id.btnConfigWSInseirManualmente);
        rvListaConfigsWS = findViewById(R.id.rv_configWS);
    }

    void setarRecyclerListaItens() {
        if (ListaConfigs != null) {
            ConfigWSAdapter listaAdapter = new ConfigWSAdapter(this, ListaConfigs, this::abrirEscolhido);
            rvListaConfigsWS.setAdapter(listaAdapter);
            rvListaConfigsWS.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}