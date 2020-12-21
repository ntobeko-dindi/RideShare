package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xcoding.rideshare.fragments.RegisterFragment;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String codeBySystem;

    String email;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        layout = findViewById(R.id.loading_layout_verify);
        progressBar = findViewById(R.id.progress_bar);
        final EditText otp = findViewById(R.id.otp_txt);
        Button verify = findViewById(R.id.phone_verification_btn);
        ImageView cancel = findViewById(R.id.cancel);
        mAuth = FirebaseAuth.getInstance();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String code = otp.getText().toString().trim();

                    if (code.isEmpty() || code.length() < 6) {
                        otp.setError("invalid OTP");
                        otp.requestFocus();
                        return;
                    } else {
                        otp.setError(null);
                        otp.clearFocus();

                        progressBar.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }
                } catch (Exception ex) {

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });

        onStartActivity();
    }

    protected void onStartActivity() {
        super.onStart();

        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(this, "no extras to display", Toast.LENGTH_LONG).show();
            } else {
                String phone = extras.getString("phoneNumber");
                email = extras.getString("email");
                pass = extras.getString("pass");
                sendOTPCode(phone);
            }
        } catch (Exception E) {

        }
    }

    void sendOTPCode(String p) {

        try {

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(p)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
            signInWithCedentials(credential);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void signInWithCedentials(PhoneAuthCredential credential) {
        try {
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNumberActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.signOut();

                        FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();

                        firebaseAuth1.signInWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {
                            String keyId = firebaseAuth1.getCurrentUser().getUid();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(keyId).child("validPhone");

                            myRef.setValue(true).addOnSuccessListener(aVoid -> {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                        }).addOnFailureListener(e -> {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                        layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }
}