package com.example.projetmobilev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class TrajetFirebase extends AppCompatActivity {

    RecyclerView recyclerView;

    TrajetAdapterFirebase trajetAdapterFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet_firebase);

        recyclerView = findViewById(R.id.recycle_firebase);


        // Intialiser recycleView

        setUpRecucleView();
    }

    private void setUpRecucleView() {
        Query query = FirebaseFirestore.getInstance().collection("trajets");
        FirestoreRecyclerOptions<TrajetBase> options = new FirestoreRecyclerOptions.Builder<TrajetBase>().setQuery(query,TrajetBase.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trajetAdapterFirebase = new TrajetAdapterFirebase(options,this);
        recyclerView.setAdapter(trajetAdapterFirebase);

    }

    @Override
    protected void onStart() {
        super.onStart();
        trajetAdapterFirebase.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        trajetAdapterFirebase.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        trajetAdapterFirebase.notifyDataSetChanged();
    }


}