package com.example.sbendakhlia.rapace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.ForFireBase.Enregistrement;
import com.example.sbendakhlia.rapace.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalRecordingAdaptater extends ArrayAdapter<Enregistrement> {

    public LocalRecordingAdaptater(Context context, ArrayList<Enregistrement> recording) {
        super(context, 0, recording);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Enregistrement loc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_enregistrement_list, parent, false);
        }
        TextView lRecording = (TextView) convertView.findViewById(R.id.lRecording);
        TextView lDate = (TextView) convertView.findViewById(R.id.lDate);

        // On change l'icone si c'est une vidéo, par défaut c'est l'icone d'image
        ImageView lIcon = (ImageView) convertView.findViewById(R.id.iconType);

        if(loc.type_recording == 0)
            lIcon.setImageResource(R.drawable.ic_video);


        lRecording.setText("Enregistrement "+loc.id);
        // On affiche la date
        lDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(loc.date)));

        return convertView;
    }

}
