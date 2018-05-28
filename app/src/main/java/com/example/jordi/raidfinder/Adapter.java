package com.example.jordi.raidfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ParticipantesHolder> {


    private LayoutInflater mInflater;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Context ctx;

    List<User> participantes;

    public Adapter(List<User> participantes, ParticipantesIncursionActivity participantesIncursionActivity) {
        this.participantes = participantes;
    }

    @Override
    public ParticipantesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row,parent,false);
        ParticipantesHolder holder = new ParticipantesHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ParticipantesHolder holder, int position) {
        User user= participantes.get(position);
        holder.participanteNombre.setText(user.getNombre());
        holder.participanteNivel.setText(String.valueOf(user.getNivel()));


    }

    @Override
    public int getItemCount() {
        return participantes.size();
    }

    public static class ParticipantesHolder extends RecyclerView.ViewHolder{
        TextView participanteDefaultNombre;
        TextView participanteNombre;
        TextView participanteDefaultNivel;
        TextView participanteNivel;
        ImageView participanteEquipo;
        //ConstraintLayout constraintLayout;

        public ParticipantesHolder(View itemView) {
            super(itemView);
            participanteDefaultNombre = itemView.findViewById(R.id.participanteDefaultNombre);
            participanteNombre = itemView.findViewById(R.id.participanteNombre);
            participanteDefaultNivel = itemView.findViewById(R.id.participanteDefaultNivel);
            participanteNivel = itemView.findViewById(R.id.participanteNivel);
            participanteEquipo = itemView.findViewById(R.id.participanteEquipo);
            //constraintLayout=itemView.findViewById(R.id.constraintLayout);
        }
    }


}
