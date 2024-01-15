package com.example.cortedemudaexataid.telas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.utils.U_Data_Hora;

import java.util.List;

public class RegistrosAdapter extends RecyclerView.Adapter<RegistrosAdapter.ViewHolder> {

    private final List<Registros> localDataSet;

    private final Context ctx;
    private final Resources res;

    public RegistrosAdapter(List<Registros> localDataSet, Context ctx, Resources res) {
        this.localDataSet = localDataSet;
        this.ctx = ctx;
        this.res = res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtData, txtCodFazenda, txtNomeFazenda, txtCodTalhao, txtArea, txtTotPar, txtEstimativa;
        ImageView btnDelete;

        public ImageView getBtnDelete() {
            return btnDelete;
        }

        public TextView getTxtData() {
            return txtData;
        }

        public TextView getTxtCodFazenda() {
            return txtCodFazenda;
        }

        public TextView getTxtNomeFazenda() {
            return txtNomeFazenda;
        }

        public TextView getTxtCodTalhao() {
            return txtCodTalhao;
        }

        public TextView getTxtArea() {
            return txtArea;
        }

        public TextView getTxtTotPar() {
            return txtTotPar;
        }

        public TextView getTxtEstimativa() {
            return txtEstimativa;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtDataRegistro);
            txtCodFazenda = itemView.findViewById(R.id.txtCodFaz);
            txtNomeFazenda = itemView.findViewById(R.id.txtNomeFazenda);
            txtCodTalhao = itemView.findViewById(R.id.txtCodTalhao);
            txtArea = itemView.findViewById(R.id.txtArea);
            txtTotPar = itemView.findViewById(R.id.txtTotPar);
            txtEstimativa = itemView.findViewById(R.id.txtEstimativa);
            btnDelete = itemView.findViewById(R.id.btnLixeira);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View registros = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_registros, parent, false);
        return new ViewHolder(registros);
    }

    @SuppressLint({"StringFormatMatches", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.getTxtData().setText(res.getString(R.string.registros, U_Data_Hora.formatarData(localDataSet.get(position).getDataRegistro(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY)));
        holder.getTxtCodFazenda().setText(res.getString(R.string.registros, localDataSet.get(position).getCodFazenda()));
        holder.getTxtNomeFazenda().setText(res.getString(R.string.registros, localDataSet.get(position).getNomeFazenda()));
        holder.getTxtCodTalhao().setText(res.getString(R.string.registros, localDataSet.get(position).getCodTalhao()));
        String areaFormat = String.format("%.2f", localDataSet.get(position).getArea()).replace(",",".");
        holder.getTxtArea().setText(res.getString(R.string.registros, areaFormat));
        holder.getTxtTotPar().setText(res.getString(R.string.registros, localDataSet.get(position).getTotalOuParcial()));
        String estimativaFormat = String.format("%.2f", localDataSet.get(position).getEstimativa()).replace(",",".");
        holder.getTxtEstimativa().setText(res.getString(R.string.registros, estimativaFormat));
        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"SetTextI18n", "MissingInflatedId"})
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = LayoutInflater.from(ctx);

                View view1 = inflater.inflate(R.layout.alert_aviso, null);
                builder.setView(view1);

                //DECLARA VARIAVEIS
                Button btnOk, btnCancela;
                TextView msg;
                btnOk = view1.findViewById(R.id.btn_OkAlert2);
                btnCancela = view1.findViewById(R.id.btn_CancelaAlert);
                msg = view1.findViewById(R.id.txtmsg);
                btnCancela.setVisibility(View.VISIBLE);
                msg.setText("Deseja remover o apontamento " + (position + 1) + "  da lista de registros?");

                builder.setView(view1);
                AlertDialog alertDialog = builder.create();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        localDataSet.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, localDataSet.size());
                        alertDialog.dismiss();
                    }
                });
                btnCancela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
