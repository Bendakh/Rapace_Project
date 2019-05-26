package com.example.sbendakhlia.rapace;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserManagement extends AppCompatActivity {

    Button addUserButton;
    //LinearLayout ll;
    //EditText userName;

    //EditText userMail;
    //EditText userPassword;
    //EditText userPasswordConfirm;
    //Button userConfirm;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        addUserButton = findViewById(R.id.add_a_user_button);
        //ll = findViewById(R.id.new_user_interface);
        //userName = findViewById(R.id.user_name);

        //userMail = findViewById(R.id.user_mail);
        //userPassword = findViewById(R.id.user_password);
        //userPasswordConfirm = findViewById(R.id.user_password_confirm);
        //userConfirm = findViewById(R.id.confirm_user);


        //ll.setVisibility(View.INVISIBLE);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ll.setVisibility(View.VISIBLE);
                DisplayUserAdd();
                addUserButton.setVisibility(View.INVISIBLE);
            }
        });
        /*userConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameString = userName.getText().toString();
                final String emailString = userMail.getText().toString();
                final String passwordString = userPassword.getText().toString();
                final String passwordConfirmString = userPasswordConfirm.getText().toString();


                if(nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || passwordConfirmString.isEmpty() || !passwordString.equals(passwordConfirmString))
                {
                    //Afficher un message d'erreur
                    ShowMessage("Please verify all fields!");
                }
                else
                {
                    CreateUserAccount(nameString,emailString,passwordString,false);
                }
            }
        });*/

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
                            //ll.setVisibility(View.INVISIBLE);

                            addUserButton.setVisibility(View.VISIBLE);
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
                }
            }
        });

    }

    private void ShowMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void DisplayUserAdd()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserManagement.this);
        alertDialog.setTitle("Add a user");
        alertDialog.setMessage("Please fill all information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.add_user_dialog,null);

        final EditText userName = layout_pwd.findViewById(R.id.user_name);

        final EditText userMail = layout_pwd.findViewById(R.id.user_mail);
        final EditText userPassword = layout_pwd.findViewById(R.id.user_password);
        final EditText userPasswordConfirm = layout_pwd.findViewById(R.id.user_password_confirm);
        alertDialog.setView(layout_pwd);

        alertDialog.setPositiveButton("Generate User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String nameString = userName.getText().toString();
                final String emailString = userMail.getText().toString();
                final String passwordString = userPassword.getText().toString();
                final String passwordConfirmString = userPasswordConfirm.getText().toString();


                if(nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty() || passwordConfirmString.isEmpty() || !passwordString.equals(passwordConfirmString))
                {
                    //Afficher un message d'erreur
                    ShowMessage("Please verify all fields!");
                }
                else
                {
                    CreateUserAccount(nameString,emailString,passwordString,false);
                    dialogInterface.dismiss();

                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
