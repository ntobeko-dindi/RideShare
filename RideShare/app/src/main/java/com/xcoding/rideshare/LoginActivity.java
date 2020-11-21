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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.xcoding.rideshare.modals.ForgotPassword;

public class LoginActivity extends AppCompatActivity {

    Button login, sign_up, google;
    private LoginButton facebook;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    TextView textView;
    private CallbackManager callbackManager;
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    ProgressDialog loadingBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView);
        sign_up = findViewById(R.id.sign_up_btn);
        login = findViewById(R.id.login_button_id);
        google = findViewById(R.id.google_sign_in);
        facebook = findViewById(R.id.facebook_sign_in);
        facebook.setReadPermissions("email","public_profile");
        loadingBar = new ProgressDialog(LoginActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
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

                if (inputEmail.equals("") || !inputEmail.contains("@")) {
                    email.setError("invalid email");
                    email.requestFocus();
                    fieldsValid = false;
                }
                if (inputPassword.equals("")) {
                    password.setError("password required");
                    password.requestFocus();
                    fieldsValid = false;
                }

                if (fieldsValid) {
                    startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class));
                    finish();
                }
            }
        });

        //implementing facebook sign up in the login page
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getApplicationContext(), "User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //info.setText("Login attempt canceled.");
                Toast.makeText(getApplicationContext(), "Operation Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                // info.setText("Login attempt failed.");
                Toast.makeText(getApplicationContext(), "Operation Failed,Please try Again", Toast.LENGTH_LONG).show();
            }
        });

        //implementing google sing in option on a login page
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("298659425253-70hna5d8fp3mjsp2vog2u80hlfpsvkuo.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null || firebaseAuth.getCurrentUser() != null) {
            //Toast.makeText(this,"user is already logged in",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, HomeScreenActivity.class));
            finish();
        }
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.setTitle("google sign in");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Intent sign = googleSignInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });
    }

    private void handleFacebookToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(),"Logged in successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Authentication failed, Please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        loadingBar.cancel();
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                loadingBar.setTitle("successfully signed in");
                loadingBar.setMessage("setting up few things...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);

                assert signInAccount != null;
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),
                        null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message = "your google account is connected to our application.";
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
        }
    }
}