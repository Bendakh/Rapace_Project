package com.example.sbendakhlia.rapace.ForDataTransit;

public class LocalSimplifie{
    public float lat;
    public float longt;
    public String nom;
    public int id;

    // pour la version alerte
    public int id_extras;

    public LocalSimplifie(String nom, int id, float lat, float longt) {
        this.nom = nom;
        this.lat = lat;
        this.longt = longt;
        this.id = id;
    }

    public LocalSimplifie(String nom, int id, float lat, float longt, int id_extras) {
        this(nom, id, lat, longt);
        this.id_extras = id_extras;
    }

}
