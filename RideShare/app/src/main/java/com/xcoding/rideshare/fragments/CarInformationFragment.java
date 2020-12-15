package com.xcoding.rideshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xcoding.rideshare.R;
import com.xcoding.rideshare.modals.Vehicle;

public class CarInformationFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    Vehicle vehicle;
    Button next,back;

    private EditText make, model, year, licenseNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_information, container, false);

        make = view.findViewById(R.id.editTextMake);
        model = view.findViewById(R.id.editTextModel);
        year = view.findViewById(R.id.editTextYear);
        licenseNum = view.findViewById(R.id.editTextPlateNumber);
        next = view.findViewById(R.id.nextOnCarDetails);
        back = view.findViewById(R.id.backOnCarDetails);
        vehicle = new Vehicle();

        next.setOnClickListener(this);
        back.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("carInfo");

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextOnCarDetails) {

            if (validFields()) {
                String keyId = firebaseAuth.getCurrentUser().getUid();
                mDatabase.child(keyId).setValue(vehicle).addOnSuccessListener(aVoid -> {
                    FragmentTransaction t = getFragmentManager().beginTransaction();
                    Fragment mFrag = new UploadFragment();
                    t.replace(R.id.frameLayout, mFrag);
                    t.commit();
                });
            } else {
                Toast.makeText(getContext(), "please fill in all fields", Toast.LENGTH_LONG).show();
            }
        }else if(v.getId() == R.id.backOnCarDetails){
            FragmentTransaction t = getFragmentManager().beginTransaction();
            Fragment mFrag = new RegisterFragment();
            t.replace(R.id.frameLayout, mFrag);
            t.commit();
        }
    }

    private boolean validFields() {
        boolean validFields = true;

        String makeInput = make.getText().toString().trim();
        String modelInput = model.getText().toString().trim();
        String yearInput = year.getText().toString().trim();
        String licenseInput = licenseNum.getText().toString().trim();

        if (makeInput.isEmpty()) {
            make.setError("make required");
            make.requestFocus();
            validFields = false;
        }
        if (modelInput.isEmpty()) {
            model.setError("model required");
            model.requestFocus();
            validFields = false;
        }
        if (yearInput.length() != 4) {
            year.setError("incorrect year");
            year.requestFocus();
            validFields = false;
        }
        if (licenseInput.isEmpty()) {
            licenseNum.setError("plate number required");
            licenseNum.requestFocus();
            validFields = false;
        }
        if(validFields){
            vehicle.setMake(makeInput);
            vehicle.setModel(modelInput);
            vehicle.setYear(yearInput);
            vehicle.setLicenseNumber(licenseInput);
        }
        return validFields;
    }

    @Override
    public void onStart() {
        super.onStart();
        readUserInfo();
    }
    private void readUserInfo() {

        final String userID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("carInfo").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                licenseNum.setText(snapshot.child("licenseNumber").getValue(String.class));
                make.setText(snapshot.child("make").getValue(String.class));
                model.setText(snapshot.child("model").getValue(String.class));
                year.setText(snapshot.child("year").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}