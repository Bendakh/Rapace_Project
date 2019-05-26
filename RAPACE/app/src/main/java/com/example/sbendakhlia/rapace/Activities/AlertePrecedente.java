package com.example.sbendakhlia.rapace.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sbendakhlia.rapace.Adapters.AlerteAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalByZoneAdaptater;
import com.example.sbendakhlia.rapace.ForDataTransit.LocalByZone;
import com.example.sbendakhlia.rapace.ForFireBase.Alerte;
import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.example.sbendakhlia.rapace.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AlertePrecedente extends AppCompatActivity implements View.OnClickListener {

    // Constantes
    public final static int maxValueBeforeMode2 = 4;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // Variables
    public ArrayList<Alerte> alertes;
    public ArrayList<Long> locauxCanCheck;

    // Adaptater du mode normal
    public AlerteAdaptater alerteAdapter;


    // ListView
    public ListView lv_alerte;

    // info concernant l'user
    public String uId = "";
    public String admin= "0";

    public FirebaseDatabase database;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alerteprecedente);

        // on récupère l'id
        uId = getIntent().getStringExtra("id");
        admin = getIntent().getStringExtra("admin");



        // On récupère les données des locaux
        database = FirebaseDatabase.getInstance();



        lv_alerte = (ListView) findViewById(R.id.list_alerte);

        // On génère les deux listview pour les deux menus d'affichage de locaux
        alertes = new ArrayList<>();
        alerteAdapter = new AlerteAdaptater(
                this,
                alertes );


        lv_alerte.setNestedScrollingEnabled(true);

        lv_alerte.setAdapter(alerteAdapter);

        // On active le retour de click
        lv_alerte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Alerte alerte_clicked = (Alerte) parent.getAdapter().getItem(position);

                Intent intent = new Intent(view.getContext(), AlerteMenuActivity.class);
                // On envoie l'id et le nom du local à la prochaine activité
                intent.putExtra("local_raw", Integer.toString(alerte_clicked.id)+";"+Integer.toString(alerte_clicked.localTmp.id)+";"+alerte_clicked.extras);
                intent.putExtra("uId", uId);
                intent.putExtra("admin", admin);

                view.getContext().startActivity(intent);
            }
        });




        // On retrieve les informations de la base de données
        // On récupère les locauxx que l'utilisateur peut regarder
        locauxCanCheck = new ArrayList<>();
        DatabaseReference getLocal = database.getReference("Users").child(uId).child("local");
        getLocal.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                locauxCanCheck.add((Long) dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        // On fait un customchildlistener pour pouvoir passer le local en  paramètre comme ça on peut le récupérer pour les alertes
        class CustomChildListener implements ChildEventListener{
            Local tmpLocal;

            public CustomChildListener(Local tmp) {
                tmpLocal = tmp;
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // On rajoute les alertes et on notifie le changement
                Alerte tmpAlerte = dataSnapshot.getValue(Alerte.class);
                tmpAlerte.addLocal(tmpLocal);
                alertes.add(tmpAlerte);

                alerteAdapter.notifyDataSetChanged();
            }

            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        // On va récupérer les alertes des locaux qu'il peut voir
        DatabaseReference locauxDB = database.getReference("locaux");
        locauxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Local tmpLocal = dataSnapshot.getValue(Local.class);

                // Seulement si c'est son local
                if(locauxCanCheck.contains(new Long(tmpLocal.id))) {
                    DatabaseReference alerteDB = database.getReference("local_" + Integer.toString(tmpLocal.id)).child("evenement");
                    alerteDB.addChildEventListener(new CustomChildListener(tmpLocal));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });







        // On s'occupe du bouton mmaintenant
        ImageButton btnGPS = (ImageButton) findViewById(R.id.btnGPS);
        btnGPS.setOnClickListener(this);


    }


    // https://stackoverflow.com/questions/31016722/googleplayservicesutil-vs-googleapiavailability
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGPS:
                Intent i = new Intent(this, LocauxLocation.class);

                StringBuffer rawText = new StringBuffer();
                for(Alerte c : alertes) {
                    rawText.append(c.extras+"|"+c.id+"|"+c.localTmp.lat+"|"+c.localTmp.longt+"|"+c.localTmp.id+";");
                }

                i.putExtra("position_locaux_raw", rawText.toString());
                i.putExtra("uId", uId);
                i.putExtra("admin", admin);
                i.putExtra("type", "alerte");

                // On lance l'activité que si on a les services google play
                if(checkPlayServices())
                    startActivity(i);

                break;

            default: break;
        }
    }


}
