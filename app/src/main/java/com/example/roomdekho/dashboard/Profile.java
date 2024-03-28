package com.example.roomdekho.dashboard;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.roomdekho.R;
import com.example.roomdekho.my_classes.ProfileInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {
    ScrollView layout_profile;
    TextInputEditText emailId,profileMobile,profileAddress,profileName;
    Button profileSave;
    ImageView profileImage;
    Spinner profession;
    String[] professionList = {"Student","Private Employee","Govt Employee","Self Employed","Room Dealer","Traveller"};
    ArrayAdapter<String>adapter;
    int IMG_REQ_CODE = 1211;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri imgUri;
    String userEmail;
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
        layout_profile = view.findViewById(R.id.profile_scroll_layout);
        emailId = view.findViewById(R.id.profileEmail);
        profileImage = view.findViewById(R.id.profileImage);
        profession = view.findViewById(R.id.profileProfession);
        profileMobile = view.findViewById(R.id.profileMobile);
        profileName = view.findViewById(R.id.profileName);
        profileAddress = view.findViewById(R.id.profileAddress);
        profileSave = view.findViewById(R.id.profileSave);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,professionList);
        profession.setAdapter(adapter);
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        emailId.setText(userEmail);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.mipmap.ic_launcher_foreground);
        progressDialog.show();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),IMG_REQ_CODE);
            }
        });


        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                progressDialog.dismiss();
                layout_profile.setVisibility(View.VISIBLE);
            }
        };
        thread.start();



        storageReference.child("RoomsPic/" + FirebaseAuth.getInstance().getUid() + "/profilePic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).placeholder(R.mipmap.ic_launcher_foreground).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        databaseReference.child("RoomDekhoProfiles/"+FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProfileInfo profileInfo = snapshot.getValue(ProfileInfo.class);
                    profileMobile.setText(profileInfo.getpMobile());
                    profileAddress.setText(profileInfo.getpAddress());
                    profileName.setText(profileInfo.getpName());
                    for(int i = 0;i<professionList.length;i++){
                        if(professionList[i].equals(profileInfo.getpProfession())){
                            profession.setSelection(i);
                            break;
                        }
                    }
                    progressDialog.dismiss();
                    layout_profile.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
        profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sMobile = profileMobile.getText().toString();
                String sAddress = profileAddress.getText().toString();
                String sProfession  = profession.getSelectedItem().toString();
                String sName = profileName.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Email : "+userEmail+"\nMobile : "+sMobile+"\nAddress : "+sAddress+"\nsProfession : "+sProfession);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveData(sMobile,sAddress,sProfession,sName);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

            }
        });

        return view;
    }

    private void saveData(String sMobile, String sAddress, String sProfession,String sName) {
        Map<String,String> map = new HashMap<>();
        map.put("pEmail",userEmail);
        map.put("pMobile",sMobile);
        map.put("pAddress",sAddress);
        map.put("pProfession",sProfession);
        map.put("pName",sName);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.mipmap.ic_launcher_foreground);
        progressDialog.show();

        databaseReference.child("RoomDekhoProfiles/"+FirebaseAuth.getInstance().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show();            }
        });

        if(imgUri!=null){
            progressDialog.show();
            storageReference.child("RoomsPic/" + FirebaseAuth.getInstance().getUid() + "/profilePic").putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQ_CODE && resultCode== RESULT_OK && data !=null){
            imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imgUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}