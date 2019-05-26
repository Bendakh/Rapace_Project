package com.example.sbendakhlia.rapace.ForDataTransit;

import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class LocalByZone {

    public int zone;
    public ArrayList<Local> locaux;


    public LocalByZone() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public LocalByZone(int zone, ArrayList<Local> loc) {
        this.zone = zone;
        this.locaux = loc;
    }

}

