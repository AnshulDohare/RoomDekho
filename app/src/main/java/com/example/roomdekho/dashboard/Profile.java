package com.example.roomdekho.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.roomdekho.R;

public class Profile extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static TextView emailId;
    String email;
    public Profile(String email) {
        // Required empty public constructor
        this.email = email;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        emailId = view.findViewById(R.id.txtEmailId);
        return view;
    }
    public void setData(String email){
        emailId.setText(email);
    }

    @Override
    public void onStart() {
        super.onStart();
        setData(this.email);
    }
}