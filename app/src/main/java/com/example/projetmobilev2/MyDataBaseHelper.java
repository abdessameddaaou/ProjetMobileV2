package com.example.projetmobilev2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ProjetMobile.db";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Trajets";
    private static final String ID = "_id";
    private static final String Nom = "nom_trajet";
    private static final String X_Lat = "latitude";
    private static final String Y_Long = "longitude";

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_NAME + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Nom + " TEXT, " + X_Lat + " REAL, "+ Y_Long + " REAL, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    void AddTrajet(String NomParams, float X_LatParams, float Y_LongParams)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Nom, NomParams);
        values.put(X_Lat, X_LatParams);
        values.put(Y_Long, Y_LongParams);
        long result  = db.insert(TABLE_NAME, null, values);

        if( result == -1)
        {
            Toast.makeText(context, "Failed to insert", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Success insertion", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor ReadALL()
    {
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =  null;

        if( db != null)
        {
            cursor =  db.rawQuery(query, null);
        }
        return  cursor;
    }


}
