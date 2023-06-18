package com.example.projetmobilev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button Start_btn;
    EditText trajet_home_name;
    Chronometre chrono = new Chronometre();


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
            public void onClick(View view)  {
                MyDataBaseHelper dataBaseHelper = new MyDataBaseHelper(MainActivity.this);
                dataBaseHelper.onUpgrade(dataBaseHelper.getWritableDatabase(),1,2);
                Intent intent = new Intent(MainActivity.this, Chronometre.class);
                String title = trajet_home_name.getText().toString().trim();

                if(title == null || title.isEmpty()){
                    trajet_home_name.setError("Veuillez renseigner le nom du trajet");
                    return;
                }
                intent.putExtra("trajet_nom",title);
                startActivity(intent);

            }
        });
    }
}