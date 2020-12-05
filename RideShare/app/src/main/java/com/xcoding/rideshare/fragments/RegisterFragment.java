package com.xcoding.rideshare.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xcoding.rideshare.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.transform.Result;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    EditText fName, lName, dob;
    Button genderFemale, genderMale, takePicture;
    DatePickerDialog.OnDateSetListener setListener;
    String currentPhotoPath;


    private StorageReference mStorageRef;
    private FirebaseAuth firebaseAuth;
    CircleImageView profilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        fName = view.findViewById(R.id.editTextFirstName);
        lName = view.findViewById(R.id.editTextLastName);
        genderFemale = view.findViewById(R.id.gender_female_btn);
        genderMale = view.findViewById(R.id.gender_male_btn);
        takePicture = view.findViewById(R.id.btn_change_profile);
        profilePic = view.findViewById(R.id.profile_pc);
        dob = view.findViewById(R.id.dob_id);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        takePicture.setOnClickListener(this);
        genderMale.setOnClickListener(this);
        genderFemale.setOnClickListener(this);

        getUserProfilePictureFromDB();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                dob.setText(date);
            }
        };

        customiseProfile();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_change_profile) {
            askCameraPermission();
        } else if (v.getId() == R.id.gender_female_btn) {
            genderFemale.setBackgroundResource(R.drawable.gender_female_selected);
            genderMale.setBackgroundResource(R.drawable.gender_male);
        } else if (v.getId() == R.id.gender_male_btn) {
            genderMale.setBackgroundResource(R.drawable.gender_male_selected);
            genderFemale.setBackgroundResource(R.drawable.gender_female);
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getContext(), "permission required", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            Bundle bundle = data.getExtras();
            Bitmap image = (Bitmap) bundle.get("data");

            Glide.with(getContext()).asBitmap().load(image).into(profilePic);

            File f = new File(currentPhotoPath);
            Uri uri = Uri.fromFile(f);
            uploadImage(uri);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                "*",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }






    void uploadImage(Uri file){

        StorageReference riversRef = mStorageRef.child("Images/"+firebaseAuth.getCurrentUser().getUid()+"/profilePic");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(),"image uploaded",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(),"image uploaded failed",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void customiseProfile() {
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String fullName;
            if (firebaseAuth.getCurrentUser() != null) {
                fullName = firebaseAuth.getCurrentUser().getDisplayName();

                char c;
                for (int x = 0; x < (fullName != null ? fullName.length() : 0); x++) {
                    c = fullName.charAt(x);
                    if (Character.isWhitespace(c)) {
                        fName.setText(fullName.substring(0, x));
                        lName.setText(fullName.substring(x + 1));
                    }
                }
                Uri image = firebaseAuth.getCurrentUser().getPhotoUrl();
                Glide.with(this).load(String.valueOf(image)).into(profilePic);
            }


            //TODO set text-fields uneditable if a user logs in using google or sign in option
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onStart() {
        super.onStart();
        if ("".equals(fName.getText().toString())) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(getContext(),"no extras to display",Toast.LENGTH_LONG).show();
            } else {

                fName.setText(extras.getString("firstName"));
                lName.setText(extras.getString("lastNameFromDB"));
                String sex = extras.getString("gender");

                if(sex.equalsIgnoreCase("male")){
                    genderMale.setBackgroundResource(R.drawable.gender_male_selected);
                    genderFemale.setBackgroundResource(R.drawable.gender_female);
                }else if(sex.equalsIgnoreCase("female")){
                    genderFemale.setBackgroundResource(R.drawable.gender_female_selected);
                    genderMale.setBackgroundResource(R.drawable.gender_male);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("".equals(fName.getText().toString())) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null) {
                Toast.makeText(getContext(),"no extras to display",Toast.LENGTH_LONG).show();
            } else {

                fName.setText(extras.getString("firstName"));
                lName.setText(extras.getString("lastNameFromDB"));
                String sex = extras.getString("gender");

                if(sex.equalsIgnoreCase("male")){
                    genderMale.setBackgroundResource(R.drawable.gender_male_selected);
                    genderFemale.setBackgroundResource(R.drawable.gender_female);
                }else if(sex.equalsIgnoreCase("female")){
                    genderFemale.setBackgroundResource(R.drawable.gender_female_selected);
                    genderMale.setBackgroundResource(R.drawable.gender_male);
                }
            }
        }
    }
}