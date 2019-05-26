package com.example.sbendakhlia.rapace;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sbendakhlia.rapace.Activities.AlertePrecedente;
import com.example.sbendakhlia.rapace.Activities.LocauxLocation;
import com.example.sbendakhlia.rapace.Activities.MainActivityLocaux;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {


    ProgressDialog mDialog;
    VideoView vv;
    ImageButton btnPlayPause;
    TextView test;
    //EditText passwordChangeField;
    //Button changePasswordButton;
    FirebaseAuth mAuth;
    SharedPreferences mPreferences;

    Button settingsButton;
    Button userManagementButton;
    Button alerTest;

    //new user personnalisation
    //LinearLayout newUserLl;
    //EditText userNamePers;
    //EditText userMailPers;
    //EditText userPasswordPers;
    //Button userPersButton;
    boolean firstConnect;

    boolean admin = false;


    //We will use this variable to get the video from the server
    String videoUrl = "http://mic.duytan.edu.vn:86/FINAL.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.e("test","test");


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        test = findViewById(R.id.test);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                startActivity(settingsIntent);
            }
        });

        //newUserLl = findViewById(R.id.new_user_personalise);
        //userNamePers = findViewById(R.id.user_name_pers);
        //userMailPers = findViewById(R.id.user_mail_pers);
        //userPasswordPers = findViewById(R.id.user_password_pers);
        //userPersButton = findViewById(R.id.confirm_user_pers);

        //newUserLl.setVisibility(View.INVISIBLE);

        Bundle data = getIntent().getExtras();
        if(data == null)
        {
            return;
        }
        String name = data.getString("_name");
        String email = data.getString("_email");
        String id = data.getString("_id");
        admin = data.getBoolean("_admin");
        int nDays = data.getInt("_nDays");
        String lastChange = data.getString("_lastChange");
        firstConnect = data.getBoolean("_firstConnect");
        final String password = data.getString("_password");

        mPreferences.edit().putInt("ndays", nDays).putString("lastChange",lastChange).apply();

        //Normal user fist connect( true if first connexion has been done, else false )
        if(!admin && !firstConnect)
        {
            //newUserLl.setVisibility(View.VISIBLE);
            DisplayFirstConnectDialog(password);
        }

        /*userPersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newName = userNamePers.getText().toString();
                final String newMail = userMailPers.getText().toString();
                final String newPassword = userPasswordPers.getText().toString();

                if(newName.isEmpty() || newMail.isEmpty() || newPassword.isEmpty())
                {
                    ShowMessage("Please enter all details!");
                    return;
                }

                else
                {
                    AuthCredential ac = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(),password);

                    mAuth.getCurrentUser().reauthenticate(ac).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {

                                new FirebaseDataBaseHelper().UpdateUser(mAuth.getCurrentUser().getUid(), newName, newMail, userPasswordPers.getText().toString(), new FirebaseDataBaseHelper.DataStatus() {
                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {
                                        mAuth.getCurrentUser().updateEmail(newMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mAuth.getCurrentUser().updatePassword(userPasswordPers.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        ShowMessage("User updated");
                                                    }
                                                });
                                            }
                                        });

                                    }

                                    @Override
                                    public void DataIsDeleted() {

                                    }
                                });
                                firstConnect = true;
                                newUserLl.setVisibility(View.INVISIBLE);
                            }

                        }
                    });



                }
            }
        });*/


        //Password change check
        try
        {
            long value = User.GetNumberOfDaysSinceLastPasswordChange(lastChange);
            mPreferences.edit().putLong("daysSinceChange",value).apply();
            if(value >= (nDays - 5))
            {
                //Propose password change
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Password change")
                        .setMessage("Do you want to change your password? You'll be forced to change it in five days!")
                        .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent newIntent = new Intent(MainActivity.this, PasswordChange.class);
                                startActivity(newIntent);
                            }
                        });
                builder.create();
            }
            else if(value >= nDays)
            {
                Intent newIntent = new Intent(MainActivity.this, PasswordChange.class);
                startActivity(newIntent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        test.setText(name + " " + email + " " + id + " " + admin + " " + password);
        userManagementButton = findViewById(R.id.users_management_button);
        if(!admin)
        {
            userManagementButton.setVisibility(View.INVISIBLE);
        }
        else if(admin)
        {
            userManagementButton.setVisibility(View.VISIBLE);
            userManagementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, UserManagement.class);
                    startActivity(intent);
                }
            });
        }
        alerTest = findViewById(R.id.go_to_alertTest);
        alerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlertTest.class);
                startActivity(intent);
            }
        });

        // partie locaux
        Button goToLocaux = findViewById((R.id.go_to_Local));
        goToLocaux.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, MainActivityLocaux.class);
                        // On va envoyer le id
                        intent.putExtra("id", mAuth.getCurrentUser().getUid());
                        intent.putExtra("admin", (admin) ? "1": "0");
                        startActivity(intent);
                    }
                }
        );

        // partie alerte précédente

        Button goToAlerte = findViewById((R.id.go_to_AlertPrecedent));
        goToAlerte.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent intent = new Intent(MainActivity.this, AlertePrecedente.class);
                                              // On va envoyer le id
                                              intent.putExtra("id", mAuth.getCurrentUser().getUid());
                                              intent.putExtra("admin", (admin) ? "1": "0");
                                              startActivity(intent);
                                          }
                                      }
        );

        /*vv = findViewById(R.id.video_view);
        btnPlayPause = findViewById(R.id.btn_play_pause);

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new ProgressDialog(MainActivityLocaux.this);
                mDialog.setMessage("Waiting");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                try {
                    if (!vv.isPlaying())
                    {
                        Uri uri = Uri.parse(videoUrl);
                        vv.setVideoURI(uri);
                        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            btnPlayPause.setImageResource(R.drawable.ic_play);
                        }
                    });
                    }
                    else
                    {
                        vv.pause();
                        btnPlayPause.setImageResource(R.drawable.ic_play);
                    }
                }
                catch(Exception ex)
                {
                    //Whatever
                }
                vv.requestFocus();
                vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mDialog.dismiss();
                        mp.setLooping(true);
                        vv.start();
                        btnPlayPause.setImageResource(R.drawable.ic_pause);
                    }
                });
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        long value = mPreferences.getLong("daysSinceChange",0);
        int nDays = mPreferences.getInt("ndays",0);

        if(value >= nDays)
        {
            Intent newIntent = new Intent(MainActivity.this, PasswordChange.class);
            startActivity(newIntent);
        }
    }

    private void ShowMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void DisplayFirstConnectDialog(final String currentPassword)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Personalise your logins");
        alertDialog.setMessage("Please fill all your new informations");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.first_connect_dialog, null);

        final EditText userNamePers = layout_pwd.findViewById(R.id.user_name_pers);
        final EditText userMailPers = layout_pwd.findViewById(R.id.user_mail_pers);
        final EditText userPasswordPers = layout_pwd.findViewById(R.id.user_password_pers);
        alertDialog.setView(layout_pwd);

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final String newName = userNamePers.getText().toString();
                final String newMail = userMailPers.getText().toString();
                final String newPassword = userPasswordPers.getText().toString();

                if(newName.isEmpty() || newMail.isEmpty() || newPassword.isEmpty())
                {
                    ShowMessage("Please enter all details!");
                    DisplayFirstConnectDialog(currentPassword);
                }

                else
                {
                    AuthCredential ac = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(),currentPassword);

                    mAuth.getCurrentUser().reauthenticate(ac).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {

                                new FirebaseDataBaseHelper().UpdateUser(mAuth.getCurrentUser().getUid(), newName, newMail, userPasswordPers.getText().toString(), new FirebaseDataBaseHelper.DataStatus() {
                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {
                                        mAuth.getCurrentUser().updateEmail(newMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mAuth.getCurrentUser().updatePassword(userPasswordPers.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        ShowMessage("User updated");
                                                    }
                                                });
                                            }
                                        });

                                    }

                                    @Override
                                    public void DataIsDeleted() {

                                    }
                                });
                                firstConnect = true;
                                dialogInterface.dismiss();
                            }

                        }
                    });



                }

            }
        //Fin de setpositive
        });
        alertDialog.show();
    }
}
