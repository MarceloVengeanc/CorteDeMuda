package com.example.cortedemudaexataid.telas;

import static android.content.ContentValues.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.telas.adapters.ConsultaAdapter;
import com.example.cortedemudaexataid.telas.listas.ListaConsultaFazenda;
import com.example.cortedemudaexataid.utils.CliqueRecycler;
import com.example.cortedemudaexataid.utils.ConfigGerais;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;
import com.example.cortedemudaexataid.utils.U_Data_Hora;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ConsultaActivity extends AppCompatActivity implements CliqueRecycler {

    private TextView txtListaConsulta;
    private ImageView btnDataInicial, btnDataFinal, btnConsultaFazenda;
    private Button btnFiltrar;
    private EditText editDataInicial, editDataFInal, editConsultaFazenda;
    private CheckBox cbNaoEnviado;
    Resources res;
    private List<Registros> listaRegistrosFiltrada;
    private List<Fazendas> listaFazenda;
    private List<Registros> listaRegistrosSalvos;
    private List<Registros> listaRegistrosVazia = new ArrayList<>();
    private RecyclerView rvConsulta;
    private ConsultaAdapter consultaAdapter;
    private Fazendas codFazenda;
    private Registros registros;
    private String dataEscolhida = "";
    private int acrescenta = 0;
    private boolean datafinal = false, dataInicial = false, editar = false, btnFazendaPressionado = false;
    ActivityResultLauncher<Intent> aguardaResultado = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra("resultadoBuscaFazendas")) {
                            setBuscaFazenda((Fazendas) data.getSerializableExtra("resultadoBuscaFazendas"));
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        carregaBotoes();
        carregaListas();
        carregarTela();
        onCliqueFiltraData();
        cliqueFiltrar();
        setEditConsultaFazenda();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setEditConsultaFazenda() {

        editConsultaFazenda.addTextChangedListener(new TextWatcher() {
            final Handler handler = new Handler();
            Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.postDelayed(() -> {
                    if (!editConsultaFazenda.getText().toString().equalsIgnoreCase("")) {
                        codFazenda = listaFazenda.stream().filter(u -> String.valueOf(u.getCodFazenda()).equalsIgnoreCase(editConsultaFazenda.getText().toString())).findFirst().orElse(null);
                    }
                    if (codFazenda != null) {
                        editConsultaFazenda.setText(codFazenda.getNomeFazenda());
                    }
                }, 2500);
            }
        });
    }

    private void chamaCalendario() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaActivity.this);
        View view = LayoutInflater.from(ConsultaActivity.this).inflate(R.layout.alert_calendario_dialog, viewGroup, false);
        Button btnOk = view.findViewById(R.id.btn_Okdata);
        DatePicker dataPicker = view.findViewById(R.id.dtpicker);
        dataPicker.setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(6));
        builder

                .setView(view);
        AlertDialog alertDialog = builder.create();

        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                String mes1 = "";
                int dia = dataPicker.getDayOfMonth();
                int mes = dataPicker.getMonth() + 1;
                int ano = dataPicker.getYear();

                Calendar dataCompleta = Calendar.getInstance();
                dataCompleta.set(ano, mes, dia);

                if (mes < 10 && dia < 10) {
                    dataEscolhida = "0" + dia + "/0" + mes + "/" + ano;
                } else if (mes < 10) {
                    dataEscolhida = dia + "/0" + mes + "/" + ano;
                } else if (dia < 10) {
                    dataEscolhida = "0" + dia + "/" + mes + "/" + ano;
                } else {
                    dataEscolhida = dia + "/" + mes + "/" + ano;
                }
                Log.i(TAG, dataEscolhida);
                Log.i(TAG, "Cria Intent");
                if (dataInicial) {
                    editDataInicial.setText(dataEscolhida);
                    dataInicial = false;
                } else if (datafinal) {
                    editDataFInal.setText(dataEscolhida);
                    datafinal = false;
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private boolean dataInconsistente() {
        return U_Data_Hora.diferenciarTempo(editDataInicial.getText().toString(), U_Data_Hora.DDMMYYYY,
                editDataFInal.getText().toString(), U_Data_Hora.DDMMYYYY, U_Data_Hora.DD) < 0;
    }

    public void cliqueFiltrar() {
        btnFiltrar.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (dataInconsistente()) {
                    alertAviso("Data inicial à frente da Data final.", false);
                } else {
                    chamarCondicoesFiltrar();
                }
            }
        });
    }

    public void chamarCondicoesFiltrar() {
        Date dataInicial = U_Data_Hora.formatarData(String.valueOf(editDataInicial.getText()), U_Data_Hora.DDMMYYYY);
        Date dataFinal = U_Data_Hora.formatarData(String.valueOf(editDataFInal.getText()), U_Data_Hora.DDMMYYYY);

        filtrarRegistrosPorData(dataInicial, dataFinal);

        filtrarPorFazenda();
        filtrarPorEnviado();
    }

    private void filtrarRegistrosPorData(Date dataInicial, Date dataFinal) {
        if (dataInicial != null && !editDataInicial.getText().toString().isEmpty()
                && dataFinal != null && !editDataFInal.getText().toString().isEmpty()) {
            try {
                String carregaDataInicial = U_Data_Hora.formatarData(
                        U_Data_Hora.formatarData(
                                String.valueOf(editDataInicial.getText()),
                                U_Data_Hora.DDMMYYYY),
                        U_Data_Hora.YYYY_MM_DD);
                String carregaDataFinal = U_Data_Hora.formatarData(
                        U_Data_Hora.formatarData(
                                String.valueOf(editDataFInal.getText()),
                                U_Data_Hora.DDMMYYYY),
                        U_Data_Hora.YYYY_MM_DD);
                listaRegistrosFiltrada = CMDatabase
                        .getInstance(this)
                        .getRegistrosDAO()
                        .buscarRegistrosPorData(ConfigGerais.UsuarioLogado.getNome(),
                                carregaDataInicial
                                , carregaDataFinal
                        ).get();
                listaRegistrosSalvos = listaRegistrosFiltrada;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void filtrarPorFazenda() {
        if (!editConsultaFazenda.getText().toString().equalsIgnoreCase("")) {
            if (listaRegistrosSalvos.stream().anyMatch(u -> u.getNomeFazenda()
                    .equalsIgnoreCase(editConsultaFazenda.getText().toString()))) {
                Registros rf = listaRegistrosSalvos
                        .stream()
                        .filter(RegistroFazenda -> RegistroFazenda.getNomeFazenda()
                                .equalsIgnoreCase(editConsultaFazenda.getText().
                                        toString())).findFirst().orElse(null);
                setRecyclerFiltroConsulta(false);

                listaRegistrosFiltrada = listaRegistrosSalvos.stream()
                        .filter(te -> te.getNomeFazenda().equalsIgnoreCase(editConsultaFazenda.getText().toString())).collect(Collectors.toList());

                if (rf != null) {
                    editConsultaFazenda.setText(String.valueOf(rf.getNomeFazenda()));
                    setRecyclerFiltroConsulta(false);
                } else {
                    editConsultaFazenda.setText("");
                    setRecyclerFiltroConsulta(false);
                }
            } else {
                editConsultaFazenda.setText("");
                listaRegistrosSalvos.clear();
                setRecyclerFiltroConsulta(false);
            }
        } else {
            setRecyclerFiltroConsulta(false);
        }
        listaRegistrosSalvos = listaRegistrosFiltrada;
    }

    private void filtrarPorEnviado() {
        if (cbNaoEnviado.isChecked()) {
            if (listaRegistrosSalvos.stream().anyMatch(u -> u.getEnviado().
                    equalsIgnoreCase("N"))) {
                listaRegistrosFiltrada = listaRegistrosSalvos.stream()
                        .filter(te -> te.getEnviado().equalsIgnoreCase("N")).
                        collect(Collectors.toList());
            } else {
                listaRegistrosFiltrada.clear();
            }
        }
        setRecyclerFiltroConsulta(false);
    }

    private void onCliqueFiltraData() {
        cliqueFiltrarDataInicial();
        cliqueFiltrarDataFinal();
        cliqueBuscaFazenda();
    }

    @SuppressLint("MissingInflatedId")
    private void cliqueFiltrarDataInicial() {
        btnDataInicial.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaCalendario();
                dataInicial = true;
            }
        });

    }

    private void cliqueFiltrarDataFinal() {
        btnDataFinal.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                chamaCalendario();
                datafinal = true;
            }
        });

    }

    private void cliqueBuscaFazenda() {
        btnConsultaFazenda.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaFazenda.size() != 0) {
                    btnFazendaPressionado = true;
                    Intent i = new Intent(ConsultaActivity.this, ListaConsultaFazenda.class);
                    aguardaResultado.launch(i);
                } else {
                    alertAviso("Fazenda(s) Não Encontrada(s). Tente Sincronizar.", false);
                }
            }
        });
    }

    private void setBuscaFazenda(Fazendas fazenda) {
        if (fazenda == null) return;
        editConsultaFazenda.setText(fazenda.getNomeFazenda());
    }

    private void setRecyclerFiltroConsulta(boolean vazio) {
        if (vazio)
            consultaAdapter = new ConsultaAdapter(listaRegistrosVazia, this, res);
        if (!vazio)
            consultaAdapter = new ConsultaAdapter(listaRegistrosFiltrada, this, res);
        consultaAdapter.setCliqueRecycler((CliqueRecycler) this);
        rvConsulta.setAdapter(consultaAdapter);
        if (acrescenta == 0) {
            rvConsulta.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
        rvConsulta.setLayoutManager(new LinearLayoutManager(this));
        if (vazio) txtListaConsulta.setText(res.getString(R.string.lista_registros, 0));
        if (!vazio) setarTotalRequisicoes();
    }

    private void setarTotalRequisicoes() {
        if (listaRegistrosFiltrada != null) {
            txtListaConsulta.setText(res.getString(R.string.lista_registros, listaRegistrosFiltrada.size()));
        } else {
            listaRegistrosFiltrada = new ArrayList<>();
            txtListaConsulta.setText(res.getString(R.string.lista_registros, 0));
        }
    }

    private void carregarTela() {
        editDataInicial.setText(U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY));
        editDataFInal.setText(U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY));
        setRecyclerFiltroConsulta(false);

        try {
            //CONFIGURAR PARA BUSCAR REGISTROS SOMENTE DO USUARIO LOGADO E DATA
            listaRegistrosFiltrada = CMDatabase
                    .getInstance(this)
                    .getRegistrosDAO()
                    .buscarRegistrosPorData(ConfigGerais.UsuarioLogado.getNome(), U_Data_Hora
                                    .formatarData(U_Data_Hora
                                            .formatarData(String.valueOf(editDataInicial.getText()), U_Data_Hora.DDMMYYYY), U_Data_Hora.YYYY_MM_DD)
                            , U_Data_Hora
                                    .formatarData(U_Data_Hora
                                                    .formatarData(String.valueOf(editDataFInal.getText()), U_Data_Hora.DDMMYYYY),
                                            U_Data_Hora.YYYY_MM_DD)).get();
            setRecyclerFiltroConsulta(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregaListas() {
        try {
            listaFazenda = CMDatabase.getInstance(this).getFazendasDAO().buscarTodas().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void carregaBotoes() {
        txtListaConsulta = findViewById(R.id.txtListaRegistros);
        btnDataInicial = findViewById(R.id.btn_consulta_data_inicial);
        btnDataFinal = findViewById(R.id.btn_consulta_data_final);
        btnFiltrar = findViewById(R.id.btn_consulta_filtrar);
        editDataInicial = findViewById(R.id.edit_consulta_data_inicial);
        editDataFInal = findViewById(R.id.edit_consulta_data_final);
        rvConsulta = findViewById(R.id.rv_registros_consulta);
        btnConsultaFazenda = findViewById(R.id.btnPesquisaFazenda);
        editConsultaFazenda = findViewById(R.id.editConsultaFazenda);
        cbNaoEnviado = findViewById(R.id.cbNaoEnviado);
        res = getResources();
    }

    @Override
    public void onItemClick(int position) {
        registros = listaRegistrosFiltrada.get(position);
        if (registros.getEnviado().equalsIgnoreCase("N")) {
            alertAviso("Deseja editar o registro do dia: " + U_Data_Hora.formatarData(
                    registros.getDataRegistro(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY) +
                    "?", true);
        } else {
            alertAviso("Registro já enviado. Não é possível editar.", false);
        }
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    private void alertAviso(String mensagem, boolean trocarMsg) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);
        if (trocarMsg) {
            btnCancela.setVisibility(View.VISIBLE);
            btnOk.setText("Editar");
            editar = true;
            msg.setText(mensagem);
        } else {
            btnOk.setText("Ok");
            msg.setText(mensagem);
        }

        builder.setView(view1);
        trocarMsg = false;
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (editar) {
                    editar = false;
                    Intent i = new Intent(ConsultaActivity.this, ApontamentoCorteMuda.class);
                    i.putExtra("Editar", registros);
                    startActivity(i);
                }
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                editar = false;
                alertDialog.dismiss();
                btnCancela.setVisibility(View.GONE);
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!btnFazendaPressionado) chamarCondicoesFiltrar();
        setarTotalRequisicoes();
        btnFazendaPressionado = false;
    }

}