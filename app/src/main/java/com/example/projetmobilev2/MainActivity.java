package com.example.projetmobilev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity extends AppCompatActivity {

    Button Start_btn;
    EditText trajet_home_name;

    String trajetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Afficher l'activité en plein écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Start_btn = findViewById(R.id.Start);
        trajet_home_name = findViewById(R.id.trajetname);

        Start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trajetName = trajet_home_name.getText().toString().trim();
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(MainActivity.this);
                dataBaseHelper.onUpgrade(dataBaseHelper.getWritableDatabase(),1,2);
                if (trajetName.isEmpty()) {
                    trajet_home_name.setError("Veuillez renseigner le nom du trajet");
                    return;
                }

                TrajetBase trajetBase = new TrajetBase();
                trajetBase.setNomTrajet(trajetName);

                saveTrajetInFirebase(trajetBase);
            }
        });
    }

    void saveTrajetInFirebase(TrajetBase trajetBase) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference trajetsRef = db.collection("trajets");

        trajetsRef.add(trajetBase)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Trajet enregistré avec succès", Toast.LENGTH_SHORT).show();
                        String trajetId = documentReference.getId();
                        startChronometreActivity(trajetBase.getNomTrajet(),trajetId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erreur lors de l'enregistrement du trajet : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    void startChronometreActivity(String trajetNom,String trajetId) {
        Intent intent = new Intent(MainActivity.this, Chronometre.class);
        intent.putExtra("trajet_nom", trajetNom);
        intent.putExtra("trajet_id",trajetId);
        startActivity(intent);
    }
}
