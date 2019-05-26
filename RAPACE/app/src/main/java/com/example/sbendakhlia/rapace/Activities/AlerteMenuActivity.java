package com.example.sbendakhlia.rapace.Activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sbendakhlia.rapace.Adapters.LocalEventAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalRecordingAdaptater;
import com.example.sbendakhlia.rapace.Adapters.LocalVisualisationAdaptater;
import com.example.sbendakhlia.rapace.ForFireBase.Alerte;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class AlerteMenuActivity extends AppCompatActivity implements View.OnClickListener {



    // db
    private FirebaseDatabase database;

    private int idLocal = -1;
    private int idAlerte = -1;
    private String nomLocal;

    public String url_res = "";


    // Info user
    public String uId = "";
    public String admin = "0";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerte_menu);

        // On récupère les informations du local qu'on visualise (son id et son nom, pour pouvoir rechercher dans la base de données)
        Intent r = getIntent();
        String raw = r.getStringExtra("local_raw");
        uId = r.getStringExtra("uId");
        admin = r.getStringExtra("admin");
        String[] parseLocal = raw.toString().split(";");



        idLocal = new Integer(parseLocal[1]);
        idAlerte = new Integer(parseLocal[0]);
        nomLocal = parseLocal[2];

        // On applique le nom sur la view
        TextView vName = (TextView) findViewById(R.id.lName);
        vName.setText(nomLocal);



        database = FirebaseDatabase.getInstance();

        // ici on récupère le nom du local
        DatabaseReference localDB = database.getReference("locaux").child("local_" + Integer.toString(idLocal)).child("nom");
        localDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView vLoc = findViewById(R.id.lLocal);
                vLoc.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // On active le bouton pour télécharger le fichier
        ImageButton btn_download = (ImageButton) findViewById(R.id.download);
        btn_download.setOnClickListener(this);

        // On va récupérer les informations concernant l'alerte

        DatabaseReference alerteDB = database.getReference("local_" + Integer.toString(idLocal)).child("evenement");
        alerteDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Alerte tmpAlerte = dataSnapshot.getValue(Alerte.class);

                // Si ça correspond à notre alerte alors on affiche ça
                if(tmpAlerte.id == idAlerte) {
                    TextView vAct = findViewById(R.id.lAction);
                    vAct.setText(tmpAlerte.action);

                    url_res = tmpAlerte.image;
                    Uri uri = Uri.parse(url_res);

                    ImageView mImageView = (ImageView) findViewById(R.id.imageView);
                    int SDK_INT = Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }
                    try {
                        mImageView.setImageBitmap(BitmapFactory.decodeStream(new URL(url_res).openConnection().getInputStream()));
                    } catch (IOException e) {
                    }
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




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.download:
                // Si pas d'image chargé on lance pas
                if(url_res.equals(""))
                    break;

                // On lance le téléchargement dans un autre thread pour ne pas faire ralentir
                Toast.makeText(this, getResources().getString(R.string.startDownload), Toast.LENGTH_SHORT).show();

                // On lance dans un autre thread
                // On fait une classe temporaire pour pouvoir avoir en paramètres le téléchargement
                class downloadThread implements Runnable {
                    private String url;
                    public downloadThread(String u) {
                        this.url = u;
                    }

                    public void run() {
                        File file =  null;
                        file = new File(getExternalFilesDir(null), System.currentTimeMillis()+".png");

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.url))
                                .setTitle("Alerte")
                                .setDescription("Téléchargement")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                .setDestinationUri(Uri.fromFile(file))
                                .setAllowedOverMetered(true)
                                .setAllowedOverRoaming(true);

                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);

                    }
                };

                Thread thread = new Thread(new downloadThread(url_res));
                thread.start();

                break;
        }
    }
}
