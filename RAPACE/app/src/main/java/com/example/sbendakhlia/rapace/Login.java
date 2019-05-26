package com.example.sbendakhlia.rapace;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Activity {

    private EditText login;
    private EditText password;
    private CardView loginButton;
    private int counter = 5;
    User user;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            //Profile activity
        }
        login = (EditText) findViewById(R.id.login_field);
        password = (EditText) findViewById(R.id.password_field);
        loginButton = (CardView) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateCredentials(login.getText().toString(),password.getText().toString());
            }
        });
    }

    private void ValidateCredentials(String login, String password)
    {
        /*if((login=="Admin") && (password =="Admin"))
        {
            Intent intent = new Intent(Login.this, MainActivityLocaux.class);
            startActivity(intent);
        }
        else
        {
            counter--;
            if(counter <= 0){
                loginButton.setEnabled(false);
            }
        }*/

        if(login.isEmpty() || password.isEmpty())
        {
            ShowMessage("Please enter credentials!");
            return;
        }
        else
        {

            mAuth.signInWithEmailAndPassword(login,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        //Start Activity
                        //System.out.println(mAuth.getCurrentUser().getUid());
                            user = new FirebaseDataBaseHelper().GetUser(mAuth.getCurrentUser().getUid(), new FirebaseDataBaseHelper.FirebaseSuccessListener() {
                            @Override
                            public void onDataFound(boolean b) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("_name", user.getName());
                                intent.putExtra("_id", user.getId());
                                intent.putExtra("_email", user.getEmail());
                                intent.putExtra("_admin", user.isAdmin());
                                intent.putExtra("_nDays", user.getnDays());
                                intent.putExtra("_lastChange",user.getLastChangedPasswordDate());
                                intent.putExtra("_alertMode",user.getAlertMode());
                                intent.putExtra("_firstConnect", user.isFirstConnect());
                                intent.putExtra("_password", user.getPassword());
                              //  Log.e("E",user.getPassword());
                                //ShowMessage("Done");
                                startActivity(intent);
                            }
                        });
                        //System.out.println(user.getName());
                        /*if(user != null) {
                            Intent intent = new Intent(Login.this, MainActivityLocaux.class);
                            intent.putExtra("_name", user.getName());
                            intent.putExtra("_id", user.getId());
                            intent.putExtra("_email", user.getEmail());
                            intent.putExtra("_admin", user.isAdmin());
                            ShowMessage("Done");
                            startActivity(intent);
                        }
                        else
                        {
                            ShowMessage("User not found!");
                        }*/
                            ShowMessage("User logged in!");
                        }
                }
            });
        }

    }

    private void ShowMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
