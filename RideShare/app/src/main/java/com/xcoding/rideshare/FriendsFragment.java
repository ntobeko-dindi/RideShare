package com.xcoding.rideshare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendsFragment extends Fragment implements View.OnClickListener{

   public FriendsFragment(){

   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   return inflater.inflate(R.layout.fragment_friends, container, false);
   }

    @Override
    public void onClick(View view) { }
}