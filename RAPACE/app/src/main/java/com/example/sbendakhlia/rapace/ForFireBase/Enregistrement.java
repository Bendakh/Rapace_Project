package com.example.sbendakhlia.rapace.ForFireBase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Enregistrement {

    public int id;
    public String url_res;
    public int type_recording; // 0 = video 1 = image
    public long date; // en timestamp

    public Enregistrement() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Enregistrement(int id, String url_res, int type_recording, long date) {
        this.id = id;
        this.url_res = url_res;
        this.type_recording = type_recording;
        this.date = date;
    }

}
