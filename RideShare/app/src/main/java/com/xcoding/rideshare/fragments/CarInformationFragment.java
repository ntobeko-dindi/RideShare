package com.xcoding.rideshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xcoding.rideshare.R;
import com.xcoding.rideshare.modals.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class CarInformationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    Vehicle vehicle;
    Button next, back;

    private Spinner make, model, category, year;
    private EditText licenseNum;

    private ProgressBar progressBar;
    private RelativeLayout layout;

    private List<ParseObject> allObjects;

    private ArrayList<String> makes;
    private ArrayList<String> models;
    private ArrayList<String> years;
    private ArrayList<String> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_information, container, false);

        makes = new ArrayList<>();
        models = new ArrayList<>();
        years = new ArrayList<>();
        categories = new ArrayList<>();

        make = view.findViewById(R.id.editTextMake);
        model = view.findViewById(R.id.editTextModel);
        category = view.findViewById(R.id.editTextCategory);
        year = view.findViewById(R.id.editTextYear);
        licenseNum = view.findViewById(R.id.editTextPlateNumber);
        next = view.findViewById(R.id.nextOnCarDetails);
        back = view.findViewById(R.id.backOnCarDetails);
        layout = view.findViewById(R.id.carInfo_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        allObjects = new ArrayList<>();
        vehicle = new Vehicle();

        make.setOnItemSelectedListener(this);
        model.setOnItemSelectedListener(this);
        year.setOnItemSelectedListener(this);
        category.setOnItemSelectedListener(this);

        next.setOnClickListener(this);
        back.setOnClickListener(this);
        FirebaseDatabase database;
        try {
            firebaseAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference("carInfo");
        } catch (Exception e) {

        }

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.editTextMake) {
            models.clear();
            String m = make.getSelectedItem().toString().trim();
            for (int x = 0; x < allObjects.size(); x++) {
                if (allObjects.get(x).getString("Make").trim().equals(m)) {
                    String mdl = allObjects.get(x).getString("Model");
                    boolean exist = false;
                    for (int c = 0; c < models.size(); c++) {
                        try {
                            if (models.get(c).equals(mdl)) {
                                exist = true;
                            }
                        } catch (Exception e) {
                            exist = false;
                        }
                    }
                    if (!exist) {
                        models.add(mdl);
                    }
                }
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, models);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            model.setAdapter(adapter);
        } else if (parent.getId() == R.id.editTextModel) {
            categories.clear();
            String m = make.getSelectedItem().toString().trim();
            String mdl = model.getSelectedItem().toString().trim();
            for (int x = 0; x < allObjects.size(); x++) {
                if (allObjects.get(x).getString("Make").trim().equals(m) && allObjects.get(x).getString("Model").trim().equals(mdl)) {
                    String cart = allObjects.get(x).getString("Category");
                    boolean exist = false;
                    for (int c = 0; c < categories.size(); c++) {
                        try {
                            if (categories.get(c).equals(cart)) {
                                exist = true;
                            }
                        } catch (Exception e) {
                            exist = false;
                        }
                    }
                    if (!exist) {
                        categories.add(cart);
                    }
                }
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            category.setAdapter(adapter);
        } else if (parent.getId() == R.id.editTextCategory) {
            years.clear();
            String m = make.getSelectedItem().toString().trim();
            String mdl = model.getSelectedItem().toString().trim();
            String carte = category.getSelectedItem().toString().trim();
            for (int x = 0; x < allObjects.size(); x++) {
                if (allObjects.get(x).getString("Make").trim().equals(m) && allObjects.get(x).getString("Model").trim().equals(mdl) && allObjects.get(x).getString("Category").trim().equals(carte)) {
                    String y = String.valueOf(allObjects.get(x).getNumber("Year"));
                    boolean exist = false;
                    for (int c = 0; c < years.size(); c++) {
                        try {
                            if (years.get(c).equals(carte)) {
                                exist = true;
                            }
                        } catch (Exception e) {
                            exist = false;
                        }
                    }
                    if (!exist) {
                        years.add(y);
                    }
                }
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, years);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            year.setAdapter(adapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void readObject() {
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Car_Model_List");
        parseQuery.setLimit(10000);
        parseQuery.findInBackground(getAllObjects());
    }

    int skip = 0;

    FindCallback<ParseObject> getAllObjects() {

        return new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    allObjects.addAll(objects);
                    int limit = 10000;
                    if (objects.size() == limit) {
                        skip = skip + limit;
                        ParseQuery query = new ParseQuery("Car_Model_List");
                        query.setSkip(skip);
                        query.setLimit(limit);
                        query.findInBackground(getAllObjects());
                    }
                    //We have a full PokeDex
                    else {
                        //USE FULL DATA AS INTENDED
                        layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        setMakes();
                    }
                }
            }
        };
    }

    private void setMakes() {
        makes.clear();
        for (int x = 0; x < allObjects.size(); x++) {
            String m = allObjects.get(x).getString("Make");
            boolean exist = false;
            for (int c = 0; c < makes.size(); c++) {
                try {
                    if (makes.get(c).equals(m)) {
                        exist = true;
                    }
                } catch (Exception e) {
                    exist = false;
                }
            }
            if (!exist) {
                makes.add(m);
            }
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, makes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        make.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextOnCarDetails) {
            if (validFields()) {
                progressBar.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                String keyId = firebaseAuth.getCurrentUser().getUid();
                mDatabase.child(keyId).setValue(vehicle).addOnSuccessListener(aVoid -> {
                    FragmentTransaction t = getFragmentManager().beginTransaction();
                    Fragment mFrag = new UploadFragment();
                    t.replace(R.id.frameLayout, mFrag);
                    t.commit();
                });
            } else {
            }
        } else if (v.getId() == R.id.backOnCarDetails) {
            FragmentTransaction t = getFragmentManager().beginTransaction();
            Fragment mFrag = new RegisterFragment();
            t.replace(R.id.frameLayout, mFrag);
            t.commit();
        }
    }

    private boolean validFields() {
        boolean validFields = true;

        String makeInput = "";
        String modelInput = "";
        String categoryInput = "";
        String yearInput = "";
        String licenseInput = "";

        try {
            makeInput = make.getSelectedItem().toString();
            modelInput = model.getSelectedItem().toString();
            categoryInput = category.getSelectedItem().toString();
            yearInput = year.getSelectedItem().toString();
            licenseInput = licenseNum.getText().toString();

            if (makeInput.equals("")) {
                //make.setError("make required");
                make.requestFocus();
                validFields = false;
            }
            if (modelInput.equals("")) {
                //model.setError("model required");
                model.requestFocus();
                validFields = false;
            }
            if (yearInput.length() != 4) {
                //year.setError("incorrect year");
                year.requestFocus();
                validFields = false;
            }
            if (licenseInput.equals("")) {
                licenseNum.setError("plate number required");
                licenseNum.requestFocus();
                validFields = false;
            }
            if (validFields) {
                vehicle.setMake(makeInput);
                vehicle.setModel(modelInput);
                vehicle.setCategory(categoryInput);
                vehicle.setYear(yearInput);
                vehicle.setLicenseNumber(licenseInput);
            }

        } catch (Exception ex) {
            validFields = false;
        }
        return validFields;
    }

    @Override
    public void onStart() {
        super.onStart();
        readObject();
    }
}