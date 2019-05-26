package com.example.sbendakhlia.rapace.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.Activities.LocalMenuActivity;
import com.example.sbendakhlia.rapace.ForDataTransit.LocalByZone;
import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.example.sbendakhlia.rapace.R;

import java.util.ArrayList;


// source https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class LocalByZoneAdaptater extends ArrayAdapter<LocalByZone> {
    public LocalAdaptater locauxAdapterNormal;

    public LocalByZoneAdaptater(Context context, ArrayList<LocalByZone> locaux_zone) {
        super(context, 0, locaux_zone);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LocalByZone loc = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_list_mode2, parent, false);
        }
        // Lookup view for data population
        ListView lv_locaux = (ListView) convertView.findViewById(R.id.list_locaux2);
        TextView lZone = (TextView) convertView.findViewById(R.id.lZone);
        // Populate the data into the template view using the data object
        lZone.setText(this.getContext().getResources().getString(R.string.zoneName)+ Integer.toString(loc.zone));

        // On va rajouter en listView les locaux de la zone

        // On active le nestedscrolling la aussi
        lv_locaux.setNestedScrollingEnabled(true);


        // On recupère les locaux et on met dans l'adapter avec police d'écriture un peu plus petite
        locauxAdapterNormal = new LocalAdaptater(
                this.getContext(),
                loc.locaux,
                16);

        lv_locaux.setAdapter(locauxAdapterNormal);

        // On cache par défaut la listview enfant, qui s'activera quand on clique sur la listview parent
        lv_locaux.setVisibility(View.VISIBLE);


        View.OnClickListener clickZone = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lv = (ListView) v.findViewById(R.id.list_locaux2);
                if(lv.getVisibility() == View.GONE)
                    lv.setVisibility(View.VISIBLE);
                else
                    lv.setVisibility(View.GONE);
            }

        };

        convertView.setOnClickListener(clickZone);

        // On active un retour de click sur la listeview enfant pour savoir quand on clique dessus
        lv_locaux.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Local local_clicked = (Local) parent.getAdapter().getItem(position);

                Intent intent = new Intent(getContext(), LocalMenuActivity.class);
                // On envoie l'id et le nom du local à la prochaine activité
                intent.putExtra("local_raw", Integer.toString(local_clicked.id)+";"+local_clicked.nom);
                intent.putExtra("uId", local_clicked.uId);
                intent.putExtra("admin", local_clicked.admin);
                getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


    // Si ces valeurs ont changés pour le listView du LocalByZone, alors on va aussi rafraichir pour les listView enfants des Local
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(locauxAdapterNormal != null)
            locauxAdapterNormal.notifyDataSetChanged();

    }





}
