package com.example.roomdekho.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.roomdekho.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address1 = address.getText().toString().trim();
                String rent1 = rent.getText().toString().trim();
                String description1 = roomDiscription.getText().toString().trim();

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.mipmap.ic_launcher_foreground);
                    builder.setTitle("Preview");
                    String alertMessage = "*City : "+selectedCity+"\n"+
                            "*Area : "+selectedArea +"\n"+
                            "*Address : "+address1 +"\n"+
                            "*Rent : "+rent1 + "\n"+
                            "*Room Description "+description1;
                    builder.setMessage(alertMessage);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddingRoomInfo(address1,rent1,description1);
                        }
                    }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();

                }





            }
        });

        return view;
    }

    private void AddingRoomInfo(String address1, String rent1, String description1) {

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imgUri);
                roomImg.setImageURI(imgUri);
                roomImg.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}