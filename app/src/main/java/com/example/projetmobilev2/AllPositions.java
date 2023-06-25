package com.example.projetmobilev2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AllPositions extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingactionbutton, visualisemap, firebaseBtn;
    TrajetAdapter adapter;

    MyDataBaseHelper myDB;
    public static ArrayList<String> trajet_id = new ArrayList<>();
    public static ArrayList<String> trajet_nom = new ArrayList<>();
    public static ArrayList<String> trajet_lat = new ArrayList<>();
    public static ArrayList<String> trajet_lon = new ArrayList<>();
    ArrayList<String>  trajet_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_positions);

        recyclerView = findViewById(R.id.affichage);

        myDB = new MyDataBaseHelper(AllPositions.this);
        trajet_id = new ArrayList<>();
        trajet_nom = new ArrayList<>();
        trajet_lat = new ArrayList<>();
        trajet_lon = new ArrayList<>();
        trajet_time = new ArrayList<>();

        DisplayData();
        adapter = new TrajetAdapter(AllPositions.this, trajet_id, trajet_nom, trajet_lat, trajet_lon, trajet_time);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllPositions.this));
        floatingactionbutton = findViewById(R.id.Export);
        visualisemap = findViewById(R.id.mapvisualie);



        visualisemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllPositions.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        firebaseBtn = findViewById(R.id.firebase_btn);

        // on clik sur le button firebase
        firebaseBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AllPositions.this,TrajetFirebase.class);
            startActivity(intent);
        });

        floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifier si la permission d'accès au stockage externe est déjà accordée
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Demander la permission d'accès au stockage externe
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    // La permission est déjà accordée, vous pouvez procéder à l'exportation des données
                    ExportFile();
                }
            }


        });



    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // Permission accordée
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ExportFile();
            } else {
                // La permission a été refusée.
                Toast.makeText(this, "Permission refusée. Impossible d'exporter les données.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void ExportFile() {
        String fileName = "export.gpx";

        try {
            StringBuilder gpxContent = new StringBuilder();
            gpxContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            gpxContent.append("<gpx version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\">\n");

            for (int i = 0; i < trajet_id.size(); i++) {
                double lat = Double.parseDouble(trajet_lat.get(i));
                double lon = Double.parseDouble(trajet_lon.get(i));
                String timeString = trajet_time.get(i);

                int year = Integer.parseInt(timeString.substring(0, 4));
                int month = Integer.parseInt(timeString.substring(5, 7));
                int day = Integer.parseInt(timeString.substring(8, 10));
                int hour = Integer.parseInt(timeString.substring(11, 13));
                int minute = Integer.parseInt(timeString.substring(14, 16));
                int second = Integer.parseInt(timeString.substring(17, 19));

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day, hour, minute, second);

                long time = calendar.getTimeInMillis();

                gpxContent.append("<wpt lat=\"").append(lat).append("\" lon=\"").append(lon).append("\">\n");
                gpxContent.append("<time>").append(formatTime(time)).append("</time>\n");
                gpxContent.append("</wpt>\n");
            }

            gpxContent.append("</gpx>");

            FileWriter writer = new FileWriter(getExportFile(fileName));
            writer.write(gpxContent.toString());
            writer.flush();
            writer.close();

            Toast.makeText(this, "Export réussi : " + getExportFilePath(fileName), Toast.LENGTH_SHORT).show();

            // Partager le fichier via l'email
            File exportFile = getExportFile(fileName);
            Uri fileUri = FileProvider.getUriForFile(this, "com.example.projetmobile.fileprovider", exportFile);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/gpx+xml");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Fichier GPX exporté");
            intent.putExtra(Intent.EXTRA_TEXT, "Veuillez trouver en pièce jointe le fichier GPX exporté.");
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Partager via"));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'exportation des données", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date(time));
    }


    private File getExportFile(String fileName) throws IOException {
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDirectory.exists()) {
            downloadsDirectory.mkdirs();
        }
        return new File(downloadsDirectory, fileName);
    }

    private String getExportFilePath(String fileName) {
        File exportFile = new File(getFilesDir(), "downloads/" + fileName);
        return exportFile.getAbsolutePath();
    }

    private String convertStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }



    void DisplayData()
    {
        Cursor cursor = myDB.ReadALL();

        if(cursor.getCount() == 0)
        {
            Toast.makeText(this, "La table est vide", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (cursor.moveToNext())
            {
                trajet_id.add(cursor.getString(0));
                trajet_nom.add(cursor.getString(1));
                trajet_lat.add(cursor.getString(2));
                trajet_lon.add(cursor.getString(3));
                trajet_time.add(cursor.getString(4));
            }
        }
    }
}
