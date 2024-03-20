package com.example.roomdekho.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.roomdekho.R;
import com.example.roomdekho.authentication.LogInPage;
import com.example.roomdekho.databinding.ActivityOpeningPageBinding;

public class OpeningPage extends AppCompatActivity {
    ActivityOpeningPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpeningPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.opening_page_anim);
        binding.textView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(OpeningPage.this, LogInPage.class));
                finish();
            }
        },1000);
    }
}