package com.example.sbendakhlia.rapace.Activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sbendakhlia.rapace.R;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static android.content.Context.DOWNLOAD_SERVICE;

public class EnregistrementViewerActivity extends DialogFragment implements View.OnClickListener {// extends AppCompatActivity implements View.OnClickListener {

    private String url_res = "";
    private int id = -1;
    private int type_recording = 0;

    public EnregistrementViewerActivity() {

    }

    public static EnregistrementViewerActivity newInstance() {
        return new EnregistrementViewerActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // On récupère les info pour l'image/video a afficher et le numero de l'event
        url_res = this.getArguments().getString("url");
        id = new Integer(this.getArguments().getString("id"));
        type_recording = new Integer(this.getArguments().getString("type_recording"));

        return inflater.inflate(R.layout.activity_enregistrementviewer, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(getResources().getString(R.string.enregistrement));

        TextView title = (TextView) view.findViewById(R.id.titleDialog);
        title.setText(getResources().getString(R.string.enregistrementn)+id);


        Uri uri = Uri.parse(url_res);

        // https://stackoverflow.com/questions/25093546/android-os-networkonmainthreadexception-at-android-os-strictmodeandroidblockgua
        // Nécessaire pour pouvoir naviguer sur internet sur un thread à part, ici avec dialog
        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Si 0 alors on est video, sinon on est image
        if(type_recording == 0) {
            VideoView mVideoView = (VideoView) view.findViewById(R.id.videoView);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setMediaController(new MediaController(getContext()));
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
        } else if(type_recording == 1) {
            ImageView mImageView = (ImageView) view.findViewById(R.id.imageView);
            mImageView.setVisibility(View.VISIBLE);

            try {
                mImageView.setImageBitmap(BitmapFactory.decodeStream(new URL(url_res).openConnection().getInputStream()));
            } catch (IOException e) {
            }

        }


        // On active le bouton pour télécharger le fichier
        ImageButton btn_download = (ImageButton) view.findViewById(R.id.download);
        // On enregistre l'intentfilter pour redirigé quand le téléchargement est fini
        getContext().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        btn_download.setOnClickListener(this);



    }

    // Pour listen quand le téléchargement est fini
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Toast.makeText(context, getResources().getString(R.string.downloadended), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.download:
                // On lance le téléchargement dans un autre thread pour ne pas faire ralentir
                Toast.makeText(getContext(), getResources().getString(R.string.startDownload), Toast.LENGTH_SHORT).show();

                // On lance dans un autre thread
                // On fait une classe temporaire pour pouvoir avoir en paramètres le téléchargement
                class downloadThread implements Runnable {
                    private String url;
                    public downloadThread(String u) {
                        this.url = u;
                    }

                    public void run() {
                        File file =  null;
                        if(type_recording == 0)
                            file = new File(getContext().getExternalFilesDir(null), System.currentTimeMillis()+".mp4");
                        else
                            file = new File(getContext().getExternalFilesDir(null), System.currentTimeMillis()+".png");

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.url))
                                .setTitle("Enregistrement "+id)
                                .setDescription("Téléchargement")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                                .setDestinationUri(Uri.fromFile(file))
                                .setAllowedOverMetered(true)
                                .setAllowedOverRoaming(true);

                        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);

                    }
                };

                Thread thread = new Thread(new downloadThread(url_res));
                thread.start();

                break;
        }
    }
}
