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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.xcoding.rideshare.modals.VerifyNewUserDetails;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class SignUpActivity extends AppCompatActivity {

    Button signUp;
    TextView backToSignIn;
    EditText fname, lname, email, phoneNumber, password, cPassword;
    RadioGroup radioGroup;
    RadioButton gender;
    CountryCodePicker countryCodePicker;

    ProgressBar progressBar;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String  USER = "users";
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
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(phoneNumber);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(view -> prepareNewUser());

        backToSignIn.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void prepareNewUser() {
        try {
            String firstName = fname.getText().toString().trim();
            String lastName = lname.getText().toString().trim();
            String mail = this.email.getText().toString().trim();
            String cell = countryCodePicker.getFullNumberWithPlus();
            String pass = password.getText().toString().trim();
            String cPass = cPassword.getText().toString().trim();

            gender = findViewById(radioGroup.getCheckedRadioButtonId());
            String sex;


            newUser = new VerifyNewUserDetails();

            if (pass.equals(cPass)) {

                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setEmail(mail);
                newUser.setCell(phoneNumber.getText().toString());
                newUser.setPass(pass);
                newUser.setDriver(false);

                String userCredentials = newUser.validate();

                switch (userCredentials) {
                    case "firstNameError":
                        fname.setError("Invalid First Name");
                        fname.requestFocus();
                        break;
                    case "lastNameError":
                        lname.setError("Invalid Last Name");
                        lname.requestFocus();
                        break;
                    case "emailError":
                        email.setError("Invalid Email");
                        email.requestFocus();
                        break;
                    case "phoneNumberError":
                        phoneNumber.setError("Invalid Phone Number");
                        phoneNumber.requestFocus();
                        break;
                    case "passwordError":
                        password.setError("Password Must Have At least 1 special Character, 1 Uppercase letter, 1 Digit and no less that 8 Characters");
                        password.requestFocus();
                        break;
                    case "ok":
                        progressBar.setVisibility(View.VISIBLE);
                        sex = this.gender.getText().toString().trim();
                        newUser.setGender(sex);
                        newUser.setCell(cell);

                        createUserAccount(mail, pass);
                        break;
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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        final FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        user.sendEmailVerification().addOnSuccessListener(aVoid -> {
                            Toast.makeText(getApplicationContext(),"Verification Email Sent", LENGTH_LONG).show();

                            String keyId = user.getUid();
                            mDatabase.child(keyId).setValue(newUser).addOnSuccessListener(aVoid1 -> logout());
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(getApplicationContext(), VerifyPhoneNumberActivity.class);
                            intent.putExtra("phoneNumber",countryCodePicker.getFullNumberWithPlus());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Invalid Email..",Toast.LENGTH_SHORT).show());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        boolean connected = false;
                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            connected = true;
                        }
                        if (!connected) {
                            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Email Already exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    // ...

                });
    }


    private void logout() {

        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).build()).signOut().addOnSuccessListener(aVoid -> {
        });
    }

}