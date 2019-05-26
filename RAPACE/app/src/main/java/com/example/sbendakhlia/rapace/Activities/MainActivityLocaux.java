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

import com.example.sbendakhlia.rapace.Adapters.LocalAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalByZoneAdaptater;
import com.example.sbendakhlia.rapace.ForDataTransit.LocalByZone;
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


public class MainActivityLocaux extends AppCompatActivity implements View.OnClickListener {

    // Constantes
    public final static int maxValueBeforeMode2 = 4;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // Variables
    public ArrayList<Local> locaux;
    public ArrayList<LocalByZone> locauxZone;
    public ArrayList<Long> locauxCanCheck;

    // Adaptater du mode normal
    public LocalAdaptater locauxAdapterNormal;

    // Adaptater du mode par type
    public LocalByZoneAdaptater locauxAdapterZone;

    // ListView
    public ListView lv_locaux;

    // info concernant l'user
    public String uId = "";
    public String admin= "0";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listlocaux);

        // on récupère l'id
        uId = getIntent().getStringExtra("id");
        admin = getIntent().getStringExtra("admin");


        // On récupère les données des locaux
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        //DatabaseReference myRef = database.getReference("message");
      /*  database.getReference("locaux").child("LIRMM").setValue(new Local("LIRMM", 1, 2, 1, 43.63684f, 3.840350f));
        database.getReference("locaux").child("Fac de Sciences").setValue(new Local("Fac de Sciences", 2, 2, 1, 43.631574f, 3.863330f));
        database.getReference("locaux").child("Université Paul Valery").setValue(new Local("Université Paul Valery", 3, 1, 1, 43.633011f, 3.870143f));
        database.getReference("locaux").child("Aeroport").setValue(new Local("Aeroport", 4, 3, 2, 43.584662f, 3.969008f));
        database.getReference("locaux").child("Cinema CGR Lattes").setValue(new Local("Cinema CGR Lattes", 5, 2, 2, 43.584704f, 3.935444f));*/

        lv_locaux = (ListView) findViewById(R.id.list_locaux);

        // On génère les deux listview pour les deux menus d'affichage de locaux
        locaux = new ArrayList<>();
        locauxAdapterNormal = new LocalAdaptater(
                this,
                locaux );



        locauxZone = new ArrayList<>();
        locauxAdapterZone = new LocalByZoneAdaptater(
                this,
                locauxZone
        );

        // On active le nestedscroll pour pouvoir scroller avec des listview dans des listviews
        lv_locaux.setNestedScrollingEnabled(true);

        // On cache le nom/categorie
        showFirstMenu();

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


        DatabaseReference locauxDB = database.getReference("locaux");
        locauxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Local tmpLocal = dataSnapshot.getValue(Local.class);
                tmpLocal.addUsrInfo(uId, admin);

                // On ajoute que si il a le droit de voir les locaux

                if(locauxCanCheck.contains(new Long(tmpLocal.id)) ) {

                    // On l'ajoute dans la liste des locaux normaux
                    locaux.add(tmpLocal);

                    // Et on l'ajoute aussi dans la liste des locaux par zone
                    LocalByZone lbyzone = null;
                    for (LocalByZone lz : locauxZone) {
                        if (lz.zone == tmpLocal.zone) {
                            lbyzone = lz;
                            break;
                        }
                    }
                    if (lbyzone == null) {
                        lbyzone = new LocalByZone(tmpLocal.zone, new ArrayList<Local>());
                        locauxZone.add(lbyzone);
                    }
                    lbyzone.locaux.add(tmpLocal);

                    locauxAdapterZone.notifyDataSetChanged();
                    locauxAdapterNormal.notifyDataSetChanged();

                    // Faudrait le faire qu'une fois
                    if (locaux.size() > maxValueBeforeMode2) {
                        showSecondMenu();
                    }


                    // Affichage du bouton GPS
                    findViewById(R.id.btnGPS).setVisibility(View.VISIBLE);

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

        // Par défaut le bouton sera invisible et sera visible lors de l'affichage du premier local
        btnGPS.setVisibility(View.INVISIBLE);

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
                for(Local c : locaux) {
                    rawText.append(c.nom+"|"+c.id+"|"+c.lat+"|"+c.longt+";");
                }

                i.putExtra("position_locaux_raw", rawText.toString());
                i.putExtra("uId", uId);
                i.putExtra("admin", admin);
                i.putExtra("type", "local");

                //i.putExtra("position_locaux_raw", "Local 1|43.599|3.8755;Un truc a montpellier|44.599|3.8755;");

                // On lance l'activité que si on a les services google play
                if(checkPlayServices())
                    startActivity(i);

                break;

            default: break;
        }
    }

    public void showFirstMenu() {
        LinearLayout rowTitle = (LinearLayout) findViewById(R.id.titleLoc);
        rowTitle.setVisibility(View.VISIBLE);

        lv_locaux.setAdapter(locauxAdapterNormal);

        // On active le retour de click
        lv_locaux.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Local local_clicked = (Local) parent.getAdapter().getItem(position);

                Intent intent = new Intent(view.getContext(), LocalMenuActivity.class);
                // On envoie l'id et le nom du local à la prochaine activité
                intent.putExtra("local_raw", Integer.toString(local_clicked.id)+";"+local_clicked.nom);
                intent.putExtra("uId", local_clicked.uId);
                intent.putExtra("admin", local_clicked.admin);

                view.getContext().startActivity(intent);
            }
        });
    }

    public void showSecondMenu() {
        LinearLayout rowTitle = (LinearLayout) findViewById(R.id.titleLoc);
        rowTitle.setVisibility(View.INVISIBLE);

        lv_locaux.setAdapter(locauxAdapterZone);

        lv_locaux.setOnItemClickListener(null);
    }
}
