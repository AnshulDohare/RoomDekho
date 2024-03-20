package com.example.roomdekho.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.roomdekho.R;
import com.example.roomdekho.databinding.ActivityVerifyEmailBinding;

public class VerifyEmail extends AppCompatActivity {
    private ActivityVerifyEmailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.loginButton.setOnClickListener(v->{
            startActivity(new Intent(this, LogInPage.class));
            finish();
        });
    }
}