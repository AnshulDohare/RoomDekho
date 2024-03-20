package com.example.roomdekho.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.roomdekho.R;
import com.example.roomdekho.dashboard.Home;
import com.example.roomdekho.databinding.ActivityLogInPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class LogInPage extends AppCompatActivity {
    private ActivityLogInPageBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        binding.createAccountTextView.setOnClickListener(v-> startActivity(new Intent(LogInPage.this,SignUpPage.class)));
        binding.loginButton.setOnClickListener(v->{
            String email = Objects.requireNonNull(binding.emailEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString().trim();
            String regexSimpleEmail = "[a-zA-Z_]+\\d*[.]?[a-zA-Z_\\d]*[.]?[a-zA-Z\\d]+@[a-zA-Z]+.[a-zA-Z]{2,3}";
            if(email.isEmpty()){
                binding.emailEditText.setError("Enter Email");
            }
            else if(!(Pattern.matches(regexSimpleEmail,email))){
                binding.emailEditText.setError("Incorrect Email Id");
            }
            else if(password.isEmpty()){
                binding.passwordEditText.setError("Enter Password");
            }
            else{
                logging(email,password);
            }
        });

        binding.forgotPasswordTextView.setOnClickListener(v->{
            String email = Objects.requireNonNull(binding.emailEditText.getText()).toString().trim();
            String regexSimpleEmail = "[a-zA-Z_]+\\d*[.]?[a-zA-Z_\\d]*[.]?[a-zA-Z\\d]+@[a-zA-Z]+.[a-zA-Z]{2,3}";
            if(email.isEmpty()){
                binding.emailEditText.setError("Enter Email");
            }
            else if(!(Pattern.matches(regexSimpleEmail,email))){
                binding.emailEditText.setError("Incorrect Email Id");
            }
            else{
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Sending Password Reset Email");
                progressDialog.setIcon(R.mipmap.ic_launcher_foreground);
                progressDialog.setCancelable(false);
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LogInPage.this);
                        builder.setCancelable(false);
                        builder.setTitle("Room Dekho");
                        builder.setIcon(R.mipmap.ic_launcher_foreground);
                        builder.setMessage("An password reset email sent on your email id.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LogInPage.this);
                        builder.setCancelable(false);
                        builder.setTitle("Room Dekho");
                        builder.setIcon(R.mipmap.ic_launcher_foreground);
                        builder.setMessage("An error occurred to send password reset email.\nCheck your email id.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                });
            }
        });


    }
    private void logging(String email,String password){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setIcon(R.mipmap.ic_launcher_foreground);
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
                    progressDialog.dismiss();
                    startActivity(new Intent(LogInPage.this, Home.class));
                    finish();
                }
                else{
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogInPage.this);
                            builder.setCancelable(false);
                            builder.setTitle("Room Dekho");
                            builder.setIcon(R.mipmap.ic_launcher_foreground);
                            builder.setMessage("Email id is not verified.\nAn verification link has been sent on your email id\nVerify your email and then try to login.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LogInPage.this);
                            builder.setCancelable(false);
                            builder.setTitle("Room Dekho");
                            builder.setIcon(R.mipmap.ic_launcher_foreground);
                            builder.setMessage("Email id is not verified.\nAn verification link has been sent on your email id\nVerify your email and then try to login.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogInPage.this, "Email id or password is incorrect", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }
}