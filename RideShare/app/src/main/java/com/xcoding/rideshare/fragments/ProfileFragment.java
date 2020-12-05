package com.xcoding.rideshare.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xcoding.rideshare.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    Button btnUpdateProfile;
    ImageButton editProfilePicture;
    com.google.android.material.textfield.TextInputEditText fname, lname, email, number;
    TextView profileName, gender;
    CircleImageView profilePic;

    ProgressBar progressBar;
    RelativeLayout layout;
    TextView uploadProgress;

    private StorageReference mStorageRef;

    private static final int SELECT_PHOTO = 120;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        editProfilePicture = view.findViewById(R.id.edit_profile_picture);
        fname = view.findViewById(R.id.current_user_first_name);
        lname = view.findViewById(R.id.current_user_last_name);
        email = view.findViewById(R.id.current_user_email);
        number = view.findViewById(R.id.current_user_number);
        profileName = view.findViewById(R.id.profile_name);
        profilePic = view.findViewById(R.id.profile_image);
        layout = view.findViewById(R.id.relativeLayout);
        progressBar = view.findViewById(R.id.progress_bar);
        gender = view.findViewById(R.id.gender);
        uploadProgress = view.findViewById(R.id.upload_progress);
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        customiseProfile();

        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

        btnUpdateProfile.setOnClickListener(this);

        return view;

    }

    void uploadImage(Uri file) {

        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        uploadProgress.setVisibility(View.VISIBLE);
        StorageReference riversRef = mStorageRef.child("Images/" + firebaseAuth.getCurrentUser().getUid() + "/profilePic");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_LONG).show();

                        layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        uploadProgress.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        uploadProgress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "image uploaded failed", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        String p = ((int) progressPercentage) + "%";
                        uploadProgress.setText(p);
                    }
                });
    }

    @Override
    public void onClick(View view) {

        Context context = getContext();
        CharSequence text = "Update Button Clicked!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void customiseProfile() {
        try {
            String fullName;
            if (firebaseAuth.getCurrentUser() != null) {
                fullName = firebaseAuth.getCurrentUser().getDisplayName();

                char c;
                for (int x = 0; x < (fullName != null ? fullName.length() : 0); x++) {
                    c = fullName.charAt(x);
                    if (Character.isWhitespace(c)) {
                        fname.setText(fullName.substring(0, x));
                        lname.setText(fullName.substring(x + 1));
                    }
                }
                email.setText(firebaseAuth.getCurrentUser().getEmail());
                number.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
                profileName.setText(firebaseAuth.getCurrentUser().getDisplayName().toUpperCase());
                Uri image = firebaseAuth.getCurrentUser().getPhotoUrl();
                Glide.with(this).load(String.valueOf(image)).into(profilePic);
            }


            //TODO set text-fields uneditable if a user logs in using google or sign in option
        } catch (NullPointerException e) {
            //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectImage = data.getData();
                InputStream inputStream = null;

                try {
                    assert selectImage != null;
                    inputStream = getContext().getContentResolver().openInputStream(selectImage);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                BitmapFactory.decodeStream(inputStream);
                Glide.with(this).load(String.valueOf(selectImage)).into(profilePic);
                uploadImage(selectImage);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if ("UNKNOWN".equals(profileName.getText().toString()) || profileName.getText().toString().equals(null) || profileName.getText().toString().equals("null") || profileName.getText().toString().equals("")) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(getContext(),"server error occurred",Toast.LENGTH_LONG).show();
            } else {

                String fullNames = extras.getString("lastNameFromDB") + " " + extras.getString("firstName");
                profileName.setText(fullNames);
                fname.setText(extras.getString("firstName"));
                lname.setText(extras.getString("lastNameFromDB"));
                gender.setText(extras.getString("gender"));
                number.setText(extras.getString("cell"));
                email.setText(extras.getString("email"));

                getUserProfilePictureFromDB();
            }
        }
    }
    private void getUserProfilePictureFromDB() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        StorageReference riversRef = mStorageRef.child("Images/" + userID + "/profilePic");
        try {
            final File localFile = File.createTempFile("profilePic","*");
            riversRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(getActivity() != null){
                        Glide.with(getContext()).load(String.valueOf(localFile.toURI())).into(profilePic);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(getContext(),"IOException",Toast.LENGTH_LONG).show();
        }
    }
}