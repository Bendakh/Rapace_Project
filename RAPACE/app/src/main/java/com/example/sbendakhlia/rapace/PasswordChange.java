package com.example.sbendakhlia.rapace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordChange extends AppCompatActivity {


    EditText passwordChangeField;
    Button changePasswordButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        mAuth = FirebaseAuth.getInstance();
        changePasswordButton = findViewById(R.id.force_change_password_button);
        passwordChangeField = findViewById(R.id.force_password_change_field);


        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordChangeField.getText() != null)
                {
                    new FirebaseDataBaseHelper().UpdatePassword(mAuth.getCurrentUser().getUid(), passwordChangeField.getText().toString(), new FirebaseDataBaseHelper.DataStatus() {
                        @Override
                        public void DataIsInserted() {
                        }
                        @Override
                        public void DataIsUpdated() {
                            mAuth.getCurrentUser().updatePassword(passwordChangeField.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ShowMessage("Password updated!");
                                }
                            });
                        }
                        @Override
                        public void DataIsDeleted() {
                        }
                    });
                }
            }
        });
    }

    private void ShowMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
