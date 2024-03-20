package com.example.roomdekho.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.roomdekho.R;
import com.example.roomdekho.dashboard.Home;
import com.example.roomdekho.databinding.ActivitySignUpPageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity {
    private ActivitySignUpPageBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();

        binding.signUpButton.setOnClickListener(v->{
            String email = Objects.requireNonNull(binding.emailEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString().trim();
            String password2 = Objects.requireNonNull(binding.password2EditText.getText()).toString().trim();
            String regexSimpleEmail = "[a-zA-Z_]+\\d*[.]?[a-zA-Z_\\d]*[.]?[a-zA-Z\\d]+@[a-zA-Z]+.[a-zA-Z]{2,3}";
            if(email.isEmpty()){
                binding.emailEditText.setError("Enter Email");
            }
            else if(!(Pattern.matches(regexSimpleEmail,email))){
                binding.emailEditText.setError("Incorrect Email Id or Try Another Email Id");
            }
            else if(password.isEmpty()){
                binding.passwordEditText.setError("Enter Password");
            }
            else if(password.length()<8){
                binding.passwordEditText.setError("Enter 8 Digit Password");
            }
            else if(password2.isEmpty()){
                binding.password2EditText.setError("Re-enter Password");
            }
            else if(!(password.equals(password2))){
                binding.password2EditText.setError("Password Not Match");
            }
            else{
                signing(email,password);
            }

        });
    }

    private void signing(String email,String password){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher_foreground);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Signing In");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                startActivity(new Intent(SignUpPage.this, VerifyEmail.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUpPage.this, "User Already Exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}