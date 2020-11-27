package com.xcoding.rideshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xcoding.rideshare.R;
import com.xcoding.rideshare.adapters.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DriverRegistrationFragment extends Fragment implements View.OnClickListener{


    RegisterFragment registerFragment;
    CarInformationFragment carInformationFragment;
    UploadFragment uploadFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_registration, container, false);


        ViewPager viewPager = view.findViewById(R.id.viewpager);
        Button getStarted = view.findViewById(R.id.get_started_btn);

        registerFragment = new RegisterFragment();
        carInformationFragment = new CarInformationFragment();
        uploadFragment = new UploadFragment();

        List<Fragment> list = new ArrayList<Fragment>();

        list.add(registerFragment);
        list.add(carInformationFragment);
        list.add(uploadFragment);

        PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), list);

        viewPager.setAdapter(pagerAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove(registerFragment).commit();
        getFragmentManager().beginTransaction().remove(carInformationFragment).commit();
        getFragmentManager().beginTransaction().remove(uploadFragment).commit();
    }
}