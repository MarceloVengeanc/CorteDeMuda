package com.example.cortedemudaexataid.telas.listas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.telas.adapters.FazendasAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaConsultaFazenda extends AppCompatActivity {

    private EditText consultaFazenda;
    private TextView txtListaFazenda;
    private RecyclerView rvFazenda;
    Resources res;
    private int acrescenta;
    private FazendasAdapter listaFazendaAdapter;
    private List<Fazendas> listaFazendas, listaFazendasFiltrada;
    private List<Fazendas> listaFazendasVazia = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consulta_fazenda);
        try {
            listaFazendas = CMDatabase.getInstance(this).getFazendasDAO().buscarTodas().get();

            carregaBotoes();
            setarCampos();
            setarRecyclerListaFazendas(false);
            setFazendaEscolhida();
        } catch (Exception e) {
            e.printStackTrace();
        }

        consultaFazenda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setFazendaEscolhida();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setFazendaEscolhida() {
        try {
            if (!consultaFazenda.getText().toString().equalsIgnoreCase("")) {
                //BUSCA POR NOME
                if (!Character.isDigit(consultaFazenda.getText().toString().charAt(0))) {
                    if (listaFazendas.stream().anyMatch(item -> item.getNomeFazenda().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaFazenda.getText())))) {
                        listaFazendasFiltrada = listaFazendas.stream().filter(itens -> itens.getNomeFazenda().toLowerCase(Locale.ROOT).contains(String.valueOf(consultaFazenda.getText()))).collect(Collectors.toList());
                        setarRecyclerListaFazendas(false);
                    } else {
                        setarRecyclerListaFazendas(true);
                    }
                    //BUSCA POR CODIGO
                } else if (Character.isDigit(consultaFazenda.getText().toString().charAt(0))) {
                    if (listaFazendas.stream().anyMatch(cod -> String.valueOf(cod.getCodFazenda()).toLowerCase(Locale.ROOT).contains(consultaFazenda.getText()))) {
                        listaFazendasFiltrada = listaFazendas.stream().filter(u -> String.valueOf(u.getCodFazenda()).toLowerCase(Locale.ROOT).contains(consultaFazenda.getText())).collect(Collectors.toList());
                        setarRecyclerListaFazendas(false);
                    } else {
                        setarRecyclerListaFazendas(true);
                    }
                }
            } else {
                listaFazendasFiltrada = listaFazendas;
                setarRecyclerListaFazendas(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResultFazendaEscolhida(Fazendas fazendas) {
        setResult(Activity.RESULT_OK, this.getIntent().putExtra("resultadoBuscaFazendas", fazendas));
        this.finish();
    }

    private void setarCampos() {
        res = getResources();
        setarTotalItens();
    }

    private void setarTotalItens() {
        if (listaFazendasFiltrada != null) {
            txtListaFazenda.setText(res.getString(R.string.lista_fazendas, listaFazendasFiltrada.size()));
        } else {
            listaFazendasFiltrada = new ArrayList<>();
            txtListaFazenda.setText(res.getString(R.string.lista_fazendas, 0));
        }
    }

    private void setarRecyclerListaFazendas(boolean vazio) {
        if (vazio)
            listaFazendaAdapter = new FazendasAdapter(listaFazendasVazia, this::setResultFazendaEscolhida);
        if (!vazio)
            listaFazendaAdapter = new FazendasAdapter(listaFazendasFiltrada, this::setResultFazendaEscolhida);
        rvFazenda.setAdapter(listaFazendaAdapter);
        if (acrescenta == 0) {
            rvFazenda.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvFazenda.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaFazenda.setText(res.getString(R.string.lista_fazendas, 0));
        if (!vazio) setarTotalItens();
    }

    public void carregaBotoes() {
        consultaFazenda = findViewById(R.id.edit_fazendaCodigo);
        txtListaFazenda = findViewById(R.id.txt_listaFazenda);
        rvFazenda = findViewById(R.id.rv_ListaFazenda);
    }
}