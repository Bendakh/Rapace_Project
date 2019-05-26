package com.example.sbendakhlia.rapace.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sbendakhlia.rapace.Adapters.LocalEventAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalRecordingAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalVisualisationAdaptater;
import com.example.sbendakhlia.rapace.ForFireBase.Enregistrement;
import com.example.sbendakhlia.rapace.ForFireBase.Evenement;
import com.example.sbendakhlia.rapace.ForFireBase.Visualisation;
import com.example.sbendakhlia.rapace.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LocalMenuActivity extends AppCompatActivity implements View.OnClickListener {

    // Pour les test
    public int id;
    public boolean augmented;
    public int nombre_visualisation;


    // db
    private FirebaseDatabase database;

    private int idLocal = -1;
    private String nomLocal;

    // View
    private ListView lv;

    // Data
    private ArrayList<Enregistrement> recordings;
    private ArrayList<Evenement> evts;
    private ArrayList<Visualisation> vis;

    // Adapter
    private LocalRecordingAdaptater recordAdapter;
    private LocalEventAdaptater eventAdapter;
    private LocalVisualisationAdaptater visAdapter;

    // Info user
    public String uId = "";
    public String admin = "0";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_menu);

        // On récupère les informations du local qu'on visualise (son id et son nom, pour pouvoir rechercher dans la base de données)
        Intent r = getIntent();
        String raw = r.getStringExtra("local_raw");
        uId = r.getStringExtra("uId");
        admin = r.getStringExtra("admin");
        String[] parseLocal = raw.toString().split(";");

        // On ne fait rien si y a pas eu d'information via l'intent
        if(parseLocal.length < 2) {
            return;
        }

        idLocal = new Integer(parseLocal[0]);
        nomLocal = parseLocal[1];

        // On applique le nom sur la view
        TextView vName = (TextView) findViewById(R.id.lName);
        vName.setText(nomLocal);

        // On interroge la base de données pour récupérer les images/vidéos, et les evenements
        database = FirebaseDatabase.getInstance();
      /*  database.getReference("local_4").child("enregistrement").child("0").setValue(new Enregistrement(0, "http://test.com/test.mp4", 0, 1558276509));
        database.getReference("local_4").child("enregistrement").child("1").setValue(new Enregistrement(1, "http://test.com/test.png", 1, 1558276509));
        database.getReference("local_4").child("enregistrement").child("2").setValue(new Enregistrement(2, "http://test.com/test.mp4", 0, 1558276509));
        database.getReference("local_3").child("enregistrement").child("0").setValue(new Enregistrement(0, "http://test.com/test.mp4", 0, 1558276509));
*/
        // On va afficher les enregistrements dans une listview
        lv = (ListView) findViewById(R.id.historique_enregistrement);

        // On génère l'arraylist
        recordings = new ArrayList<>();
        // On créé l'adapter
        recordAdapter = new LocalRecordingAdaptater(
                this,
                recordings );
        // On l'affecte à la list view
        lv.setAdapter(recordAdapter);
        lv.setNestedScrollingEnabled(true);


        // On récupère les informations de la base de données des enregistrement
        DatabaseReference locauxDB = database.getReference("local_" + Integer.toString(idLocal)).child("enregistrement");
        locauxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Enregistrement tmpEnregistrement = dataSnapshot.getValue(Enregistrement.class);

                // On l'ajoute dans la liste
                recordings.add(tmpEnregistrement);
                // On en informe l'adapter
                recordAdapter.notifyDataSetChanged();
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

        // On active un retour de click sur la listeview enfant pour savoir quand on clique dessus
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Enregistrement recording_clicked = (Enregistrement) parent.getAdapter().getItem(position);

                /*
                Intent intent = new Intent(LocalMenuActivity.this, EnregistrementViewerActivity.class);
                // On envoie l'id et le nom du local à la prochaine activité
                intent.putExtra("url_res", recording_clicked.url_res);
                startActivity(intent);
                */

                // On fait un bundle pour envoyer des messages :p
                Bundle bundle = new Bundle();

                // On bundle url, id, type
                bundle.putString("url", recording_clicked.url_res );
                bundle.putString("id", Integer.toString(recording_clicked.id) );
                bundle.putString("type_recording", Integer.toString(recording_clicked.type_recording) );

                // On appelle le dialog fragment
                FragmentManager fm = getSupportFragmentManager();
                EnregistrementViewerActivity dialogFrag = EnregistrementViewerActivity.newInstance();

                // On lui rajoute le bundle en argument
                dialogFrag.setArguments(bundle);
                dialogFrag.show(fm, "activity_enregistrementviewer");




            }
        });

        // Ici on affiche la listes des evenements / alertes
      //  database.getReference("local_4").child("evenement").child("0").setValue(new Evenement(0, 0, "Une intrusion", 1558276509));
       // database.getReference("local_4").child("evenement").child("1").setValue(new Evenement(1, 0, "Une coupure de la transmission", 1558276509));

        // On va afficher les evt dans une listview
        ListView lv_evt = (ListView) findViewById(R.id.liste_evenement);

        // On génère l'arraylist
        evts = new ArrayList<>();
        // On créé l'adapter
        eventAdapter = new LocalEventAdaptater(
                this,
                evts );
        // On l'affecte à la list view
        lv_evt.setAdapter(eventAdapter);
        lv_evt.setNestedScrollingEnabled(true);

        // On récupère les informations de la base de données des evt
        locauxDB = database.getReference("local_" + Integer.toString(idLocal)).child("evenement");
        locauxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Evenement tmpEvent = dataSnapshot.getValue(Evenement.class);

                // On l'ajoute dans la liste
                evts.add(tmpEvent);
                // On en informe l'adapter
                eventAdapter.notifyDataSetChanged();
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

        // On va faire pour la visualisation

        // Normalement ici on fait une étape que si l'utilisateur n'est pas augmenté alors on affiche pas l'historique de visualisation

        // On va afficher la liste des visualisations

        // Ici numero du child = par rapport à l'utilisateur id
       // database.getReference("local_4").child("visualisation").child("0").setValue(new Visualisation(0, 0, 2, 1558276509));
       // database.getReference("local_4").child("visualisation").child("2").setValue(new Visualisation(2, 2, 1, 1558276509));




        // On va rajouter de 1 la visualisation dans la base ed données pour l'utilisateur actuel qui regarde.
        ListView lv_v = (ListView) findViewById(R.id.historique_visualisation);

        // On génère l'arraylist
        vis = new ArrayList<>();
        // On créé l'adapter
        visAdapter = new LocalVisualisationAdaptater(
                this,
                vis );
        // On l'affecte à la list view
        lv_v.setAdapter(visAdapter);
        lv_v.setNestedScrollingEnabled(true);


        augmented = (admin.equals("1")) ? true : false;

        System.out.println(admin + " - " +augmented);

        // Si on est pas augmenté, on affiche pas
        if(!augmented) {
            lv_v.setVisibility(View.GONE);
            findViewById(R.id.textVisu).setVisibility(View.GONE);
        }



        // On va ajouter les visualisations (qui seront affichés que si utilisateur augmenté) et augmenté la valeur actuelle de visualisation vu qu'on visualise une n-ieme fois

        // On actualise d'abord le nombre de visualisation vu que la l'utilisateur est en train de visualiser un local
        locauxDB = database.getReference("local_" + Integer.toString(idLocal)).child("visualisation").child(uId);
        locauxDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Visualisation tmpV = dataSnapshot.getValue(Visualisation.class);
                long newTime = System.currentTimeMillis();

                if(tmpV == null)
                    database.getReference("local_"+idLocal).child("visualisation").child(uId).setValue(new Visualisation(uId, uId, 1, newTime));
                else
                    database.getReference("local_"+idLocal).child("visualisation").child(uId).setValue(new Visualisation(uId, uId, tmpV.nb_times+1, newTime));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // On affiche la liste que si on est augmenté
        if(augmented) {
            locauxDB = database.getReference("local_" + Integer.toString(idLocal)).child("visualisation");
            locauxDB.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    Visualisation tmpV = dataSnapshot.getValue(Visualisation.class);


                    vis.add(tmpV);
                    visAdapter.notifyDataSetChanged();


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
        }
    }

    @Override
    public void onClick(View v) {

    }
}
