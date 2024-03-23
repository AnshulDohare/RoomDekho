package com.example.roomdekho.dashboard;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Locale;

public class RoomsList extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;
    RoomListAdapter roomListAdapter;
    ArrayList<RoomInfo> roomsList;
    ArrayList<RoomInfo> newRoomList;
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
        searchView = view.findViewById(R.id.searchRoom);
        recyclerView = view.findViewById(R.id.roomsList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        roomsList = new ArrayList<>();
        newRoomList = new ArrayList<>();
        roomListAdapter = new RoomListAdapter(getContext(),roomsList);
        recyclerView.setAdapter(roomListAdapter);

        View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        searchPlate.setBackgroundColor(getResources().getColor(R.color.aqua));
        EditText searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(getResources().getColor(R.color.black));
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setColorFilter(getResources().getColor(R.color.black));
        ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeIcon.setColorFilter(getResources().getColor(R.color.white));

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!(query.isEmpty())){
                    newRoomList.clear();
                    String input = query.toLowerCase(Locale.ROOT);
                    for(RoomInfo roomInfo : roomsList){
                        if(roomInfo.getrCity().toLowerCase(Locale.ROOT).contains(input)||roomInfo.getrArea().toLowerCase(Locale.ROOT).contains(input)){
                            newRoomList.add(roomInfo);
                            Toast.makeText(getContext(), "Filter"+roomInfo.getrAddress(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    RoomListAdapter newRoomListAdapter = new RoomListAdapter(getContext(),newRoomList);
                    recyclerView.setAdapter(newRoomListAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    roomListAdapter = new RoomListAdapter(getContext(),roomsList);
                    recyclerView.setAdapter(roomListAdapter);
                    roomListAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        return view;
    }
}