package com.xcoding.rideshare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xcoding.rideshare.R;
import com.xcoding.rideshare.adapters.LongDistanceAdapter;
import com.xcoding.rideshare.modals.LongDistanceCommuteModal;
import com.xcoding.rideshare.modals.ShortDistanceCommuteModal;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    List<LongDistanceCommuteModal> longDistanceCommuteModalList;
    LongDistanceAdapter dailyRideAdapter;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        linearLayout = view.findViewById(R.id.loading_layout);
        fAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        longDistanceCommuteModalList = new ArrayList<>();
//        Timer timer = new Timer();
//        timer.schedule(new Check(), 0, 5000);

        return view;
    }

    class Check extends TimerTask {
        public void run() {
            longDistanceCommuteModalList.clear();
            getLongRides();
            getShortRides();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getShortRides();
        getLongRides();
    }

    @Override
    public void onClick(View view) {
    }

    void getLongRides(){
        reference = FirebaseDatabase.getInstance().getReference("offeredRides/longDistance");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    LongDistanceCommuteModal modal = dataSnapshot.getValue(LongDistanceCommuteModal.class);
                    longDistanceCommuteModalList.add(modal);
                }
                dailyRideAdapter = new LongDistanceAdapter(longDistanceCommuteModalList,getContext());
                recyclerView.setAdapter(dailyRideAdapter);
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getShortRides(){
        reference = FirebaseDatabase.getInstance().getReference("offeredRides/shortDistance");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    LongDistanceCommuteModal modal = dataSnapshot.getValue(LongDistanceCommuteModal.class);
                    longDistanceCommuteModalList.add(modal);
                }
                dailyRideAdapter = new LongDistanceAdapter(longDistanceCommuteModalList,getContext());
                recyclerView.setAdapter(dailyRideAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
