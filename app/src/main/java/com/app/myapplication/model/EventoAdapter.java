package com.app.myapplication.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {
    public interface OnClickItemListener{
        void onItemClick(Evento evento);
    }
    private Context context;
    private List<Evento> listaEvento;
    private OnClickItemListener listener;
    public EventoAdapter(Context context, List<Evento> listaEvento, OnClickItemListener listener){
        this.context = context;
        this.listaEvento = listaEvento;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_event,parent,false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = listaEvento.get(position);

        holder.lstTittle.setText(evento.getNombre());
        holder.lstOrganizador.setText(evento.getOrganizador());
        holder.lstFecha.setText(evento.getFecha());
        holder.lstHora.setText(evento.getHora());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(evento);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEvento.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder{
        TextView lstTittle;
        TextView lstOrganizador;
        TextView lstFecha;
        TextView lstHora;

        public EventoViewHolder(@NonNull View itemView){
            super(itemView);
            lstTittle = itemView.findViewById(R.id.lstTittle);
            lstOrganizador = itemView.findViewById(R.id.lstOrganizador);
            lstFecha = itemView.findViewById(R.id.lstFecha);
            lstHora = itemView.findViewById(R.id.lstHora);
        }
    }
}
