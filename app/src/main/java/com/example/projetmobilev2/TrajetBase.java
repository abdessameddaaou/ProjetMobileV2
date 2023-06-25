package com.example.projetmobilev2;

import java.util.List;

public class TrajetBase {
     String nomTrajet;
     List<Localisation> listeLocalisations;

    // Constructeur
    public TrajetBase(){

    }

    public String getNomTrajet() {
        return nomTrajet;
    }

    public void setNomTrajet(String nomTrajet) {
        this.nomTrajet = nomTrajet;
    }

    public List<Localisation> getListeLocalisations() {
        return listeLocalisations;
    }

    public void setListeLocalisations(List<Localisation> listeLocalisations) {
        this.listeLocalisations = listeLocalisations;
    }
}
