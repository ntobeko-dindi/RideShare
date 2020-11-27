package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.xcoding.rideshare.modals.VerifyNewUserDetails;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {

    Button login, sign_up, google;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    TextView textView;
    public static final int GOOGLE_SIGN_IN_CODE = 10005;

    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView);
        sign_up = findViewById(R.id.sign_up_btn);
        login = findViewById(R.id.login_button_id);
        google = findViewById(R.id.google_sign_in);
        progressBar = findViewById(R.id.progress_bar);

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
                final EditText phone;
                final EditText password;

                phone = findViewById(R.id.editTextTextPersonName);
                password = findViewById(R.id.editTextTextPassword);

                boolean fieldsValid = true;

                final String inputPhone = phone.getText().toString();
                final String inputPassword = password.getText().toString();

                VerifyNewUserDetails checkPhone = new VerifyNewUserDetails();
                checkPhone.setCell(inputPhone);

                if (!checkPhone.isValidPhone()) {
                    phone.setError("invalid phone number");
                    phone.requestFocus();
                    fieldsValid = false;
                }else {
                   phone.setError(null);
                }
                if (inputPassword.equals("")) {
                    password.setError("password required");
                    password.requestFocus();
                    fieldsValid = false;
                }

                if (fieldsValid) {
                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    Query checkUser = reference.orderByChild("cell").equalTo(inputPhone);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                String passwordFromDB = snapshot.child(inputPhone).child("pass").getValue(String.class);

                                if(inputPassword.equals(passwordFromDB)){

/*                                    String firstNameFromDB = snapshot.child(inputEmail).child("firstName").getValue(String.class);
                                    String lastNameFromDB = snapshot.child(inputEmail).child("lastName").getValue(String.class);
                                    String cellNumberFromDB = snapshot.child(inputEmail).child("cell").getValue(String.class);
                                    String genderFromDB = snapshot.child(inputEmail).child("gender").getValue(String.class);*/

                                    Intent intent = new Intent(getApplicationContext(),HomeScreenActivity.class);

/*                                    intent.putExtra("firstName",firstNameFromDB);
                                    intent.putExtra("lastNameFromDB",lastNameFromDB);
                                    intent.putExtra("email",emailFromDB);
                                    intent.putExtra("cell",cellNumberFromDB);
                                    intent.putExtra("password",passwordFromDB);
                                    intent.putExtra("gender",genderFromDB);*/

                                    progressBar.setVisibility(GONE);

                                    startActivity(intent);
                                    finish();

                                }else{
                                    progressBar.setVisibility(GONE);
                                    password.setError("Incorrect Password");
                                    password.requestFocus();
                                }
                            }else{
                                progressBar.setVisibility(GONE);
                                phone.setError("User Does Not Exist!");
                                phone.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        //implementing facebook sign up in the login page
        
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
                progressBar.setVisibility(View.VISIBLE);
                Intent sign = googleSignInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        progressBar.setVisibility(GONE);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                progressBar.setVisibility(View.VISIBLE);

                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);

                assert signInAccount != null;
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),
                        null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(GONE);
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
                progressBar.setVisibility(GONE);
                e.printStackTrace();
            }
        }
    }
}