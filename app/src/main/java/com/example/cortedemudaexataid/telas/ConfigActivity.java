package com.example.cortedemudaexataid.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Coletor;
import com.example.cortedemudaexataid.telas.configs.ConfigColetorActivity;
import com.example.cortedemudaexataid.telas.configs.ConfigWebServiceActivity;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class ConfigActivity extends AppCompatActivity {
    private TextView txtColetor, txtVersao;
    private Button btnWebService;
    private Button btnConfigColetor;
    Resources res;
    private Coletor coletor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setarCampos();
        setarListeners();
        carregaBanco();
        carregaColetor();
    }

    private void setarListeners() {

        btnConfigColetor.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent i = new Intent(ConfigActivity.this, ConfigColetorActivity.class);
                startActivity(i);
            }
        });

        btnWebService.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                Intent i = new Intent(ConfigActivity.this, ConfigWebServiceActivity.class);
                startActivity(i);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void carregaColetor() {
        if (coletor != null) {
            txtColetor.setText(res.getString(R.string.coletor, coletor.getNumero().toString()));
        } else {
            txtColetor.setText(getResources().getString(R.string.coletor, "Não Configurado"));
        }
    }

    private void carregaBanco() {
        try {
            coletor = CMDatabase.getInstance(this).getColetorDAO().buscarAtivo().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaBanco();
        carregaColetor();
    }

    private void setarCampos() {
        res = getResources();
        btnWebService = findViewById(R.id.btnConfigWS);
        btnConfigColetor = findViewById(R.id.btnConfigColetor);
        txtColetor = findViewById(R.id.txtColetor4);
        txtVersao = findViewById(R.id.versao_app4);
        try {
            txtVersao.setText(this.getString(R.string.msg_versao_app, this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            txtVersao.setText("");
            e.printStackTrace();
        }
    }

}