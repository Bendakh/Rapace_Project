package com.example.sbendakhlia.rapace;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sbendakhlia.rapace.ForFireBase.Local;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttributeLocalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttributeLocalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttributeLocalsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button attributeBtn;
    Button doneBtn;
    Spinner localsSpinner;
    Spinner usersSpinner;

    private ArrayList<Local> locaux;
    private ArrayList<String> locauxNames;
    private ArrayList<User> users;
    private ArrayList<String> usersNames;
    ArrayAdapter<String> adapterLocals;
    ArrayAdapter<String> adapterUsers;

    private OnFragmentInteractionListener mListener;

    public AttributeLocalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttributeLocalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttributeLocalsFragment newInstance(String param1, String param2) {
        AttributeLocalsFragment fragment = new AttributeLocalsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attribute_locals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attributeBtn = view.findViewById(R.id.attribuer_locaux_button);
        doneBtn = view.findViewById(R.id.return_to_user_management_button);
        localsSpinner = view.findViewById(R.id.locaux_spinner);
        usersSpinner = view.findViewById(R.id.users_spinner);

        Log.e("PLEASEWORK","FOUND0");


        //Locaux spinner
        locauxNames = new ArrayList<>();
        adapterLocals = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locauxNames);
        adapterLocals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localsSpinner.setAdapter(adapterLocals);

        DatabaseReference locauxDB = FirebaseDatabase.getInstance().getReference("locaux");
        locauxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Local tmpLocal = dataSnapshot.getValue(Local.class);

                    //locaux.add(tmpLocal);
                    locauxNames.add(tmpLocal.GetNom());
                    adapterLocals.notifyDataSetChanged();
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




        //Users spinner
        usersNames = new ArrayList<>();
        adapterUsers = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, usersNames);
        adapterUsers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersSpinner.setAdapter(adapterUsers);

        DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("Users");
        usersDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User tmpUser = dataSnapshot.getValue(User.class);

                //locaux.add(tmpLocal);
                usersNames.add(tmpUser.getEmail());
                adapterUsers.notifyDataSetChanged();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
