package com.example.projetmobilev2;

import java.io.Serializable;
import java.util.Date;

import com.google.firebase.Timestamp;

public class Localisation implements Serializable {

    private double coordX;
    private double coordY;
    private Date timestamp;

    public Localisation(){

    }

    public double getCoordX() {
        return coordX;
    }

    public void setCoordX(double coordX) {
        this.coordX = coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public void setCoordY(double coordY) {
        this.coordY = coordY;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = new Date(timestamp.toDate().getTime());
    }

}
