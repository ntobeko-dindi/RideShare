package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.xcoding.rideshare.modals.VerifyNewUserDetails;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button send;
    ImageView back;
    ProgressBar progressBar;
    RelativeLayout layout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email_forgot_txt);
        send = findViewById(R.id.email_forgot_password_btn);
        back = findViewById(R.id.back_to_sign_in);
        layout = findViewById(R.id.forgot_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                String inputEmail = email.getText().toString();
                VerifyNewUserDetails userDetails = new VerifyNewUserDetails();
                userDetails.setEmail(inputEmail);
                if(userDetails.emailOkay()){
                    firebaseAuth.sendPasswordResetEmail(inputEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"password reset email sent",Toast.LENGTH_SHORT).show();
                            layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            layout.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"entered email does not exist!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"please enter a valid email \n or check internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}