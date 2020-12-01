package com.xcoding.rideshare.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.xcoding.rideshare.LoginActivity;
import com.xcoding.rideshare.R;

public class MoreFragment extends Fragment implements View.OnClickListener{

    Button logout;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    public MoreFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        logout = view.findViewById(R.id.btnLogout);
        progressBar = view.findViewById(R.id.progress_bar);
        relativeLayout = view.findViewById(R.id.relativeLayout);

        logout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogout){
            logout();
        }
    }
    private void logout(){

        relativeLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(getActivity(),new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }
}