package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xcoding.rideshare.modals.VerifyNewUserDetails;

public class SignUpActivity extends AppCompatActivity {

    Button signUp;
    TextView backToSignIn;
    EditText fname, lname, email, phoneNumber, password, cPassword;
    RadioGroup radioGroup;
    RadioButton gender;

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


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSomething();
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

    private void doSomething() {
        try {
            String firstName = fname.getText().toString().trim();
            String lastName = lname.getText().toString().trim();
            String mail = this.email.getText().toString().trim();
            String cell = phoneNumber.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String cPass = cPassword.getText().toString().trim();

            gender = findViewById(radioGroup.getCheckedRadioButtonId());
            String sex;


            VerifyNewUserDetails newUser = new VerifyNewUserDetails();

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
                    sex = this.gender.getText().toString().trim();
                    newUser.setGender(sex);

                    //TODO code to create the user profile
                    boolean success = createUserAccount();

                    if (success){
                        startActivity(new Intent(getApplicationContext(),VerifyUserAccountActivity.class));
                    }else {
                        Toast.makeText(getApplicationContext(),"Sign Up Failed",Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                cPassword.setError("Password Mismatch");
                cPassword.requestFocus();
            }

        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Please Select Your Gender", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean createUserAccount() {
        boolean accountCreated = false;

        

        return accountCreated;
    }

}