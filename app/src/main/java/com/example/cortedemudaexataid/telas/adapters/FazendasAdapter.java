package com.example.cortedemudaexataid.telas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.Fazendas;
import com.example.cortedemudaexataid.utils.OnItemClickListener;

import java.util.List;

public class FazendasAdapter extends RecyclerView.Adapter<FazendasAdapter.ViewHolder> {

    private final List<Fazendas> localDataSet;
    private final OnItemClickListener<Fazendas> listener;

    public FazendasAdapter(List<Fazendas> localDataSet, OnItemClickListener<Fazendas> listener) {
        this.localDataSet = localDataSet;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nomeFazenda, codFazenda;

        public TextView getNomeFazenda() {
            return nomeFazenda;
        }

        public TextView getCodFazenda() {
            return codFazenda;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeFazenda = itemView.findViewById(R.id.txtNomeFazenda);
            codFazenda = itemView.findViewById(R.id.txtCodFazenda);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_lista_fazendas, parent, false
        );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNomeFazenda().setText(String.valueOf(localDataSet.get(position).getNomeFazenda()));
        holder.getCodFazenda().setText(String.valueOf(localDataSet.get(position).getCodFazenda()));
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(localDataSet.get(position)));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
