package com.example.cortedemudaexataid.telas;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.example.cortedemudaexataid.banco.modelos.Talhao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MenuPrincipalCorteMuda extends AppCompatActivity {

    private Button btnApontamento, btnConsulta, btnSinc;
    private TextView txtVersao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carregaBotoes();
        setaBotoes();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };

    }
    private void setaBotoes() {
        btnApontamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuPrincipalCorteMuda.this, ApontamentoCorteMuda.class);
                startActivity(i);
            }
        });

        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuPrincipalCorteMuda.this, ConsultaActivity.class);
                startActivity(i);
            }
        });

        btnSinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuPrincipalCorteMuda.this, SincronizarActivity.class);
                startActivity(i);
            }
        });
    }

    public void criaBanco() {

        CMDatabase db = CMDatabase.getInstance(this.getApplicationContext());
        List<Fazendas> fazendas = new ArrayList<>();
        List<Talhao> talhoes = new ArrayList<>();
        List<Funcionario> funcionarios = new ArrayList<>();

        Funcionario funcionario = new Funcionario();
        funcionario.setMatricula(1001);
        funcionario.setNome("Teste");
        funcionario.setLogin("teste1");
        funcionario.setSenha("exata");
        funcionarios.add(funcionario);

        Fazendas fazenda = new Fazendas();
        fazenda.setNomeFazenda("Vale Verde");
        fazenda.setCodFazenda(12455);
        fazendas.add(fazenda);

        Fazendas fazenda1 = new Fazendas();
        fazenda1.setNomeFazenda("Laranjeira");
        fazenda1.setCodFazenda(78945);
        fazendas.add(fazenda1);

        Fazendas fazenda2 = new Fazendas();
        fazenda2.setNomeFazenda("Podoado");
        fazenda2.setCodFazenda(85274);
        fazendas.add(fazenda2);

        Fazendas fazenda3 = new Fazendas();
        fazenda3.setNomeFazenda("Lagoa Lagoa");
        fazenda3.setCodFazenda(45683);
        fazendas.add(fazenda3);

        Talhao talhao = new Talhao();
        talhao.setCodTalhao(1111);
        talhao.setArea(20);
        talhao.setCodFazenda(12455);
        talhoes.add(talhao);

        Talhao talhao1 = new Talhao();
        talhao1.setCodTalhao(1245);
        talhao1.setArea(50);
        talhao1.setCodFazenda(12455);
        talhoes.add(talhao1);

        Talhao talhao9 = new Talhao();
        talhao9.setCodTalhao(9999);
        talhao9.setArea(300);
        talhao9.setCodFazenda(12455);
        talhoes.add(talhao9);

        Talhao talhao10 = new Talhao();
        talhao10.setCodTalhao(1000);
        talhao10.setArea(400);
        talhao10.setCodFazenda(12455);
        talhoes.add(talhao10);

        Talhao talhao2 = new Talhao();
        talhao2.setCodTalhao(6589);
        talhao2.setArea(30);
        talhao2.setCodFazenda(85274);
        talhoes.add(talhao2);

        Talhao talhao7 = new Talhao();
        talhao7.setCodTalhao(7777);
        talhao7.setArea(60);
        talhao7.setCodFazenda(85274);
        talhoes.add(talhao7);

        Talhao talhao8 = new Talhao();
        talhao8.setCodTalhao(8888);
        talhao8.setArea(140);
        talhao8.setCodFazenda(85274);
        talhoes.add(talhao8);

        Talhao talhao3 = new Talhao();
        talhao3.setCodTalhao(6699);
        talhao3.setArea(80);
        talhao3.setCodFazenda(45683);
        talhoes.add(talhao3);

        Talhao talhao4 = new Talhao();
        talhao4.setCodTalhao(3333);
        talhao4.setArea(50);
        talhao4.setCodFazenda(45683);
        talhoes.add(talhao4);

        Talhao talhao5 = new Talhao();
        talhao5.setCodTalhao(4444);
        talhao5.setArea(120);
        talhao5.setCodFazenda(45683);
        talhoes.add(talhao5);

        Talhao talhao6 = new Talhao();
        talhao6.setCodTalhao(555);
        talhao6.setArea(140);
        talhao6.setCodFazenda(45683);
        talhoes.add(talhao6);

        try {
            CMDatabase.getInstance(this).getFazendasDAO().inserir(fazendas).get();
            CMDatabase.getInstance(this).getTalhaoDAO().inserir(talhoes).get();
            CMDatabase.getInstance(this).getFuncionarioDAO().inserir(funcionarios).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void carregaBotoes() {
        txtVersao = findViewById(R.id.txtVersao);
        try {
            txtVersao.setText(this.getString(R.string.msg_versao_app, this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            txtVersao.setText("");
            e.printStackTrace();
        }
        btnApontamento = findViewById(R.id.btnApontamento);
        btnConsulta = findViewById(R.id.btnConsulta);
        btnSinc = findViewById(R.id.btnSinc);
        txtVersao = findViewById(R.id.txtVersao);
    }
}