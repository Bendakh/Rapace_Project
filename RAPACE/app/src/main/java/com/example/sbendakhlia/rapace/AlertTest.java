package com.example.sbendakhlia.rapace;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AlertTest extends AppCompatActivity {

    Button testAlertbtn;
    User.AlertModes currentAlertMode;
    FirebaseAuth mAuth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_test);

        mAuth = FirebaseAuth.getInstance();
        currentAlertMode = User.AlertModes.ALARM;

        user = new FirebaseDataBaseHelper().GetUser(mAuth.getCurrentUser().getUid(), new FirebaseDataBaseHelper.FirebaseSuccessListener() {
            @Override
            public void onDataFound(boolean b) {
                currentAlertMode = user.getAlertMode();
                Log.e("AlertMode: ", currentAlertMode.toString());
            }
        });

        testAlertbtn = findViewById(R.id.test_alert);
        testAlertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentAlertMode)
                {
                    case ALARM:
                        ShowAlertNotification();
                        break;
                    case VIBRATION:
                        ShowVibrationNotification();
                        break;
                    case FLASH:
                        ShowFlashNotification();
                }
            }
        });
    }

    protected  void ShowAlertNotification()
    {
        long[] pattern = {100, 300, 300, 300};
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel", "Rapace", NotificationManager.IMPORTANCE_DEFAULT);

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            channel.setSound(alarmSound, att);
            channel.enableVibration(true);
            channel.setVibrationPattern(pattern);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AlertTest.this, "channel");
        mBuilder.setContentTitle("ALERT INTRUDER!");
        mBuilder.setContentText("An intruder has been detected!");
        mBuilder.setTicker("New message alert");
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        //mBuilder.setSound(alarmSound);
        //mBuilder.setVibrate(pattern);

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("com.example.sbendakhlia.rapace");
        }*/

        Intent intent = new Intent(AlertTest.this, NotifAction.class);
        intent.putExtra("notificationID", 111);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotifAction.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        nm.notify(111, mBuilder.build());
    }

    protected void ShowVibrationNotification()
    {
        long[] pattern = {100, 300, 300, 300};

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelv", "Rapace", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null,null);
            channel.enableVibration(true);
            channel.setVibrationPattern(pattern);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AlertTest.this, "channelv");
        mBuilder.setContentTitle("ALERT INTRUDER!");
        mBuilder.setContentText("An intruder has been detected!");
        mBuilder.setTicker("New message alert");
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        mBuilder.setDefaults(0);


        Intent intent = new Intent(AlertTest.this, NotifAction.class);
        intent.putExtra("notificationID", 112);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotifAction.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingIntent);


        nm.notify(112, mBuilder.build());
    }

    protected void ShowFlashNotification()
    {


        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channelf", "Rapace", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null,null);
            channel.enableVibration(false);
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AlertTest.this, "channelf");
        mBuilder.setContentTitle("ALERT INTRUDER!");
        mBuilder.setContentText("An intruder has been detected!");
        mBuilder.setTicker("New message alert");
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        mBuilder.setDefaults(0);



        nm.notify(113, mBuilder.build());
    }
}
