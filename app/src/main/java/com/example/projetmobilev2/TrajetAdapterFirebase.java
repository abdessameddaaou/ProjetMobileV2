package com.example.projetmobilev2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.Serializable;


public class TrajetAdapterFirebase extends FirestoreRecyclerAdapter<TrajetBase,TrajetAdapterFirebase.TrajetViewHolder> {

    Context context;
    public TrajetAdapterFirebase(@NonNull FirestoreRecyclerOptions<TrajetBase> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TrajetViewHolder holder, int position, @NonNull TrajetBase trajetFirebase) {
        holder.nomTrajet.setText(trajetFirebase.nomTrajet);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrajetDetails.class);
                intent.putExtra("nom_trajet", trajetFirebase.nomTrajet);
                intent.putExtra("listeLocalisations", (Serializable) trajetFirebase.listeLocalisations);

                if (position >= 0 && position < getSnapshots().size()) {
                    String docId = getSnapshots().getSnapshot(position).getId();
                    intent.putExtra("docId", docId);
                    context.startActivity(intent);
                } else {
                    // Gérer le cas où la position est en dehors des limites de la liste
                }
            }
        });
    }

    @NonNull
    @Override
    public TrajetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_firebase,parent,false);
        return new TrajetViewHolder(view);
    }
    class TrajetViewHolder extends RecyclerView.ViewHolder{

        TextView nomTrajet;
        public TrajetViewHolder(@NonNull View itemView) {
            super(itemView);
            nomTrajet = itemView.findViewById(R.id.nom_trajet);
        }
    }
}
