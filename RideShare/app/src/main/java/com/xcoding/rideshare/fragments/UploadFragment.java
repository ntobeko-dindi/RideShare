package com.xcoding.rideshare.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.xcoding.rideshare.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

    CircleImageView driverLicence, prdp, carPhoto, carRegCert, millagePic;
    Button driverLicenceBTN, prdpBTN, carPhotoBTN, carRegCertBTN, millagePicBTN, back, submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        driverLicence = view.findViewById(R.id.licenseImg);
        prdp = view.findViewById(R.id.prdpImg);
        carPhoto = view.findViewById(R.id.carImg);
        carRegCert = view.findViewById(R.id.carRegCertificateImg);
        millagePic = view.findViewById(R.id.millegeImg);
        back = view.findViewById(R.id.backOnUpload);
        submit = view.findViewById(R.id.submit_driver_application);

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
        back.setOnClickListener(this);

        submit.setOnClickListener(v -> {
            if (driverLicence.getDrawable() == null || prdp.getDrawable() == null ||
                    carPhoto.getDrawable() == null || carRegCert.getDrawable() == null
                    || millagePic.getDrawable() == null) {

                String message = "Your Application Will Not be Processed Until you Upload all Required Images";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Warning");
                builder.setMessage(message);
                builder.setPositiveButton("Continue Now!",
                        (dialog, which) -> {
                        });
                builder.setNegativeButton(null, (dialog, which) -> {
                    //TODO
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                String message = "Your Application was Successfully Submitted and you will be notified via the email";
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Online Registration Confirmation");
                builder.setMessage(message);
                builder.setPositiveButton("OKAY!",
                        (dialog, which) -> {

                            String keyId = firebaseAuth.getCurrentUser().getUid();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("users").child(keyId).child("driver");

                            myRef.setValue(true).addOnSuccessListener(aVoid -> {
                                FragmentTransaction t = getFragmentManager().beginTransaction();
                                Fragment mFrag = new RegisterFragment();
                                t.replace(R.id.frameLayout, mFrag);
                                t.commit();
                            });

                        });
                builder.setNegativeButton(null, (dialog, which) -> {
                    //TODO
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.licenseBtn) {
            selectedBtn = 1;
            imageName = "license";
        } else if (v.getId() == R.id.prdpBtn) {
            selectedBtn = 2;
            imageName = "pdp";
        } else if (v.getId() == R.id.carBtn) {
            selectedBtn = 3;
            imageName = "car";
        } else if (v.getId() == R.id.carRegCertificateBtn) {
            selectedBtn = 4;
            imageName = "carRegCertificate";
        } else if (v.getId() == R.id.millegeBtn) {
            selectedBtn = 5;
            imageName = "millage";
        } else if (v.getId() == R.id.backOnUpload) {
            FragmentTransaction t = getFragmentManager().beginTransaction();
            Fragment mFrag = new CarInformationFragment();
            t.replace(R.id.frameLayout, mFrag);
            t.commit();
            return;
        }
        openLocalStorage();
    }

    void uploadImage(Uri file) {

        StorageReference riversRef = mStorageRef.child("Images/" + firebaseAuth.getCurrentUser().getUid() + "/" + imageName);

        riversRef.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
                })
                .addOnFailureListener(exception -> Toast.makeText(getContext(), "image uploaded failed", Toast.LENGTH_LONG).show())
                .addOnProgressListener(snapshot -> {
                    double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    String p = ((int) progressPercentage) + "%";
                    uploadProgress.setText(p);
                });
    }

    void openLocalStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
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

                switch (selectedBtn) {
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

    @Override
    public void onStart() {
        super.onStart();
        getUserLicenseFromDB();
        getUserPrDPFromDB();
        getUserCarPhotoFromDB();
        getUserCarCertificateFromDB();
        getUserCarMillageFromDB();

    }

    private void getUserLicenseFromDB() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        StorageReference riversRef = mStorageRef.child("Images/" + userID + "/license");
        try {
            final File localFile = File.createTempFile("license", "*");
            riversRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                if (getActivity() != null) {
                    Glide.with(getContext()).load(String.valueOf(localFile.toURI())).into(driverLicence);
                }
            }).addOnFailureListener(e -> {
            });
        } catch (IOException ignored) {
        }
    }

    private void getUserPrDPFromDB() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        StorageReference riversRef = mStorageRef.child("Images/" + userID + "/pdp");
        try {
            final File localFile = File.createTempFile("pdp", "*");
            riversRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                if (getActivity() != null) {
                    Glide.with(getContext()).load(String.valueOf(localFile.toURI())).into(prdp);
                }
            }).addOnFailureListener(e -> {
            });
        } catch (IOException ignored) {
        }
    }

    private void getUserCarPhotoFromDB() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        StorageReference riversRef = mStorageRef.child("Images/" + userID + "/car");
        try {
            final File localFile = File.createTempFile("car", "*");
            riversRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                if (getActivity() != null) {
                    Glide.with(getContext()).load(String.valueOf(localFile.toURI())).into(carPhoto);
                }
            }).addOnFailureListener(e -> {
            });
        } catch (IOException ignored) {
        }
    }

    private void getUserCarCertificateFromDB() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        StorageReference riversRef = mStorageRef.child("Images/" + userID + "/carRegCertificate");
        try {
            final File localFile = File.createTempFile("carRegCertificate", "*");
            riversRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                if (getActivity() != null) {
                    Glide.with(getContext()).load(String.valueOf(localFile.toURI())).into(carRegCert);
                }
            }).addOnFailureListener(e -> {
            });
        } catch (IOException ignored) {
        }
    }

    private void getUserCarMillageFromDB() {
        String userID = firebaseAuth.getCurrentUser().getUid();
        StorageReference riversRef = mStorageRef.child("Images/" + userID + "/millage");
        try {
            final File localFile = File.createTempFile("millage", "*");
            riversRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                if (getActivity() != null) {
                    Glide.with(getContext()).load(String.valueOf(localFile.toURI())).into(millagePic);
                }
            }).addOnFailureListener(e -> {
            });
        } catch (IOException ignored) {
        }
    }
}