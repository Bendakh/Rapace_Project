package com.example.sbendakhlia.rapace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.ForFireBase.Evenement;
import com.example.sbendakhlia.rapace.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalEventAdaptater extends ArrayAdapter<Evenement> {

    public LocalEventAdaptater(Context context, ArrayList<Evenement> evt) {
        super(context, 0, evt);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evenement loc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_evenement_list, parent, false);
        }
        TextView lRecording = (TextView) convertView.findViewById(R.id.lEvt);
        TextView lDate = (TextView) convertView.findViewById(R.id.lDate);


        // Par défaut on va dire une alerte

        lRecording.setText("Alerte - "+loc.extras);
        // On affiche la date
        lDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(loc.date)));

        return convertView;
    }

}
