package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    Button login,sign_up,google,facebook;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    TextView textView;
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    ProgressDialog loadingBar;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView);
         sign_up =  findViewById(R.id.sign_up_btn);
         login =  findViewById(R.id.login_button_id);
         google = findViewById(R.id.google_sign_in);
         facebook = findViewById(R.id.facebook_sign_in);
         loadingBar = new ProgressDialog(LoginActivity.this);
         firebaseAuth = FirebaseAuth.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
         //implementing sign up button
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //implementing login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email, password;

                email = findViewById(R.id.editTextTextPersonName);
                password = findViewById(R.id.editTextTextPassword);

                boolean fieldsValid = true;

                String inputEmail = email.getText().toString();
                String inputPassword = password.getText().toString();

                if(inputEmail.equals("") || !inputEmail.contains("@")){
                    email.setError("invalid email");
                    email.requestFocus();
                    fieldsValid = false;
                }
                if (inputPassword.equals("")){
                    password.setError("password required");
                    password.requestFocus();
                    fieldsValid = false;
                }

                if(fieldsValid){
                    startActivity(new Intent(LoginActivity.this,HomeScreenActivity.class));
                    finish();
                }
            }
        });

        //implementing facebook sign up in the login page
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

         //implementing google sing in option on a login page
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("298659425253-70hna5d8fp3mjsp2vog2u80hlfpsvkuo.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null || firebaseAuth.getCurrentUser() != null){
            //Toast.makeText(this,"user is already logged in",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,HomeScreenActivity.class));
            finish();
        }
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,HomeScreenActivity.class));
                finish();
//                loadingBar.setTitle("google sign in");
//                loadingBar.setMessage("Please wait...");
//                loadingBar.setCanceledOnTouchOutside(false);
//                loadingBar.show();
//
//                Intent sign = googleSignInClient.getSignInIntent();
//                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        loadingBar.cancel();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);

                assert signInAccount != null;
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),
                        null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(this,"your google account is connected to our application.",Toast.LENGTH_LONG);
                                startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (ApiException e){
                e.printStackTrace();
            }
        }
    }
}












/*
*         signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(LoginActivity.this ,SignUpActivity.class);
                startActivity(login);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });*/