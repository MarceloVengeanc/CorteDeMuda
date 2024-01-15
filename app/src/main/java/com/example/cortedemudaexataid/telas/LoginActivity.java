package com.example.cortedemudaexataid.telas;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cortedemudaexataid.CMDatabase;
import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Coletor;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.banco.modelos.Funcionario;
import com.example.cortedemudaexataid.utils.ConfigGerais;
import com.example.cortedemudaexataid.utils.OnOneOffClickListener;
import com.example.cortedemudaexataid.utils.U_Data_Hora;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private TextView versao, txtColetor;
    private EditText editMatricula, editUsuario, editSenha;
    private Button btnLogin, btnSync, btnconfig;
    private ConfigWS listaURL;
    private List<Funcionario> listaFuncionarios;
    private Funcionario usuarioLogin;
    private Coletor coletor;
    Resources res;

    ActivityResultLauncher<Intent> AguardaResultadoForcaAuto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

    });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Intent serviceIntent = new Intent(this, SyncService.class);
//        startService(serviceIntent);

        iniciarCampos();
        setarListener();
        carregaBanco();
        carregarColetor();
    }

    // MÉTODO QUE CARREGA BANCO WS E SYNC, E FAZ A VERIFICAÇÃO

    void setarListener() {
        editMatricula.addTextChangedListener(new TextWatcher() {
            final Handler handler = new Handler();
            Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                handler.postDelayed(() -> {
//                else {
//                    matricula.setText("");
//                    usuario.setText("");
//                }
//                }, 3000);
                if (!editMatricula.getText().toString().equalsIgnoreCase("")) {
                    usuarioLogin = listaFuncionarios.stream().filter(u -> u.getMatricula() == Integer.parseInt(editMatricula.getText().toString())).findFirst().orElse(null);
                }
                if (usuarioLogin != null) {
                    editUsuario.setText(usuarioLogin.getNome());
                } else {
                    editUsuario.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                handler.removeCallbacks(runnable);
                if (editMatricula.getText().toString().equalsIgnoreCase("")) {
                    usuarioLogin = null;
                    editUsuario.setText("");
                }
            }
        });
        btnLogin.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                try {
                    if (!editUsuario.getText().toString().equalsIgnoreCase("") &
                            !editMatricula.getText().toString().equalsIgnoreCase("")) {
                        validarLogin();
                    } else {
                        alertAviso("Campo Usuário Obrigatório. Digite a matrícula.", false);
                        editMatricula.requestFocus();
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSync.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (listaURL != null) {
                    Intent i = new Intent(v.getContext(), SincronizarActivity.class);
                    startActivity(i);
                } else {
                    alertAviso("É necessário que seja configurado WEB SERVICE.", false);
                }
            }
        });

        btnconfig.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Senha");
                final EditText txtSenha = new EditText(LoginActivity.this);
                txtSenha.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                txtSenha.requestFocus();
                builder.setView(txtSenha);
                builder.setPositiveButton(("Ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (txtSenha.getText().toString().equalsIgnoreCase("17395")) {
                            Intent i = new Intent(LoginActivity.this, ConfigActivity.class);
                            startActivity(i);
                        } else if (txtSenha.getText().toString().equalsIgnoreCase("")) {
                            alertAviso("Informe a Senha!", false);
                        } else {
                            alertAviso("Senha incorreta!", false);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }
    // MÉTODO QUE VALIDA LOGIN DE ACORDO COM DADO USER E SENHA DO BANCO

    void validarLogin() throws ExecutionException, InterruptedException {
        CMDatabase db = CMDatabase.getInstance(this.getApplicationContext());
        String user = "";
        if (usuarioLogin != null) {
            user = usuarioLogin.getLogin();
        } else {
            alertAviso("Campo Matrícula diferente do usuário.", false);
        }
        String pass = editSenha.getText().toString();

        if (user.equalsIgnoreCase("")) {
            alertAviso("Campo Usuário é obrigatório.", false);
            editUsuario.requestFocus();
            return;
        } else if (pass.equalsIgnoreCase("")) {
            alertAviso("Campo Senha vazio! Insira uma senha válida.", false);
            editSenha.requestFocus();
            return;
        }

        Funcionario u = CMDatabase.getInstance(this).getFuncionarioDAO().validarLogin(user, pass).get();
        if (u == null) {
            alertAviso("Senha Inválida. Tente novamente.", false);
        } else if (coletor != null) {
            ConfigGerais.UsuarioLogado = u;
            Intent i = new Intent(this, MenuPrincipalCorteMuda.class);
            startActivity(i);
        } else {
            alertAviso("Coletor não configurado. Para continuar, adicione um número de coletor em configurações.", false);
        }
//        Intent i = new Intent(this, MenuPrincipalProdutividadeActivity.class);
//            startActivity(i);
//            finish();
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    private void alertAviso(String mensagem, boolean trocarMsg) {
        //CRIA ALERT
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.alert_aviso, null);

        //DECLARA VARIAVEIS
        Button btnOk;
        TextView msg;
        btnOk = view1.findViewById(R.id.btn_OkAlert2);
        msg = view1.findViewById(R.id.txtmsg);
        if (trocarMsg) {
            msg.setText(mensagem);
        } else {
            msg.setText(mensagem);
        }

        builder.setView(view1);
        trocarMsg = false;
        AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaBanco();
        carregarColetor();
        if (!U_Data_Hora.validarDataHoraAuto(this)) {
            Intent i = new Intent(Settings.ACTION_DATE_SETTINGS);
            AguardaResultadoForcaAuto.launch(i);
        }
        editMatricula.setText("");
        editMatricula.requestFocus();
        editSenha.setText("");
        editUsuario.setText("");

    }

    private void carregaBanco() {
        try {
            listaFuncionarios = CMDatabase.getInstance(this).getFuncionarioDAO().buscarTodos().get();
            listaURL = CMDatabase.getInstance(this).getConfigWSDAO().buscarUm().get();
            coletor = CMDatabase.getInstance(this).getColetorDAO().buscarAtivo().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressLint("SetTextI18n")
    private void carregarColetor() {
        if (coletor != null) {
            txtColetor.setText(res.getString(R.string.coletor, coletor.getNumero().toString()));
        } else {
            txtColetor.setText(getResources().getString(R.string.coletor, "Não Configurado"));
        }
    }

    //    // MÉTODO QUE EXCLUI DIA INICIADO (DEPOIS DE 15 DIAS)
//    private void checkBanco() {
//        try {
//            List<IniciarDia> iniciarDia = PRDatabase.getInstance(this).getIniciarDiaDAO().buscarTodos().get();
//            if (iniciarDia != null) {
//                if (iniciarDia.stream().noneMatch(y -> U_Data_Hora.diferenciarTempo(y.getDataNovoDia(),
//                        U_Data_Hora.DDMMYYYY, U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY), U_Data_Hora.DDMMYYYY, U_Data_Hora.DD) > 15)) {
//                    return;
//                }
//                List<IniciarDia> diaExcluir = iniciarDia.stream().filter(x -> U_Data_Hora.diferenciarTempo(x.getDataNovoDia(),
//                        U_Data_Hora.DDMMYYYY, U_Data_Hora.retornaData(0, U_Data_Hora.DDMMYYYY), U_Data_Hora.DDMMYYYY,
//                        U_Data_Hora.DD) > 15).collect(Collectors.toList());
//                if (diaExcluir.size() > 0) {
//                    PRDatabase.getInstance(this).getIniciarDiaDAO().excluir(diaExcluir).get();
//                    finish();
//                    startActivity(getIntent());
//                }
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    @SuppressLint({"StringFormatMatches", "SetTextI18n"})
    void iniciarCampos() {

        versao = findViewById(R.id.versao_app);
        try {
            versao.setText(this.getString(R.string.msg_versao_app, this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            versao.setText("");
            e.printStackTrace();
        }
        editMatricula = findViewById(R.id.editMatricula);
        editUsuario = findViewById(R.id.editUsuario);
        editSenha = findViewById(R.id.editSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnSync = findViewById(R.id.btnSinc);
        btnconfig = findViewById(R.id.btnConfig);
        txtColetor = findViewById(R.id.txtColetor);
        res = getResources();
    }
}