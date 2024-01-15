package com.example.cortedemudaexataid.telas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Registros;
import com.example.cortedemudaexataid.utils.CliqueRecycler;
import com.example.cortedemudaexataid.utils.U_Data_Hora;

import java.util.List;

public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaAdapter.ViewHolder> {

    private List<Registros> listaRegistros;
    private Context ctx;
    private Resources res;
    private CliqueRecycler cliqueRecycler;

    public void setCliqueRecycler(CliqueRecycler cliqueRecycler) {
        this.cliqueRecycler = cliqueRecycler;
    }

    public ConsultaAdapter(List<Registros> listaRegistros, Context ctx, Resources res) {
        this.listaRegistros = listaRegistros;
        this.ctx = ctx;
        this.res = res;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtData, txtCodFazenda, txtNomeFazenda, txtTalhao, txtArea, txtTotPar, txtEstimativa, txtUltimoCorte, txtEnviado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtDataRegistro);
            txtCodFazenda = itemView.findViewById(R.id.txtCodFaz);
            txtNomeFazenda = itemView.findViewById(R.id.txtNomeFazenda);
            txtTalhao = itemView.findViewById(R.id.txtCodTalhao);
            txtArea = itemView.findViewById(R.id.txtArea);
            txtTotPar = itemView.findViewById(R.id.txtTotPar);
            txtEstimativa = itemView.findViewById(R.id.txtEstimativa);
            txtUltimoCorte = itemView.findViewById(R.id.txtUltimoCorte);
            txtEnviado = itemView.findViewById(R.id.txtEnviado);

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

        public TextView getTxtTalhao() {
            return txtTalhao;
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

        public TextView getTxtUltimoCorte() {
            return txtUltimoCorte;
        }

        public TextView getTxtEnviado() {
            return txtEnviado;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_registros_salvos, parent, false
        );
        return new ViewHolder(v);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position % 2 != 0) {
            holder.itemView.setBackgroundColor(ctx.getColor(R.color.temaCinza));
        } else {
            holder.itemView.setBackgroundColor(ctx.getColor(R.color.temaBranco));
        }

        holder.getTxtData().setText(res.getString(R.string.registros, U_Data_Hora.formatarData(listaRegistros.get(position).getDataRegistro(), U_Data_Hora.YYYY_MM_DD, U_Data_Hora.DDMMYYYY)));
        holder.getTxtCodFazenda().setText(res.getString(R.string.registros, listaRegistros.get(position).getCodFazenda()));
        holder.getTxtNomeFazenda().setText(res.getString(R.string.registros, listaRegistros.get(position).getNomeFazenda()));
        holder.getTxtTalhao().setText(res.getString(R.string.registros, listaRegistros.get(position).getCodTalhao()));
        holder.getTxtTotPar().setText(res.getString(R.string.registros, listaRegistros.get(position).getTotalOuParcial()));
        holder.getTxtUltimoCorte().setText(res.getString(R.string.registros, listaRegistros.get(position).getUltimoCorte()));
        holder.getTxtEnviado().setText(res.getString(R.string.registros, listaRegistros.get(position).getEnviado()));
        @SuppressLint("DefaultLocale") String areaFormat = String.format("%.2f", listaRegistros.get(position).getArea()).replace(",",".");
        holder.getTxtArea().setText(res.getString(R.string.registros, areaFormat));
        holder.getTxtTotPar().setText(res.getString(R.string.registros, listaRegistros.get(position).getTotalOuParcial()));
        @SuppressLint("DefaultLocale") String estimativaFormat = String.format("%.2f", listaRegistros.get(position).getEstimativa()).replace(",",".");
        holder.getTxtEstimativa().setText(res.getString(R.string.registros, estimativaFormat));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliqueRecycler != null) {
                    cliqueRecycler.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRegistros.size();
    }
}
