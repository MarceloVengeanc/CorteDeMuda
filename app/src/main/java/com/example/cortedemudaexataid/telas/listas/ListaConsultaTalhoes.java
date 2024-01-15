package com.example.cortedemudaexataid.telas.listas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Talhao;
import com.example.cortedemudaexataid.telas.adapters.TalhoesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaConsultaTalhoes extends AppCompatActivity {

    private EditText consultaTalhao;
    private TextView txtListaTalhao;
    private RecyclerView rvTalhao;
    Resources res;
    private int acrescenta, codFazenda;
    private TalhoesAdapter listaTalhaoAdapter;
    private List<Talhao> listaTalhao, listaTalhaoFiltrada;
    private List<Talhao> listaTalhaoVazia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consulta_talhoes);

        Intent i = getIntent();
        codFazenda = (int) i.getSerializableExtra("CodFazenda");
        try {
            listaTalhao = CMDatabase.getInstance(this).getTalhaoDAO().buscaTalhaoFazenda(codFazenda).get();

            carregaBotoes();
            setarCampos();
            setarRecyclerListaFazendas(false);
            setTalhaoEscolhido();
        } catch (Exception e) {
            e.printStackTrace();
        }

        consultaTalhao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTalhaoEscolhido();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setTalhaoEscolhido() {
        try {
            if (!consultaTalhao.getText().toString().equalsIgnoreCase("")) {
                if (Character.isDigit(consultaTalhao.getText().toString().charAt(0))) {
                    if (listaTalhao.stream().anyMatch(cod -> String.valueOf(cod.getCodTalhao()).toLowerCase(Locale.ROOT).contains(consultaTalhao.getText()))) {
                        listaTalhaoFiltrada = listaTalhao.stream().filter(u -> String.valueOf(u.getCodTalhao()).toLowerCase(Locale.ROOT).contains(consultaTalhao.getText())).collect(Collectors.toList());
                        setarRecyclerListaFazendas(false);
                    } else {
                        setarRecyclerListaFazendas(true);
                    }
                }
            } else {
                listaTalhaoFiltrada = listaTalhao;
                setarRecyclerListaFazendas(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultTalhaoEscolhido(Talhao talhao) {
        setResult(Activity.RESULT_OK, this.getIntent().putExtra("resultadoBuscaTalhoes", talhao));
        this.finish();
    }

    private void setarCampos() {
        res = getResources();
        setarTotalItens();
    }

    private void setarTotalItens() {
        if (listaTalhaoFiltrada != null) {
            txtListaTalhao.setText(res.getString(R.string.lista_talhoes, listaTalhaoFiltrada.size()));
        } else {
            listaTalhaoFiltrada = new ArrayList<>();
            txtListaTalhao.setText(res.getString(R.string.lista_fazendas, 0));
        }
    }

    private void setarRecyclerListaFazendas(boolean vazio) {
        if (vazio)
            listaTalhaoAdapter = new TalhoesAdapter(listaTalhaoVazia, this::setResultTalhaoEscolhido);
        if (!vazio)
            listaTalhaoAdapter = new TalhoesAdapter(listaTalhaoFiltrada, this::setResultTalhaoEscolhido);
        rvTalhao.setAdapter(listaTalhaoAdapter);
        if (acrescenta == 0) {
            rvTalhao.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvTalhao.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaTalhao.setText(res.getString(R.string.lista_fazendas, 0));
        if (!vazio) setarTotalItens();
    }

    public void carregaBotoes() {
        consultaTalhao = findViewById(R.id.edit_talhaoCodigo);
        txtListaTalhao = findViewById(R.id.txt_listaTalhoes);
        rvTalhao = findViewById(R.id.rv_ListaTalhao);
    }
}