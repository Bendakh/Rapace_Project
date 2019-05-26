package com.example.sbendakhlia.rapace.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.sbendakhlia.rapace.ForDataTransit.LocalSimplifie;
import com.example.sbendakhlia.rapace.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

;

public class LocauxLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LocalSimplifie> listLocaux;

    public boolean isLocal = true; // Pour savoir si on affiche des locaux ou des alertes


    public String uId = "";
    public String admin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locaux_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listLocaux = new ArrayList<>();

        // On récupère les informations via intent
        Intent r = getIntent();

        String raw = r.getStringExtra("position_locaux_raw");
        uId = r.getStringExtra("uId");
        admin = r.getStringExtra("admin");

        isLocal = (r.getStringExtra("type").equals("alerte")) ? false : true;


        if(raw != null) {
            String[] parseLocaux = raw.toString().split(";");
            for (String s : parseLocaux) {
                String[] parseInfo = s.split("\\|");
                if(isLocal)
                    listLocaux.add(new LocalSimplifie(parseInfo[0], new Integer(parseInfo[1]), new Float(parseInfo[2]), new Float(parseInfo[3])));
                else
                    listLocaux.add(new LocalSimplifie(parseInfo[0], new Integer(parseInfo[4]), new Float(parseInfo[2]), new Float(parseInfo[3]), new Integer(parseInfo[1])));
            }
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Pour tous les locaux on va rajouter un marqueur

        LatLng lastPos = null;

        for(LocalSimplifie l : listLocaux) {
            LatLng pos_loc = new LatLng(l.lat, l.longt);
            // On stock la nouvelle position pour après pouvoir zoomer dessus
            lastPos = pos_loc;

            // On rajoute un marqueur
            if(isLocal)
                mMap.addMarker(new MarkerOptions().position(pos_loc).title(l.nom)).setTag(l.id);
            else {
                ArrayList<Integer> infos = new ArrayList<>();
                infos.add(l.id);
                infos.add(l.id_extras);
                mMap.addMarker(new MarkerOptions().position(pos_loc).title(l.nom)).setTag(infos);
            }

        }

        // Et on va bouger la camera sur le dernier marqueur
        if(lastPos != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastPos));
            // On anime le mouvement
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }

        // On rajoute un listener sur le texte du marker pour ramener sur le local qui est cliqué
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                if(isLocal) {
                    // On recupere l'id du local qui a été cliqué dessus
                    int idLocalClicked = (Integer) arg0.getTag();
                    String nomLocalClicked = arg0.getTitle();

                    // Et on va appeler l'activité du menu
                    Intent intent = new Intent(LocauxLocation.this, LocalMenuActivity.class);
                    // On envoie l'id et le nom du local à la prochaine activité
                    intent.putExtra("local_raw", Integer.toString(idLocalClicked) + ";" + nomLocalClicked);
                    intent.putExtra("uId", uId);
                    intent.putExtra("admin", admin);

                    startActivity(intent);
                } else {
                    // On recupere l'id du local qui a été cliqué dessus
                    ArrayList<Integer> idLocalClicked = (ArrayList<Integer>) arg0.getTag();
                    String nomLocalClicked = arg0.getTitle();

                    // Et on va appeler l'activité du menu
                    Intent intent = new Intent(LocauxLocation.this, AlerteMenuActivity.class);
                    // On envoie l'id et le nom du local à la prochaine activité
                    intent.putExtra("local_raw", Integer.toString(idLocalClicked.get(1))+";"+Integer.toString(idLocalClicked.get(0))+";"+nomLocalClicked);
                    intent.putExtra("uId", uId);
                    intent.putExtra("admin", admin);

                    startActivity(intent);
                }
            }
        });
    }
}
