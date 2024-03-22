package com.example.roomdekho.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roomdekho.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    ArrayList<RoomInfo> list;
    //Uri imgUri;
    Context context ;
    public RoomListAdapter(Context context, ArrayList<RoomInfo> list){
        this.list = list;
        //this.imgUri = uri;
        this.context = context;
    }
    @NonNull
    @Override
    public RoomListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomListAdapter.ViewHolder holder, int position) {
        holder.cityName.setText("City : "+list.get(position).rCity);
        holder.areaName.setText("Area : "+list.get(position).rArea);
        holder.roomAddress.setText("Address : "+list.get(position).rAddress);
        holder.roomRent.setText("Rent : "+list.get(position).rRent+" Rs.");
        holder.roomDescription.setText("Room Description : "+list.get(position).rDescription);
        FirebaseStorage.getInstance().getReference().child("RoomsPic/" + list.get(position).rImageUri + "/pic1").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).placeholder(R.mipmap.ic_launcher_foreground).into(holder.roomImage);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(!(list.isEmpty())){
            return list.size();
        }
        else return 0;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        TextView areaName;
        TextView roomAddress;
        TextView roomRent;
        TextView roomDescription;
        ImageView roomImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.text_city);
            areaName = itemView.findViewById(R.id.text_area);
            roomAddress = itemView.findViewById(R.id.text_address);
            roomRent = itemView.findViewById(R.id.text_rent);
            roomDescription = itemView.findViewById(R.id.text_description);
            roomImage = itemView.findViewById(R.id.image_room);
        }
    }
}
