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

public class SignUpActivity extends AppCompatActivity {

    Button signUp;
    TextView backToSignIn;
    EditText fname,lname,email,password,cPassword;
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
        password = findViewById(R.id.signup_password_id);
        cPassword = findViewById(R.id.signup_password_confirm_id);
        radioGroup = findViewById(R.id.radioGroup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(SignUpActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();*/

                doSomething();
            }
        });

        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void doSomething() {
        gender = findViewById(radioGroup.getCheckedRadioButtonId());
        Toast.makeText(this,gender.getText(),Toast.LENGTH_LONG).show();
    }

}