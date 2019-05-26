package com.example.sbendakhlia.rapace.ForFireBase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Local {

    public int id;
    public String nom;
    public int categorie;
    public int zone;
    public float lat;
    public float longt;

    // pour transmettre les informations concernant le user
    public String uId = "";
    public String admin = "";

    public Local() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Local(String nom, int id, int categorie, int zone, float lat, float longt) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.zone = zone;
        this.lat = lat;
        this.longt = longt;
    }

    public void addUsrInfo(String uId, String admin) {
        this.uId = uId;
        this.admin = admin;
    }

    public String GetNom()
    {
        return this.nom;
    }
}