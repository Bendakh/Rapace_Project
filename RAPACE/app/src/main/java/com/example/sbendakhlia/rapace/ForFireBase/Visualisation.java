package com.example.sbendakhlia.rapace.ForFireBase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Visualisation {

    public String id;
    public String id_user;
    public int nb_times; // 0 = video 1 = image
    public long date_lasttime; // en timestamp

    public Visualisation() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Visualisation(String id, String id_user, int nb_times, long date_lasttime) {
        this.id = id;
        this.id_user = id_user;
        this.nb_times = nb_times;
        this.date_lasttime = date_lasttime;
    }

}
