package com.example.cortedemudaexataid.telas.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Talhao;
import com.example.cortedemudaexataid.utils.OnItemClickListener;

import java.util.List;

public class TalhoesAdapter extends RecyclerView.Adapter<TalhoesAdapter.ViewHolder> {
    private final List<Talhao> localDataSet;
    private final OnItemClickListener<Talhao> listener;

    public TalhoesAdapter(List<Talhao> localDataSet, OnItemClickListener<Talhao> listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView areaTalhao, codTalhao;

        public TextView getAreaTalhao() {
            return areaTalhao;
        }

        public TextView getCodTalhao() {
            return codTalhao;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            areaTalhao = itemView.findViewById(R.id.txtNomeFazenda);
            codTalhao = itemView.findViewById(R.id.txtCodFazenda);
        }

    }

    @NonNull
    @Override
    public TalhoesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_talhoes, parent, false
        );
        return new TalhoesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String formato = "%.2f";
        @SuppressLint("DefaultLocale") String area = String.format(formato, localDataSet.get(position).getArea()).replace(",",".");
        holder.getAreaTalhao().setText(area);
        holder.getCodTalhao().setText(String.valueOf(localDataSet.get(position).getCodTalhao()));
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(localDataSet.get(position)));
    }


    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
