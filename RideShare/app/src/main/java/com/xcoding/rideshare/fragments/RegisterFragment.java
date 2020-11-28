package com.xcoding.rideshare.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xcoding.rideshare.MainActivity;
import com.xcoding.rideshare.R;

import java.util.Calendar;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    public static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    EditText fName,lName,dob;
    Button genderFemale,genderMale,takePicture;
    DatePickerDialog.OnDateSetListener setListener;

    ImageView profilePic;
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

        takePicture.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                  getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year,month,day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                dob.setText(date);
            }
        };

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change_profile:
                askCameraPermission();
                break;
        }
    }
    void openCamera(){
        //Toast.makeText(getContext(),"opening camera",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) getContext(),new String[] {Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else{
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else{
                Toast.makeText(getContext(),"permission required",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(),String.valueOf(requestCode),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            Glide.with(this).load(String.valueOf(image)).into(profilePic);
        }
    }
}