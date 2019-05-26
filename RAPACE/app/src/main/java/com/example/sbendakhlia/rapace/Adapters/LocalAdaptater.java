package com.example.sbendakhlia.rapace.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.example.sbendakhlia.rapace.R;

import java.util.ArrayList;

// source https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class LocalAdaptater extends ArrayAdapter<Local> {
    public float fontSize;

    public LocalAdaptater(Context context, ArrayList<Local> locaux) {
        this(context, locaux, 26);
    }

    public LocalAdaptater(Context context, ArrayList<Local> locaux, int fontSize) {
        super(context, 0, locaux);
        this.fontSize = fontSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Local loc = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_list, parent, false);
        }
        // Lookup view for data population
        TextView lName = (TextView) convertView.findViewById(R.id.lName);
        TextView lZone = (TextView) convertView.findViewById(R.id.lZone);

        // En fonction du paramètre du localAdaptater on change la taille des textes
        lName.setTextSize(fontSize);
        lZone.setTextSize(fontSize);

        // Populate the data into the template view using the data object
        lName.setText(loc.nom);
        lZone.setText(Integer.toString(loc.zone));
        // Return the completed view to render on screen
        return convertView;
    }

}
