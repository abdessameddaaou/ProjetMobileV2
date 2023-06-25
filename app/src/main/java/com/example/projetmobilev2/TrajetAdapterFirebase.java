package com.example.projetmobilev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


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
