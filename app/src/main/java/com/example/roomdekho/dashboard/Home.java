package com.example.roomdekho.dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roomdekho.R;
import com.example.roomdekho.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    Fragment fragment ;
    FragmentManager fragmentManager;
    TextView actionBarName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        actionBarName = findViewById(R.id.action_bar_name);
        actionBarName.setText("Rooms List");

        fragment = new RoomsList();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearlayout,fragment);
        fragmentTransaction.commit();
    }

    public  void onMenuClicked(View view){
        openDrawer(binding.drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void onProfileClicked(View view){
        actionBarName.setText("Profile");
        String str = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        fragment = new Profile(str);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearlayout,fragment);
        fragmentTransaction.commit();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }
    public  void onRoomsListClicked(View view){
        actionBarName.setText("RoomsList");
        fragment = new RoomsList();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearlayout,fragment);
        fragmentTransaction.commit();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void onAddRoomsClicked(View view){
        actionBarName.setText("Add Rooms");
        fragment = new AddRooms();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearlayout,fragment);
        fragmentTransaction.commit();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void onLogOutClicked(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).setTitle("RoomDekho").setIcon(R.mipmap.ic_launcher_foreground).setMessage("Are you want to logout ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionBarName.setText("Room Dekho");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, OpeningPage.class));
                Home.this.finish();
            }
        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }
}