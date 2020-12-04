package com.xcoding.rideshare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xcoding.rideshare.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadFragment extends Fragment implements View.OnClickListener {

    private static final int SELECT_PHOTO = 100;
    private int selectedBtn = 0;

    ProgressBar progressBar;
    RelativeLayout layout;
    TextView uploadProgress;
    private StorageReference mStorageRef;
    FirebaseAuth firebaseAuth;

    private String imageName;

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

        uploadProgress = view.findViewById(R.id.upload_progress);
        layout = view.findViewById(R.id.relativeLayout);
        progressBar = view.findViewById(R.id.progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();


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
                imageName = "license";
                break;
            case R.id.prdpBtn:
                selectedBtn = 2;
                imageName = "pdp";
                break;
            case R.id.carBtn:
                selectedBtn = 3;
                imageName = "car";
                break;
            case R.id.carRegCertificateBtn:
                selectedBtn = 4;
                imageName = "carRegCertificate";
                break;
            case R.id.millegeBtn:
                selectedBtn = 5;
                imageName = "millage";
                break;
        }
        openLocalStorage();
    }

    void uploadImage(Uri file){

        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        uploadProgress.setVisibility(View.VISIBLE);


        StorageReference riversRef = mStorageRef.child("Images/"+firebaseAuth.getCurrentUser().getEmail()+"/"+imageName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(),"image uploaded",Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getContext(),"image uploaded failed",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        String p = ((int)progressPercentage) + "%";
                        uploadProgress.setText(p);
                    }

                });
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

                uploadImage(selectImage);
            }
        }
    }
}