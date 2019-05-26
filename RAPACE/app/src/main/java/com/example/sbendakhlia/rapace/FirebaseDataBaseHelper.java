package com.example.sbendakhlia.rapace;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FirebaseDataBaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUsers;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Local> locaux = new ArrayList<>();
    User user;

    public interface DataStatus {

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public interface FirebaseSuccessListener {

        void onDataFound(boolean b);

    }

    public FirebaseDataBaseHelper() {

        mDatabase = FirebaseDatabase.getInstance();
        mReferenceUsers = mDatabase.getReference("Users");

    }

    public ArrayList<Local> GetLocalList()
    {
        return this.locaux;
    }

    public ArrayList<User> GetUserList()
    {
        return this.users;
    }

    public void GetLocals(final FirebaseSuccessListener fb)
    {
        DatabaseReference locauxDB = mDatabase.getReference("locaux");
        locauxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Local tempLocal = dataSnapshot.getValue(Local.class);
                locaux.add(tempLocal);
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

    public void GetUsers(final FirebaseSuccessListener fb)
    {
        mReferenceUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                User tempUser = dataSnapshot.getValue(User.class);

                users.add(tempUser);

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

    public User GetUser(final String uid, final FirebaseSuccessListener fb) {
        final User toReturn = new User();


        mReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tempId = "";
                String tempName = "";
                String tempEmail = "";
                String tempPassword = "";
                boolean tempAdmin = false;
                int tempnDays = 0;
                String tempLastChangeDate = "";
                User.AlertModes tempAlertMode = User.AlertModes.ALARM;
                boolean tempFistConnect = false;

                if (dataSnapshot.exists()) {
                /*System.out.println(dataSnapshot.child(uid).getValue(User.class).getId());
                System.out.println(dataSnapshot.child(uid).getValue(User.class).getName());
                System.out.println(dataSnapshot.child(uid).getValue(User.class).getEmail());
                System.out.println(dataSnapshot.child(uid).getValue(User.class).isAdmin());*/
                    tempId = dataSnapshot.child(uid).getValue(User.class).getId();
                    tempName = dataSnapshot.child(uid).getValue(User.class).getName();
                    tempEmail = dataSnapshot.child(uid).getValue(User.class).getEmail();
                    tempAdmin = dataSnapshot.child(uid).getValue(User.class).isAdmin();
                    tempnDays = dataSnapshot.child(uid).getValue(User.class).getnDays();
                    tempLastChangeDate = dataSnapshot.child(uid).getValue(User.class).getLastChangedPasswordDate();
                    tempAlertMode = dataSnapshot.child(uid).getValue(User.class).getAlertMode();
                    tempFistConnect = dataSnapshot.child(uid).getValue(User.class).isFirstConnect();
                    tempPassword = dataSnapshot.child(uid).getValue(User.class).getPassword();

                    toReturn.setId(tempId);
                    toReturn.setName(tempName);
                    toReturn.setEmail(tempEmail);
                    toReturn.setAdmin(tempAdmin);
                    toReturn.setnDays(tempnDays);
                    toReturn.setLastChangedPasswordDate(tempLastChangeDate);
                    toReturn.setAlertMode(tempAlertMode);
                    toReturn.setFirstConnect(tempFistConnect);
                    toReturn.setPassword(tempPassword);
                    fb.onDataFound(true);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        /*System.out.println(toReturn.getId());
        System.out.println(toReturn.getName());
        System.out.println(toReturn.getEmail());
        System.out.println(toReturn.isAdmin());*/

        return toReturn;
    }

    public void AddUser(User user, final DataStatus dataStatus) {
        //String key = mReferenceUsers.push().getKey();
        mReferenceUsers.child(user.getId()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void UpdatePassword(final String uid, String newPassword, final DataStatus ds) {
        mReferenceUsers.child(uid).child("password").setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                String newDate = formater.format(todayDate);

                mReferenceUsers.child(uid).child("lastChangedPasswordDate").setValue(newDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ds.DataIsUpdated();
                    }
                });
            }
        });

    }

    public void UpdateNDays(final String uid, int newNDays, final DataStatus ds) {
        mReferenceUsers.child(uid).child("nDays").setValue(newNDays).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ds.DataIsUpdated();
            }
        });
    }

    public void UpdateAlertMode(final String uid, User.AlertModes newAlertMode, final DataStatus ds) {
        mReferenceUsers.child(uid).child("alertMode").setValue(newAlertMode).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ds.DataIsUpdated();
            }
        });
    }

    public void UpdateUserName(final String uid, String newName, final DataStatus ds) {
        mReferenceUsers.child(uid).child("name").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ds.DataIsUpdated();
            }
        });
    }

    public void UpdateUserMail(final String uid, String newMail, final DataStatus ds) {
        mReferenceUsers.child(uid).child("email").setValue(newMail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ds.DataIsUpdated();
            }
        });
    }

    public void UpdateFirstConnect(final String uid, boolean firstConnect, final DataStatus ds) {
        mReferenceUsers.child(uid).child("firstConnect").setValue(firstConnect).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ds.DataIsUpdated();
            }
        });
    }

    public void UpdateUser(final String uid, final String newName, String newMail, final String newPassword, final DataStatus ds) {
        mReferenceUsers.child(uid).child("email").setValue(newMail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mReferenceUsers.child(uid).child("password").setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                        Date todayDate = new Date();
                        String newDate = formater.format(todayDate);

                        mReferenceUsers.child(uid).child("lastChangedPasswordDate").setValue(newDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mReferenceUsers.child(uid).child("name").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mReferenceUsers.child(uid).child("firstConnect").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ds.DataIsUpdated();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }

        });
    }

}
