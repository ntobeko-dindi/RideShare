package com.xcoding.rideshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xcoding.rideshare.R;

public class DriverRegistrationFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_registration, container, false);


        try {

            Bundle bundle = getActivity().getIntent().getExtras();
            boolean isDriver = bundle.getBoolean("isDriver");

            if (isDriver){
                getFragmentManager().beginTransaction()
                        .replace(R.id. frameLayout, new RegisterFragment())
                        .commit();
            }else {
                throw new Exception();
            }
        } catch (Exception e) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new WalkThrough())
                    .commit();
        }
        return view;
    }
}