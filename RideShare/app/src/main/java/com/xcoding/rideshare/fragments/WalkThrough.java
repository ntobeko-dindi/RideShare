package com.xcoding.rideshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xcoding.rideshare.R;

public class WalkThrough extends Fragment implements View.OnClickListener {

    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk_through, container, false);
        next = view.findViewById(R.id.get_started);
        next.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.get_started){
            FragmentTransaction t = this.getFragmentManager().beginTransaction();
            Fragment mFrag = new RegisterFragment();
            t.replace(R.id.frameLayout, mFrag);
            t.commit();
        }
    }
}