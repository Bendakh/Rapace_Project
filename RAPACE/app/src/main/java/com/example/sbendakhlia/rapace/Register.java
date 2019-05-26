package com.example.sbendakhlia.rapace;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

public class Register extends Activity {

    private EditText name_reg;
    private EditText mail_reg;
    private EditText password_reg;
    private EditText password_confirm;
    private ProgressBar loadingProgress;
    private CardView regBtn;
    private CheckBox admin;
    private Button goToLoginButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name_reg = findViewById(R.id.name_reg);
        admin = findViewById(R.id.admin);
        mail_reg = findViewById(R.id.mail_reg);
        password_reg = findViewById(R.id.password_reg);
        password_confirm = findViewById(R.id.password_confirm);
        loadingProgress = findViewById(R.id.loading_progress);
        goToLoginButton = findViewById(R.id.go_to_login_button);

        loadingProgress.setVisibility(View.INVISIBLE);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        regBtn = findViewById(R.id.register_button);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                final String nameString = name_reg.getText().toString();
                final String emailString = mail_reg.getText().toString();
                final String passwordString = password_reg.getText().toString();
                final String passwordConfirmString = password_confirm.getText().toString();
                final boolean adminBool = admin.isChecked();

                if(nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || passwordConfirmString.isEmpty() || !passwordString.equals(passwordConfirmString))
                {
                    //Afficher un message d'erreur
                    ShowMessage("Please verify all fields!");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //Register
                    CreateUserAccount(nameString,emailString,passwordString,adminBool);
                }
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);

            }
        });
    }

    private void CreateUserAccount(final String nameString, final String emailString, final String passwordString, final boolean adminBool) {

        mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    //UpdateUserInfo(nameString,adminBool,mAuth.getCurrentUser());
                    User newUser = new User();
                    newUser.setId(mAuth.getUid());
                    newUser.setAdmin(adminBool);
                    newUser.setName(nameString);
                    newUser.setEmail(emailString);
                    newUser.setPassword(passwordString);

                    new FirebaseDataBaseHelper().AddUser(newUser, new FirebaseDataBaseHelper.DataStatus() {


                        @Override
                        public void DataIsInserted() {
                            ShowMessage("Account Created!");
                            Intent loginIntent = new Intent(Register.this, Login.class);
                            startActivity(loginIntent);
                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
                else
                {
                    ShowMessage("Error " + task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }



    private void ShowMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}
