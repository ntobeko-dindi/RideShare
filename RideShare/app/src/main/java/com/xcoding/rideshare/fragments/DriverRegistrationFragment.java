package com.xcoding.rideshare.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import com.xcoding.rideshare.R;
import java.util.Objects;

public class DriverRegistrationFragment extends Fragment{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_registration, container, false);


        getFragmentManager().beginTransaction()
                .replace(R.id. frameLayout, new WalkThrough())
                .commit();
        return view;
    }
}