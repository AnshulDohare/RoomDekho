package com.example.roomdekho.dashboard;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomdekho.R;
import com.example.roomdekho.adapter.RoomInfo;
import com.example.roomdekho.adapter.RoomListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class RoomsList extends Fragment {
    RecyclerView recyclerView;
    RoomListAdapter roomListAdapter;
    ArrayList<RoomInfo> roomsList;
    public RoomsList() {
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
        View view =  inflater.inflate(R.layout.fragment_rooms_list, container, false);
        recyclerView = view.findViewById(R.id.roomsList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        roomsList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(getContext(),roomsList);
        recyclerView.setAdapter(roomListAdapter);
        FirebaseFirestore.getInstance().collection("AllRooms").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                roomsList.clear();
                for(DocumentSnapshot documentSnapshot:value){
                    roomsList.add(new RoomInfo(documentSnapshot.getString("rCity"),documentSnapshot.getString("rArea"),documentSnapshot.getString("rAddress"),
                            documentSnapshot.getString("rRent"),documentSnapshot.getString("rDescription"), "RoomsPic/"+documentSnapshot.getString("rUserId")+"/"+documentSnapshot.getString("rRoomNumber")));
                }
                roomListAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
}