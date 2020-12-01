package com.xcoding.rideshare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.xcoding.rideshare.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadFragment extends Fragment implements View.OnClickListener {

    private static final int SELECT_PHOTO = 100;
    private int selectedBtn = 0;

    CircleImageView driverLicence,prdp,carPhoto,carRegCert,millagePic;
    Button driverLicenceBTN,prdpBTN,carPhotoBTN,carRegCertBTN,millagePicBTN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        driverLicence = view.findViewById(R.id.licenseImg);
        prdp = view.findViewById(R.id.prdpImg);
        carPhoto = view.findViewById(R.id.carImg);
        carRegCert = view.findViewById(R.id.carRegCertificateImg);
        millagePic = view.findViewById(R.id.millegeImg);


        driverLicenceBTN = view.findViewById(R.id.licenseBtn);
        prdpBTN = view.findViewById(R.id.prdpBtn);
        carPhotoBTN = view.findViewById(R.id.carBtn);
        carRegCertBTN = view.findViewById(R.id.carRegCertificateBtn);
        millagePicBTN = view.findViewById(R.id.millegeBtn);

        driverLicenceBTN.setOnClickListener(this);
        prdpBTN.setOnClickListener(this);
        carPhotoBTN.setOnClickListener(this);
        carRegCertBTN.setOnClickListener(this);
        millagePicBTN.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.licenseBtn:
                selectedBtn = 1;
                break;
            case R.id.prdpBtn:
                selectedBtn = 2;
                break;
            case R.id.carBtn:
                selectedBtn = 3;
                break;
            case R.id.carRegCertificateBtn:
                selectedBtn = 4;
                break;
            case R.id.millegeBtn:
                selectedBtn = 5;
                break;
        }
        openLocalStorage();
    }

    void openLocalStorage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO){
            if(resultCode == Activity.RESULT_OK){
                Uri selectImage = data.getData();
                InputStream inputStream = null;

                try{
                    assert selectImage != null;
                    inputStream = getContext().getContentResolver().openInputStream(selectImage);
                }
                catch (FileNotFoundException ex){
                    ex.printStackTrace();
                }
                BitmapFactory.decodeStream(inputStream);

                switch (selectedBtn){
                    case 1:
                        Glide.with(this).load(String.valueOf(selectImage)).into(driverLicence);
                        break;
                    case 2:
                        Glide.with(this).load(String.valueOf(selectImage)).into(prdp);
                        break;
                    case 3:
                        Glide.with(this).load(String.valueOf(selectImage)).into(carPhoto);
                        break;
                    case 4:
                        Glide.with(this).load(String.valueOf(selectImage)).into(carRegCert);
                        break;
                    case 5:
                        Glide.with(this).load(String.valueOf(selectImage)).into(millagePic);
                        break;
                }

            }
        }
    }
}