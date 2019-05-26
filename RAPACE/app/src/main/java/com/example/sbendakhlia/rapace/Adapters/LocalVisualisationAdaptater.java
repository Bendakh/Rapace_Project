package com.example.sbendakhlia.rapace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.ForFireBase.Visualisation;
import com.example.sbendakhlia.rapace.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalVisualisationAdaptater extends ArrayAdapter<Visualisation> {

    public LocalVisualisationAdaptater(Context context, ArrayList<Visualisation> evt) {
        super(context, 0, evt);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Visualisation loc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_visualisation_list, parent, false);
        }
        TextView lUser = (TextView) convertView.findViewById(R.id.lUser);

        //lUser.setText("Utilisateur "+loc.id_user+" a consulté "+loc.nb_times+" fois, le "+new SimpleDateFormat("yyyy-MM-dd").format(new Date(loc.date_lasttime)));
        lUser.setText(loc.id_user+" a consulté "+loc.nb_times+" fois, le "+new SimpleDateFormat("yyyy-MM-dd").format(new Date(loc.date_lasttime)));

        return convertView;
    }

}
