package com.example.sbendakhlia.rapace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.ForFireBase.Alerte;
import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.example.sbendakhlia.rapace.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AlerteAdaptater extends ArrayAdapter<Alerte> {

    public AlerteAdaptater(Context context, ArrayList<Alerte> locaux) {
        super(context, 0, locaux);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Alerte alerte = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_alerte_list, parent, false);
        }
        // Lookup view for data population
        TextView lEvt = (TextView) convertView.findViewById(R.id.lEvt);
        TextView lLocal = (TextView) convertView.findViewById(R.id.lLocal);
        TextView lDate = (TextView) convertView.findViewById(R.id.lDate);


        lEvt.setText(alerte.extras);
        lLocal.setText(alerte.localTmp.nom);
        lDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(alerte.date)));



        // Return the completed view to render on screen
        return convertView;
    }

}
