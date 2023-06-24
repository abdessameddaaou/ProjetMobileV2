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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
    private double previousLatitude = 0.0;
    private double previousLongitude = 0.0;

    private static final int REQUEST_CODE = 100;
    private static final int LOCATION_UPDATE_DELAY = 15000; // 15 seconds

    private FloatingActionButton terminerBtn;

    private Handler locationHandler;
    private Runnable locationRunnable;
    private LocationCallback locationCallback;

    private boolean manualLocationUpdate = false;

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
                manualLocationUpdate = true;
                getLastLocation();
            }
        });

        // Initialiser le gestionnaire de localisation différée
        locationHandler = new Handler();
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                if (!manualLocationUpdate) {
                    getLastLocation();
                }
                scheduleLocationUpdates(); // Planifier la prochaine mise à jour de localisation
            }
        };

        startLocationUpdates();
        scheduleLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(LOCATION_UPDATE_DELAY);

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location location = locationResult.getLastLocation();
                        if (location != null && !manualLocationUpdate) {
                            updateLocation(location);
                        }
                    }
                }
            };

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }
    private int getSecondsFromTimerText(String timerText) {
        String[] timeParts = timerText.split(":");
        if (timeParts.length == 2) {
            int minutes = Integer.parseInt(timeParts[0].trim());
            int seconds = Integer.parseInt(timeParts[1].trim());
            return minutes * 60 + seconds;
        } else {
            // Gérer le cas où le format du temps est incorrect
            return 0;
        }
    }

    private void scheduleLocationUpdates() {
        locationHandler.postDelayed(locationRunnable, LOCATION_UPDATE_DELAY); // Planifier la prochaine mise à jour de localisation
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateLocation(location);
                    }
                }
            });
        } else {
            // Demander la permission d'accéder à la localisation si elle n'est pas encore accordée
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (manualLocationUpdate) {
            // Ajouter les données de latitude et de longitude à la base de données pour la localisation manuelle
            MyDataBaseHelper MDB = new MyDataBaseHelper(Chronometre.this);
            MDB.AddTrajet(getIntent().getStringExtra("trajet_nom"), (float) latitude, (float) longitude);

            Toast.makeText(Chronometre.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_LONG).show();

            manualLocationUpdate = false; // Réinitialiser le drapeau de mise à jour manuelle de la localisation
        } else {
            // Vérifier si la position est différente de la précédente
            if (latitude != previousLatitude || longitude != previousLongitude) {
                // Ajouter les données de latitude et de longitude à la base de données
                MyDataBaseHelper MDB = new MyDataBaseHelper(Chronometre.this);
                MDB.AddTrajet(getIntent().getStringExtra("trajet_nom"), (float) latitude, (float) longitude);

                Toast.makeText(Chronometre.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_LONG).show();

                // Mettre à jour les coordonnées précédentes
                previousLatitude = latitude;
                previousLongitude = longitude;
            }
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
                }
            }

            chronometer.setBase(SystemClock.elapsedRealtime() - (minutes * 60000 + seconds * 1000));
            chronometer.start();
            running = true;
        }
    }

    public void FinirLeTrajet() {
        chronometer.stop();
        running = false;
        timerTxt.setText("00:00");
        locationHandler.removeCallbacks(locationRunnable); // Arrêter la planification des mises à jour de localisation
        fusedLocationProviderClient.removeLocationUpdates(locationCallback); // Arrêter les mises à jour de localisation
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationHandler.removeCallbacks(locationRunnable); // Arrêter la planification des mises à jour de localisation
        fusedLocationProviderClient.removeLocationUpdates(locationCallback); // Arrêter les mises à jour de localisation
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
