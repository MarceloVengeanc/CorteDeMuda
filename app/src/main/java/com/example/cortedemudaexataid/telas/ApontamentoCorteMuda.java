package com.example.cortedemudaexataid.telas;

import static android.app.PendingIntent.getActivity;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Coletor;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.banco.modelos.Talhao;
import com.example.cortedemudaexataid.telas.adapters.RegistrosAdapter;
import com.example.cortedemudaexataid.telas.listas.ListaConsultaFazenda;
import com.example.cortedemudaexataid.telas.listas.ListaConsultaTalhoes;
import com.example.cortedemudaexataid.utils.ConfigGerais;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;
import com.example.cortedemudaexataid.utils.U_Data_Hora;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ApontamentoCorteMuda extends AppCompatActivity {

    private Button btnAdicionar, btnSalvar;
    private ImageButton btnData, btnFazenda, btnTalhao;
    private TextView txtRegistros;
    private EditText editData, editFazenda, editTalhao, editNomeFazenda, editArea, editEstimativa;
    private RadioButton rbTotal, rbParcial, rbUltimoPorConta, rbUltimoPorLiquidado;
    private RecyclerView rvRegistros;
    private Calendar dataCompleta, dataAtual, dataEscolhida;
    private List<Fazendas> listaTodasFazendas;
    private List<Talhao> listaTalhaoFazendas;
    private List<Registros> listaRegistros = new ArrayList<>();
    private Fazendas fazendaEscolhida;
    private Talhao talhaoEscolhido;
    private Coletor coletor;
    private RegistrosAdapter registrosAdapter;
    private Registros registroEditar;
    private Context ctx;
    private Resources res;
    private String dataSelecionada = "";
    private int acrescenta = 0, area = 0, contSalvar = 0;
    private boolean trocarMsg = false, editar = false, finalizar = false, possuiPonto = false;

    ActivityResultLauncher<Intent> aguardaResultado = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.hasExtra("resultadoBuscaFazendas")) {
                            setBuscaFazenda((Fazendas) data.getSerializableExtra("resultadoBuscaFazendas"));
                        } else if (data.hasExtra("resultadoBuscaTalhoes")) {
                            setBuscaTalhao((Talhao) data.getSerializableExtra("resultadoBuscaTalhoes"));
                        }
                    }
                }
            }
    );

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_corte_muda);
        Intent i = getIntent();
        registroEditar = (Registros) i.getSerializableExtra("Editar");

        ctx = this;
        carregaBotoes();
        setarData();
        setarRecyclerListaRegistros();
        setarBotoes();
        carregaBanco();
        listenerEditText();
        if (registroEditar != null) {
            editData.setText(U_Data_Hora.formatarData(registroEditar.getDataRegistro(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY));
            editFazenda.setText("Faz: " + registroEditar.getCodFazenda());
            editNomeFazenda.setText(registroEditar.getNomeFazenda());
            editTalhao.setText("Talhão: " + registroEditar.getCodTalhao());
            @SuppressLint("DefaultLocale") String areaEditFormat = String.format("%.2f", registroEditar.getArea()).replace(",", ".");
            editArea.setText(areaEditFormat);
            @SuppressLint("DefaultLocale") String estimatvaEditFormat = String.format("%.2f", registroEditar.getEstimativa()).replace(",", ".");
            editEstimativa.setText(estimatvaEditFormat);
            if (!registroEditar.getTotalOuParcial().equalsIgnoreCase("Total")) {
                rbParcial.setChecked(true);
            }
            if (registroEditar.getUltimoCorte().equalsIgnoreCase("Por Conta")) {
                rbUltimoPorConta.setChecked(true);
            }
            editar = true;
            btnAdicionar.setVisibility(View.INVISIBLE);
            rvRegistros.setVisibility(View.INVISIBLE);
            txtRegistros.setVisibility(View.INVISIBLE);
            fazendaEscolhida = listaTodasFazendas.stream().filter(u -> u.getCodFazenda() == registroEditar.getCodFazenda()).findFirst().orElse(null);
            talhaoEscolhido = listaTalhaoFazendas.stream().filter(u -> u.getCodTalhao() == registroEditar.getCodTalhao()).findFirst().orElse(null);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (listaRegistros.size() != 0) {
                    finalizar = true;
                    alertAviso("Os registros inseridos não serão salvos. Deseja continuar?", true, true);
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void listenerEditText() {
        editEstimativa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.contains(".")) {
                    possuiPonto = true;
                } else {
                    possuiPonto = false;
                }

                if (possuiPonto) {

                    int dotIndex = text.indexOf(".");
                    int maxLength = dotIndex + 3;
                    editEstimativa.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                } else {
                    editEstimativa.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }
            }
        });
        editArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.contains(".")) {
                    possuiPonto = true;
                } else {
                    possuiPonto = false;
                }

                if (possuiPonto) {

                    int dotIndex = text.indexOf(".");
                    int maxLength = dotIndex + 3;
                    editArea.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                } else {
                    editArea.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                }
            }
        });
    }

    private void chamaVerificaCampos() {
        if (editVazio(editData, "Data")) return;
        if (editVazio(editFazenda, "Fazenda")) return;
        if (editVazio(editTalhao, "Talhão")) return;
        if (editVazio(editArea, "Área")) return;
        if (editVazio(editEstimativa, "Estimativa")) return;

        String estimativaText = editEstimativa.getText().toString();
        if (estimativaText.matches("^0+$")) {
            alertAviso("Campo Estimativa não pode ser 0.", true, false);
            return;
        }

        float areaModificada = Float.parseFloat(editArea.getText().toString());
        if (areaModificada > talhaoEscolhido.getArea()) {
            alertAviso("Área maior que a do Talhão!", true, false);
        } else {
            // ADICIONA NA LISTA DE REGISTROS
            salvaRegistroNaLista();
        }
    }

    private boolean editVazio(EditText editText, String fieldName) {
        if (editText.getText().toString().trim().isEmpty()) {
            alertAviso(fieldName, false, false);
            editText.requestFocus();
            return true;
        }
        return false;
    }

    private void salvaRegistroNaLista() {
        if (!editar) {
            Registros registros = new Registros();
            registros.setDataRegistro(!dataSelecionada.equalsIgnoreCase("") ? dataSelecionada : U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD));
            registros.setCodFazenda(fazendaEscolhida.getCodFazenda());
            registros.setNomeFazenda(fazendaEscolhida.getNomeFazenda());
            registros.setCodTalhao(talhaoEscolhido.getCodTalhao());
            registros.setArea(Float.parseFloat(editArea.getText().toString()));
            registros.setEstimativa(Float.parseFloat(editEstimativa.getText().toString()));
            String resultado = (rbTotal.isChecked()) ? "Total" : "Parcial";
            registros.setTotalOuParcial(resultado);
            listaRegistros.add(registros);
            limparCampos();
            setarRecyclerListaRegistros();
        } else {
            if (!dataSelecionada.equalsIgnoreCase("")) {
                registroEditar.setDataRegistro(dataSelecionada);
            } else {
                registroEditar.setDataRegistro(registroEditar.getDataRegistro());
            }
            registroEditar.setCodFazenda(fazendaEscolhida.getCodFazenda());
            registroEditar.setNomeFazenda(editNomeFazenda.getText().toString());
            registroEditar.setCodTalhao(talhaoEscolhido.getCodTalhao());
            registroEditar.setArea(Float.parseFloat(editArea.getText().toString()));
            registroEditar.setEstimativa(Float.parseFloat(editEstimativa.getText().toString()));
            String resultadoEditar = (rbTotal.isChecked()) ? "Total" : "Parcial";
            registroEditar.setTotalOuParcial(resultadoEditar);
            salvarRegistros();
        }
    }

    private void salvarRegistros() {
        CMDatabase db = CMDatabase.getInstance(this.getApplicationContext());

        try {
            if (!editar) {
                db.getRegistrosDAO().inserir(listaRegistros).get();
            } else {
                registroEditar.setDataRegistroSalvo(U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD_HH_MM_SS));
                String resultadoEditar = (rbUltimoPorConta.isChecked()) ? "Por Conta" : "Por Liquidado";
                registroEditar.setUltimoCorte(resultadoEditar);
                db.getRegistrosDAO().atualizar(registroEditar).get();
                finalizar = true;
                alertAviso("Registro alterado com sucesso!", true, false);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        listaRegistros.clear();
        setarRecyclerListaRegistros();
    }

    private void setarBotoes() {
        btnFazenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaTodasFazendas.size() != 0) {
                    Intent i = new Intent(ApontamentoCorteMuda.this, ListaConsultaFazenda.class);
                    aguardaResultado.launch(i);
                } else {
                    alertAviso("Fazenda(s) não encontrada(s). Tente sincronizar", true, false);
                }
            }
        });

        btnTalhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editFazenda.getText().toString().equalsIgnoreCase("")) {
                    if (listaTalhaoFazendas.stream().anyMatch(u -> u.getCodFazenda() == fazendaEscolhida.getCodFazenda())) {

                        Intent i = new Intent(ApontamentoCorteMuda.this, ListaConsultaTalhoes.class);
                        i.putExtra("CodFazenda", fazendaEscolhida.getCodFazenda());
                        aguardaResultado.launch(i);
                    } else {
                        alertAviso("Nenhum talhão encontrado para fazenda selecionada!", true, false);
                    }
                } else {
                    alertAviso("Fazenda", false, false);
                }
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaVerificaCampos();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!editar) {
                    if (listaRegistros.size() != 0) {
                        for (int i = 0; i < listaRegistros.size(); i++) {
                            listaRegistros.get(i).setDataRegistroSalvo(U_Data_Hora.retornaData(U_Data_Hora.DDMMYYYY_HHMMSS));
                            String resultadoSalvar = (rbUltimoPorConta.isChecked()) ? "Por Conta" : "Por Liquidado";
                            listaRegistros.get(i).setUltimoCorte(resultadoSalvar);
                            listaRegistros.get(i).setEnviado("N");
                            listaRegistros.get(i).setColetor(coletor.getNumero());
                            listaRegistros.get(i).setMatriculaFuncionario(ConfigGerais.UsuarioLogado.getTurma());
                            listaRegistros.get(i).setNomeFuncionario(ConfigGerais.UsuarioLogado.getNome());
                        }
                        salvarRegistros();
                        alertAviso("Registro(s) salvo(s) com sucesso!", true, false);

                    } else {
                        alertAviso("Nenhum registro adicionado!", true, false);
                    }
                } else {
                    chamaVerificaCampos();
                }
            }
        });
    }

    private void setarRecyclerListaRegistros() {
        //configurar Adapter
        registrosAdapter = new RegistrosAdapter(listaRegistros, ctx, res);
        //configurar Recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvRegistros.setLayoutManager(layoutManager);
        rvRegistros.setHasFixedSize(true);
        rvRegistros.setAdapter(registrosAdapter);
        if (acrescenta == 0) {
            rvRegistros.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
            acrescenta++;
        }
    }

    private void setarData() {
        editData.setText(U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY));
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApontamentoCorteMuda.this);
                builder.setTitle("Senha");
                final EditText txtSenha = new EditText(ApontamentoCorteMuda.this);
                txtSenha.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                txtSenha.requestFocus();
                builder.setView(txtSenha);
                builder.setPositiveButton(("Ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (txtSenha.getText().toString().equalsIgnoreCase("17395")) {
                            chamaAlertCalendario();
                        } else if (txtSenha.getText().toString().equalsIgnoreCase("")) {
                            alertAviso("Informe a Senha!", true, false);
                        } else {
                            alertAviso("Senha incorreta!", true, false);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void chamaAlertCalendario() {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ApontamentoCorteMuda.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_calendario_dialog, null);

        //DECLARA VARIAVEIS
        Button btnOk;
        DatePicker datePicker;
        btnOk = view1.findViewById(R.id.btn_Okdata);
        datePicker = view1.findViewById(R.id.dtpicker);
        datePicker.setMinDate(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(6));
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                int ano = datePicker.getYear();
                int mes = datePicker.getMonth() + 1;
                int dia = datePicker.getDayOfMonth();

                // Criar um objeto Calendar para a data selecionada
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(ano, (mes - 1), dia);

                // Obter a data atual do sistema
                Calendar currentCalendar = Calendar.getInstance();


                if (selectedCalendar.before(currentCalendar) || selectedCalendar.equals(currentCalendar)) {
                    if (mes < 10 && dia < 10) {
                        editData.setText("0" + dia + "/0" + mes + "/" + ano);
                        dataSelecionada = ano + "-0" + mes + "-0" + dia;
                        alertDialog.dismiss();
                    } else if (mes < 10) {
                        editData.setText(dia + "/0" + mes + "/" + ano);
                        dataSelecionada = ano + "-0" + mes + "-" + dia;
                        alertDialog.dismiss();
                    } else if (dia < 10) {
                        editData.setText("0" + dia + "/" + mes + "/" + ano);
                        dataSelecionada = ano + "-" + mes + "-0" + dia;
                        alertDialog.dismiss();
                    } else {
                        editData.setText(dia + "/" + mes + "/" + ano);
                        dataSelecionada = ano + "-" + mes + "-" + dia;
                        alertDialog.dismiss();
                    }

                } else if (selectedCalendar.after(currentCalendar)) {
                    Toast.makeText(ApontamentoCorteMuda.this, "A data é posterior à data atual", Toast.LENGTH_SHORT).show();
                }
            }

        });
        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaFazenda(Fazendas fazenda) {
        if (fazenda == null) return;
        editNomeFazenda.setText(fazenda.getNomeFazenda());
        editFazenda.setText("Faz: " + fazenda.getCodFazenda());
        editTalhao.setText("");
        editArea.setText("");
        fazendaEscolhida = fazenda;
    }

    @SuppressLint("SetTextI18n")
    private void setBuscaTalhao(Talhao talhao) {
        if (talhao == null) return;
        @SuppressLint("DefaultLocale") String areaFormat = String.format("%.2f", talhao.getArea()).replace(",", ".");
        editTalhao.setText("Talhão: " + talhao.getCodTalhao());
        editArea.setText(areaFormat);
        talhaoEscolhido = talhao;
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    private void alertAviso(String mensagem, boolean trocarMsg, boolean sair) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(ApontamentoCorteMuda.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk, btnCancela;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
        msg = view1.findViewById(R.id.txtmsg);
        if (sair) {
            btnCancela.setVisibility(View.VISIBLE);
            btnOk.setText("Sim");
        }
        if (trocarMsg) {
            msg.setText(mensagem);
        } else {
            msg.setText("Campo " + mensagem + " Vazio!");
        }

        builder.setView(view1);
        trocarMsg = false;
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalizar) {
                    finish();
                }
                alertDialog.dismiss();
            }
        });
        btnCancela.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                editar = false;
                finalizar = false;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void limparCampos() {
        editFazenda.setText("");
        editNomeFazenda.setText("");
        editTalhao.setText("");
        editArea.setText("");
        editEstimativa.setText("");
        rbTotal.setChecked(true);
    }

    private void carregaBanco() {
        try {
            listaTodasFazendas = CMDatabase.getInstance(this).getFazendasDAO().buscarTodas().get();
            listaTalhaoFazendas = CMDatabase.getInstance(this).getTalhaoDAO().buscarTodos().get();
            coletor = CMDatabase.getInstance(this).getColetorDAO().buscarAtivo().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongViewCast")
    private void carregaBotoes() {
        btnAdicionar = findViewById(R.id.btn_adicionar);
        btnSalvar = findViewById(R.id.btn_Salvar);
        btnData = findViewById(R.id.btnBuscaData);
        btnFazenda = findViewById(R.id.btnBuscaFazenda);
        btnTalhao = findViewById(R.id.btnBuscaTalhao);
        editData = findViewById(R.id.editData);
        editFazenda = findViewById(R.id.editFazenda);
        editTalhao = findViewById(R.id.editTalhao);
        editNomeFazenda = findViewById(R.id.editConsultaFazenda);
        editArea = findViewById(R.id.editArea);
        editEstimativa = findViewById(R.id.editEstimativa);
        rbTotal = findViewById(R.id.rbTotal);
        rbParcial = findViewById(R.id.rbParcial);
        rbUltimoPorConta = findViewById(R.id.rbUltimoCorteConta);
        rbUltimoPorLiquidado = findViewById(R.id.rbUltimoCorteLiquidado);
        rvRegistros = findViewById(R.id.rvRegistro);
        txtRegistros = findViewById(R.id.txtRegistros);
        rbTotal.setChecked(true);
        rbUltimoPorConta.setChecked(true);
        res = getResources();
    }
}