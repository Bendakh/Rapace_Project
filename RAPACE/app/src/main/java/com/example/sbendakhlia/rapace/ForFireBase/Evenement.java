package com.example.sbendakhlia.rapace.ForFireBase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Evenement {

    public int id;
    public String extras;
    public int type_evt; // 0 = alerte
    public long date; // en timestamp

    public Evenement() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Evenement(int id, int type_evt, String extras, long date, String action, String image) {
        this.id = id;
        this.type_evt = type_evt;
        this.extras = extras;
        this.date = date;
    }

}
