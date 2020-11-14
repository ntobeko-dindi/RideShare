package com.xcoding.rideshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    Button btnUpdateProfile;
    ImageButton editProfilePicture;
    com.google.android.material.textfield.TextInputEditText fname,lname,email,number;
    TextView profileName;
    CircleImageView profilePic;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnUpdateProfile =  view.findViewById(R.id.btn_update_profile);
        editProfilePicture =  view.findViewById(R.id.edit_profile_picture);
        fname = view.findViewById(R.id.current_user_first_name);
        lname = view.findViewById(R.id.current_user_last_name);
        email = view.findViewById(R.id.current_user_email);
        number = view.findViewById(R.id.current_user_number);
        profileName = view.findViewById(R.id.profile_name);
        profilePic = view.findViewById(R.id.profile_image);

        customiseProfile();

        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Opening File Explorer...", Toast.LENGTH_LONG).show();
            }
        });

        btnUpdateProfile.setOnClickListener(this);

        return view;

    }
    @Override
    public void onClick(View view){

        Context context = getContext();
        CharSequence text = "Update Button Clicked!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void customiseProfile(){
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String fullName;
            if(firebaseAuth.getCurrentUser() != null){
                fullName = firebaseAuth.getCurrentUser().getDisplayName();

                char c;
                for(int x = 0; x < (fullName != null ? fullName.length() : 0); x++) {
                    c = fullName.charAt(x);
                    if (Character.isWhitespace(c)) {
                        fname.setText(fullName.substring(0,x));
                        lname.setText(fullName.substring(x+1));
                    }
                }
                email.setText(firebaseAuth.getCurrentUser().getEmail());
                number.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
                profileName.setText(firebaseAuth.getCurrentUser().getDisplayName().toUpperCase());
                Uri image = firebaseAuth.getCurrentUser().getPhotoUrl();
                Glide.with(this).load(String.valueOf(image)).into(profilePic);
            }


            //TODO set text-fields uneditable if a user loggs in using google or sign in option
        }catch (NullPointerException e){
            getUserFromExternalDB();
        }
    }

    private void getUserFromExternalDB() {
        //TODO write code that will retrieve user information from the database if the user does not login with facebook or google
        Toast.makeText(getContext(),"you logged in using an email and password option",Toast.LENGTH_LONG).show();
    }
}