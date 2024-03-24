package com.example.roomdekho.dashboard;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.roomdekho.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import kotlin.Result;

public class Profile extends Fragment {

    TextInputEditText emailId;
    ImageView profileImage;
    Spinner profession;
    String professionList[] = {"Student","Private Employee","Govt Employee","Self Employed","Room Dealer","Traveller"};
    ArrayAdapter<String>adapter;
    int IMG_REQ_CODE = 1211;
    public Profile() {
        // Required empty public constructor
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
        emailId = view.findViewById(R.id.profileEmail);
        profileImage = view.findViewById(R.id.profileImage);
        profession = view.findViewById(R.id.profileProfession);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,professionList);
        profession.setAdapter(adapter);
        emailId.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),IMG_REQ_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQ_CODE && resultCode== RESULT_OK && data !=null){
            Uri imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imgUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}