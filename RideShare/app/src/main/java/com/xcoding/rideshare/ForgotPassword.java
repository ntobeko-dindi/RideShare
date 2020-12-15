package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        send.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            String inputEmail = email.getText().toString();
            VerifyNewUserDetails userDetails = new VerifyNewUserDetails();
            userDetails.setEmail(inputEmail);
            if (userDetails.emailOkay()) {


                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                }
                if (connected) {
                    firebaseAuth.sendPasswordResetEmail(inputEmail).addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "password reset email sent", Toast.LENGTH_SHORT).show();
                        layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                        layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "entered email does not exist!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();

                }
            } else {
                layout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}