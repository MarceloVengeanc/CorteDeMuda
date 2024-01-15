package com.example.cortedemudaexataid.telas.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cortedemudaexataid.R;
import com.example.cortedemudaexataid.banco.modelos.ConfigWS;
import com.example.cortedemudaexataid.utils.OnItemClickListener;

import java.util.List;


public class ConfigWSAdapter extends RecyclerView.Adapter<ConfigWSAdapter.ViewHolder> {

	private List<ConfigWS> localDataSet;
	private OnItemClickListener<ConfigWS> listener;
	private Context context;
	private Resources res;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView url;
		private final TextView porta;
		private final TextView prioridade;

		public ViewHolder(View view) {
			super(view);

			url = view.findViewById(R.id.txtItemConfigWSEndereco);
			porta = view.findViewById(R.id.txtItemConfigWSPorta);
			prioridade = view.findViewById(R.id.txtItemConfigWSPrioridade);
		}

		public TextView getUrl() {
			return url;
		}
		public TextView getPorta() {
			return porta;
		}
		public TextView getPrioridade() {
			return prioridade;
		}
	}

	public ConfigWSAdapter(Context ctx, List<ConfigWS> dataSet, OnItemClickListener<ConfigWS> l) {
		localDataSet = dataSet;
		context = ctx;
		res = ctx.getResources();
		listener = l;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext())
			.inflate(R.layout.item_lista_config_ws, viewGroup, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.getUrl().setText(res.getString(R.string.configws_servidor,
				localDataSet.get(position).getUrl()));
		viewHolder.getPorta().setText(res.getString(R.string.configws_porta,
				localDataSet.get(position).getPorta().toString()));
		viewHolder.getPrioridade().setText(res.getString(R.string.configws_prioridade,
				localDataSet.get(position).getPrioridade().toString()));
		viewHolder.itemView.setOnClickListener(v -> {
			listener.onItemClick(localDataSet.get(position));
		});
	}

	@Override
	public int getItemCount() {
		return localDataSet.size();
	}
}