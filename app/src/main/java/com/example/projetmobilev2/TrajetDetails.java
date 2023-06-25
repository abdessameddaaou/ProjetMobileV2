package com.example.projetmobilev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TrajetDetails extends AppCompatActivity {
    TextView nomTrajet, idTrajet;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet_details);

        nomTrajet = findViewById(R.id.nom_trajet);
        //idTrajet = findViewById(R.id.id_trajet);

        recyclerView = findViewById(R.id.recyclerView_localisation);

        nomTrajet.setText(getIntent().getStringExtra("nom_trajet"));
        //idTrajet.setText(getIntent().getStringExtra("docId"));
        List<Localisation> listeLocalisations = (List<Localisation>) getIntent().getSerializableExtra("listeLocalisations");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        LocalisationAdapter adapter = new LocalisationAdapter(listeLocalisations);
        recyclerView.setAdapter(adapter);
    }
}