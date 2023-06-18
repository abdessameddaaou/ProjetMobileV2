package com.example.projetmobilev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrajetAdapter extends RecyclerView.Adapter<TrajetAdapter.ViewHolder> {

    Context context;
    ArrayList id_trajet, name_trajet, lat_trajet, lont_trajet, time_trajet;

    public TrajetAdapter(Context context, ArrayList id_trajet, ArrayList name_trajet, ArrayList lat_trajet, ArrayList lont_trajet, ArrayList time_trajet) {
        this.context = context;
        this.id_trajet = id_trajet;
        this.name_trajet = name_trajet;
        this.lat_trajet = lat_trajet;
        this.lont_trajet = lont_trajet;
        this.time_trajet = time_trajet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_items, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.lat_trajet_txt.setText(String.valueOf(lat_trajet.get(position)));
        holder.lont_trajet_txt.setText(String.valueOf(lont_trajet.get(position)));
        holder.time_tajet_txt.setText(String.valueOf(time_trajet.get(position)));

    }

    @Override
    public int getItemCount() {
        return id_trajet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_trajet_txt, name_trajet_txt, lat_trajet_txt, lont_trajet_txt, time_tajet_txt;

        public ViewHolder(@NonNull View view){
            super(view);
            lat_trajet_txt = view.findViewById(R.id.X_Cordonne);
            lont_trajet_txt = view.findViewById(R.id.Y_Cordonne);
            time_tajet_txt = view.findViewById(R.id.DateCreation);

        }

    }
}
