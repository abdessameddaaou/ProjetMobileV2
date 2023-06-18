package com.example.projetmobilev2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Chronometre extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView timerTxt;
    private FloatingActionButton addBtn, viewBtn;
    private Chronometer chronometer;
    private boolean running;
    private float longitude;
    private float latitude;
    private static final int REQUEST_CODE = 100;
    private static final int LOCATION_UPDATE_DELAY = 15000; // 15 seconds

    private FloatingActionButton terminerBtn;

    private Handler locationHandler;
    private Runnable locationRunnable;
    private int timerSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometre);

        chronometer = findViewById(R.id.chronometer);
        terminerBtn = findViewById(R.id.Finir_Trajet);
        addBtn = findViewById(R.id.Add_Button);
        timerTxt = findViewById(R.id.Timer_text);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        viewBtn = findViewById(R.id.Visualiser_Button);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chronometre.this, AllPositions.class);
                startActivity(intent);
            }
        });

        startChronometer();

        terminerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinirLeTrajet();
                Intent intent = new Intent(Chronometre.this, AllPositions.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                Log.d("essie", String.valueOf(longitude));
            }
        });

        // Initialiser le gestionnaire de localisation différée
        locationHandler = new Handler();
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                getLastLocation();
                scheduleLocationUpdates(); // Planifier la prochaine mise à jour de localisation
            }
        };
        scheduleLocationUpdates();
    }

    private void scheduleLocationUpdates() {
        locationHandler.postDelayed(locationRunnable, LOCATION_UPDATE_DELAY); // Mettre à jour toutes les 1 seconde
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(Chronometre.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        float latitude = (float) addresses.get(0).getLatitude();
                        float longitude = (float) addresses.get(0).getLongitude();

                        // Ajouter les données d'altitude et de longitude à la base de données
                        MyDataBaseHelper MDB = new MyDataBaseHelper(Chronometre.this);
                        MDB.AddTrajet(getIntent().getStringExtra("trajet_nom"), latitude, longitude);

                        Toast.makeText(Chronometre.this, "Longitude: " + longitude + ", Latitude: " + latitude, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            // Demander la permission d'accéder à la localisation si elle n'est pas encore accordée
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show();
        }
    }

    public void startChronometer() {
        if (!running) {
            int minutes = 0;
            int seconds = 0;

            if (timerTxt.getText() != null && !timerTxt.getText().toString().isEmpty()) {
                String timerText = timerTxt.getText().toString();
                String[] timeParts = timerText.split(":");
                if (timeParts.length == 2) {
                    minutes = Integer.parseInt(timeParts[0].trim());
                    seconds = Integer.parseInt(timeParts[1].trim());
                } else {
                    // Afficher un message d'erreur si le format du temps est incorrect
                    Toast.makeText(this, "Format du temps incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            timerSeconds = minutes * 60 + seconds;
            chronometer.setBase(SystemClock.elapsedRealtime() - (timerSeconds * 1000));
            chronometer.start();
            running = true;
        }
    }


    public void FinirLeTrajet() {
        chronometer.stop();
        timerSeconds = 0;
        running = false;
        locationHandler.removeCallbacks(locationRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêter les mises à jour de localisation différées lorsque l'activité est détruite
        locationHandler.removeCallbacks(locationRunnable);
    }
}
