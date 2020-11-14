package com.xcoding.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Rides[] myMovieData = new Rides[]{
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),
                new Rides("Ntobeko Dindi","Any delicate you how kindness horrible outlived servants. You high bed wish help call draw side. Girl quit if case mr sing as no have. At none neat am do over will. ",R.drawable.ic_baseline_person_black,"11h ago"),

        };

        RideAdapter myMovieAdapter = new RideAdapter(myMovieData,HomeFragment.this);
        recyclerView.setAdapter(myMovieAdapter);

        return view;
    }

    @Override
    public void onClick(View view) {
    }
}
