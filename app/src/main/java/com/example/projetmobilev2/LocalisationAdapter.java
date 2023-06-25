package com.example.projetmobilev2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class LocalisationAdapter extends RecyclerView.Adapter<LocalisationAdapter.LocalisationViewHolder> {
    private List<Localisation> localisations;

    public LocalisationAdapter(List<Localisation> localisations) {
        this.localisations = localisations;
    }

    @NonNull
    @Override
    public LocalisationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_localisation_firebase, parent, false);
        return new LocalisationViewHolder(view);
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull LocalisationViewHolder holder, int position) {
        Localisation localisation = localisations.get(position);
        holder.bind(localisation);
    }

    @Override
    public int getItemCount() {
        return localisations.size();
    }

    public static class LocalisationViewHolder extends RecyclerView.ViewHolder {
        private TextView cordonnesX;
        private TextView cordonnesY;
        private TextView times;

        public LocalisationViewHolder(@NonNull View itemView) {
            super(itemView);
            cordonnesX = itemView.findViewById(R.id.cordX);
            cordonnesY = itemView.findViewById(R.id.cordY);
            times = itemView.findViewById(R.id.timestamps);
        }

        public void bind(Localisation localisation) {
            cordonnesX.setText(String.valueOf(localisation.getCoordX()));
            cordonnesY.setText(String.valueOf(localisation.getCoordY()));
            times.setText(String.valueOf(localisation.getTimestamp()));

        }
    }
}

