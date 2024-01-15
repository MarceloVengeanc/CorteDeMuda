package com.example.cortedemudaexataid.telas.configs;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;

import java.util.concurrent.ExecutionException;

public class ConfigWebServiceManualmenteActivity extends AppCompatActivity {

    private EditText endereco;
    private EditText porta;
    private EditText empresa;
    private EditText prioridade;
    private Button btnSalvar;
    private Button btnExcluir;

    private ConfigWS config;
    private String priEditar = "";
    private Context ctx = null;
    private boolean excluir = false, onBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_web_service_manualmente);
        ctx = this;

        inicializarCampos();
        setListenerBtn();

        priEditar = "";
        btnExcluir.setVisibility(View.GONE);
        if (this.getIntent().hasExtra("ConfigWS")) {
            config = (ConfigWS) this.getIntent().getExtras().getSerializable("ConfigWS");
            priEditar = String.valueOf(config.getPrioridade());
            preencheConfigExistente(config);
            btnExcluir.setVisibility(View.VISIBLE);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                chamaBackPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void chamaBackPressed() {
        if (!endereco.getText().toString().equalsIgnoreCase("") ||
                !porta.getText().toString().equalsIgnoreCase("") ||
                !empresa.getText().toString().equalsIgnoreCase("") ||
                !prioridade.getText().toString().equalsIgnoreCase("")) {
            alertAviso("Os campos editados não serão salvos. Deseja Continuar?", false);
        } else {
            finish();
        }
    }

    void excluirConfig() throws ExecutionException, InterruptedException {
        if (config != null) {
            alertAviso("Deseja realmente excluir a configuração?", true);
        }
    }

    void excluirSimNao(boolean excluir) {
        if (excluir) {
            try {
                CMDatabase.getInstance(ctx).getConfigWSDAO().excluir(config).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setResult(Activity.RESULT_OK);
    }

    void salvarConfig() throws ExecutionException, InterruptedException {
        if (endereco.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Campo endereço não informado.", Toast.LENGTH_LONG).show();
            endereco.requestFocus();
            return;
        }
        if (porta.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Campo porta não informado.", Toast.LENGTH_LONG).show();
            porta.requestFocus();
            return;
        }
        if (empresa.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Campo empresa não informado.", Toast.LENGTH_LONG).show();
            empresa.requestFocus();
            return;
        }
        if (prioridade.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Campo prioridade não informado.", Toast.LENGTH_LONG).show();
            prioridade.requestFocus();
            return;
        }

        if (config == null) config = new ConfigWS();
        config.setEmpresa(Integer.parseInt(String.valueOf(empresa.getText())));
        config.setPorta(Integer.parseInt(String.valueOf(porta.getText())));
        config.setPrioridade(Integer.parseInt(String.valueOf(prioridade.getText())));
        config.setUrl(String.valueOf(endereco.getText()));

        boolean achouPriNull = CMDatabase.getInstance(this).getConfigWSDAO().buscarPorPrioridade(config.getPrioridade()).get() == null;
        if (achouPriNull || (!achouPriNull && priEditar.equalsIgnoreCase(String.valueOf(config.getPrioridade())))) {
            CMDatabase.getInstance(this).getConfigWSDAO().inserir(config).get();
            this.setResult(Activity.RESULT_OK);
            Toast.makeText(this, "Dados salvos com sucesso.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Já existe uma configuração com esta prioridade.", Toast.LENGTH_LONG).show();
            prioridade.requestFocus();
        }
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    private void alertAviso(String mensagem, boolean trocarMsg) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfigWebServiceManualmenteActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);
        if (trocarMsg) {
            btnCancela.setVisibility(View.VISIBLE);
            btnOk.setText("Sim");
            excluir = true;
            msg.setText(mensagem);
        } else {
            btnCancela.setVisibility(View.VISIBLE);
            btnOk.setText("Sim");
            msg.setText(mensagem);
            onBack = true;
        }

        builder.setView(view1);
        trocarMsg = false;
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (excluir) {
                    excluirSimNao(true);
                    excluir = false;
                } else if (onBack) {
                    finish();
                    onBack = false;
                }
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                excluir = false;
                excluirSimNao(false);
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        alertDialog.show();
    }

    void setListenerBtn() {
        btnExcluir.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                try {
                    excluirConfig();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSalvar.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                try {
                    salvarConfig();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void inicializarCampos() {
        endereco = findViewById(R.id.etxtConfigWSManualEndereco);
        porta = findViewById(R.id.etxtConfigWSManualPorta);
        empresa = findViewById(R.id.etxtConfigWSManualEmpresa);
        prioridade = findViewById(R.id.etxtConfigWSManualPrioridade);
        btnSalvar = findViewById(R.id.btnSalvarConfigWSManualmente);
        btnExcluir = findViewById(R.id.btnExcluirConfigWSManualmente);
    }

    void preencheConfigExistente(ConfigWS config) {
        endereco.setText(config.getUrl());
        porta.setText(String.valueOf(config.getPorta()));
        empresa.setText(String.valueOf(config.getEmpresa()));
        prioridade.setText(String.valueOf(config.getPrioridade()));
    }
}