package com.example.sbendakhlia.rapace;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends Activity {



    EditText passwordChangeField;
    EditText nDaysChangeField;
    Button changePasswordButton;
    Button changeNDaysButton;
    FirebaseAuth mAuth;
    Spinner alertModesSpinner;
    Button changeAlertButton;

    EditText changeEmailField;
    Button changeEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        changePasswordButton = findViewById(R.id.change_password_button);
        passwordChangeField = findViewById(R.id.password_change_field);
        nDaysChangeField = findViewById(R.id.change_ndays_field);
        changeNDaysButton = findViewById(R.id.change_ndays_button);
        alertModesSpinner = findViewById(R.id.alert_modes);
        changeAlertButton = findViewById(R.id.change_alert_modes_button);

        changeEmailField = findViewById(R.id.email_change_field);
        changeEmailButton = findViewById(R.id.change_email_button);

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!changeEmailField.getText().toString().isEmpty())
                {
                    new FirebaseDataBaseHelper().UpdateUserMail(mAuth.getCurrentUser().getUid(), changeEmailField.getText().toString(), new FirebaseDataBaseHelper.DataStatus() {
                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            mAuth.getCurrentUser().updateEmail(changeEmailField.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ShowMessage("Email updated!");
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

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!passwordChangeField.getText().toString().isEmpty()) {
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
        changeNDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nDaysChangeField.getText().toString().isEmpty()) {
                    int newVal = Integer.parseInt(nDaysChangeField.getText().toString());
                    if (newVal > 0 && newVal <= 30) {
                        new FirebaseDataBaseHelper().UpdateNDays(mAuth.getCurrentUser().getUid(), newVal, new FirebaseDataBaseHelper.DataStatus() {
                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {

                                ShowMessage("Field updated");
                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });
                    }
                }
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alertModes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alertModesSpinner.setAdapter(adapter);
        int index = 0;


        changeAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User.AlertModes newAlertMode = User.AlertModes.ALARM;
                User.AlertModes newAlertMode = null;

                switch (alertModesSpinner.getSelectedItem().toString()) {
                    case "Alarm":
                        newAlertMode = User.AlertModes.ALARM;
                        break;
                    case "Vibration":
                        newAlertMode = User.AlertModes.VIBRATION;
                        break;
                    case "Flash":
                        newAlertMode = User.AlertModes.FLASH;
                        break;
                    case "Visualisation":
                        newAlertMode = User.AlertModes.VISUALISATION;
                        break;

                    default:
                        break;
                }
                if(newAlertMode != null) {
                    new FirebaseDataBaseHelper().UpdateAlertMode(mAuth.getCurrentUser().getUid(), newAlertMode, new FirebaseDataBaseHelper.DataStatus() {
                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            ShowMessage("Field updated");
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
