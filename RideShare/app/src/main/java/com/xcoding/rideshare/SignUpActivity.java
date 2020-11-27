package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xcoding.rideshare.modals.VerifyNewUserDetails;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "CREATE";
    Button signUp;
    TextView backToSignIn;
    EditText fname, lname, email, phoneNumber, password, cPassword;
    RadioGroup radioGroup;
    RadioButton gender;

    ProgressBar progressBar;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String USER = "users";
    VerifyNewUserDetails newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        signUp = findViewById(R.id.sign_up_btn);
        backToSignIn = findViewById(R.id.back_to_sign_in);

        fname = findViewById(R.id.signup_firstname_id);
        lname = findViewById(R.id.signup_lastname_id);
        email = findViewById(R.id.signup_email_id);
        phoneNumber = findViewById(R.id.signup_phonenumber_id);
        password = findViewById(R.id.signup_password_id);
        cPassword = findViewById(R.id.signup_password_confirm_id);
        radioGroup = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.progress_bar);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareNewUser();
            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void prepareNewUser() {
        try {
            String firstName = fname.getText().toString().trim();
            String lastName = lname.getText().toString().trim();
            String mail = this.email.getText().toString().trim();
            String cell = phoneNumber.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String cPass = cPassword.getText().toString().trim();

            gender = findViewById(radioGroup.getCheckedRadioButtonId());
            String sex;


            newUser = new VerifyNewUserDetails();

            if (pass.equals(cPass)) {

                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setEmail(mail);
                newUser.setCell(cell);
                newUser.setPass(pass);

                String userCredentials = newUser.validate();

                if (userCredentials.equals("firstNameError")) {
                    fname.setError("Invalid First Name");
                    fname.requestFocus();
                } else if (userCredentials.equals("lastNameError")) {
                    lname.setError("Invalid Last Name");
                    lname.requestFocus();
                } else if (userCredentials.equals("emailError")) {
                    email.setError("Invalid Email");
                    email.requestFocus();
                } else if (userCredentials.equals("phoneNumberError")) {
                    phoneNumber.setError("Invalid Phone Number");
                    phoneNumber.requestFocus();
                } else if (userCredentials.equals("passwordError")) {
                    password.setError("Password Must Have At least 1 special Character, 1 Uppercase letter, 1 Digit and no less that 8 Characters");
                    password.requestFocus();
                } else if (userCredentials.equals("ok")) {
                    progressBar.setVisibility(View.VISIBLE);
                    sex = this.gender.getText().toString().trim();
                    newUser.setGender(sex);
                    newUser.setCell(cell);

                    createUserAccount(mail,pass);
                }
            } else {
                cPassword.setError("Password Mismatch");
                cPassword.requestFocus();
            }

        } catch (NullPointerException e) {
            makeText(getApplicationContext(), "Please Select Your Gender", LENGTH_LONG).show();
        } catch (Exception ex) {
            makeText(getApplicationContext(), ex.getMessage(), LENGTH_LONG).show();
        }
    }

    private void createUserAccount(String email,String password) {
        //TODO code to create the user profile

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            String keyId = user.getUid();
                            mDatabase.child(keyId).setValue(newUser);
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            makeText(getApplicationContext(), "Authentication Failed", LENGTH_LONG).show();
                        }

                        // ...

                    }
                });
    }

}