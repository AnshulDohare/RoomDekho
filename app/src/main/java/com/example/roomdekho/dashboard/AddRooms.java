package com.example.roomdekho.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.roomdekho.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class AddRooms extends Fragment {
    final String[] cities = {"Bhopal","Indore","Balaghat"};
    final String[] areasBhopal = {"Ashoka Garden","LalGhati","MP Nagar"};
    final String[] areasIndore = {"Holker Stadium","IIT"};
    final String[] areasBalaghat = {"Railway Station","College"};
    Spinner selectCity,selectArea;
    TextInputEditText address,rent,roomDiscription;
    Button selectImg,submit;
    ImageView roomImg;
    ArrayAdapter<String> cityAdapter,areaAdapter;
    String selectedCity;
    String selectedArea;
    int IMG_REQ_CODE = 121;
    Uri imgUri;
    public AddRooms() {
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
        View view =  inflater.inflate(R.layout.fragment_add_rooms, container, false);
        selectCity = view.findViewById(R.id.addCity);
        selectArea = view.findViewById(R.id.addArea);
        address = view.findViewById(R.id.addAddress);
        rent = view.findViewById(R.id.addRent);
        roomDiscription = view.findViewById(R.id.addRoomDiscription);
        selectImg = view.findViewById(R.id.addRoomPic);
        submit = view.findViewById(R.id.addRoomSubmit);
        roomImg = view.findViewById(R.id.imgRoomPic);

        roomImg.setVisibility(View.GONE);

        cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,cities);
        selectCity.setAdapter(cityAdapter);
        areaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,areasBhopal);
        selectArea.setAdapter(areaAdapter);
        selectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    areaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,areasBhopal);
                    selectedCity = cityAdapter.getItem(position);
                    selectArea.setAdapter(areaAdapter);
                }
                else if(position==1){
                    areaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,areasIndore);
                    selectedCity = cityAdapter.getItem(position);
                    selectArea.setAdapter(areaAdapter);
                }
                else if(position==2){
                    areaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,areasBalaghat);
                    selectedCity = cityAdapter.getItem(position);
                    selectArea.setAdapter(areaAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedArea = areaAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectImg.setOnClickListener(v->imageSelecting());

        submit.setOnClickListener(v -> {

            String address1 = Objects.requireNonNull(address.getText()).toString().trim();
            String rent1 = Objects.requireNonNull(rent.getText()).toString().trim();
            String description1 = Objects.requireNonNull(roomDiscription.getText()).toString().trim();

            if(address1.isEmpty()){
                address.setError("Enter Address");
            }
            else if(rent1.isEmpty()){
                rent.setError("Enter Rent");
            }
            else if(description1.isEmpty()){
                roomDiscription.setError("Enter Room Description");
            }
            else if(imgUri==null){
                selectImg.setError("Upload Room Image");
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setIcon(R.mipmap.ic_launcher_foreground);
                builder.setTitle("Preview");
                String alertMessage = "*City : "+selectedCity+"\n"+
                        "*Area : "+selectedArea +"\n"+
                        "*Address : "+address1 +"\n"+
                        "*Rent : "+rent1 + "\n"+
                        "*Room Description :"+description1;
                builder.setMessage(alertMessage);
                builder.setCancelable(false);
                builder.setPositiveButton("Add", (dialog, which) -> AddingRoomInfo(address1,rent1,description1)).setNegativeButton("Cancle", (dialog, which) -> {

                });
                builder.show();

            }





        });

        return view;
    }

    private void AddingRoomInfo(String address1, String rent1, String description1){
        HashMap<String,String> map = new HashMap<>();
        map.put("rCity",selectedCity);
        map.put("rArea",selectedArea);
        map.put("rAddress",address1);
        map.put("rRent",rent1);
        map.put("rDescription",description1);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        firestore.collection(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).document("Room1").set(map).addOnSuccessListener(unused -> firebaseStorage.getReference().child("RoomsPic/"+FirebaseAuth.getInstance().getUid()+"/pic1").putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Successfully Room Added", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void imageSelecting() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Image"),IMG_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQ_CODE && resultCode==-1 && data!=null){
            imgUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),imgUri);
                roomImg.setImageBitmap(bitmap);
                roomImg.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}