package com.example.projetmobilev2;

public class Trajet {
    String id;
    String name;
    float lat;
    float lont;

    public Trajet(float lat, float lont) {
        this.lat = lat;
        this.lont = lont;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLont() {
        return lont;
    }

    public void setLont(float lont) {
        this.lont = lont;
    }
}
