package com.xcoding.rideshare.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.xcoding.rideshare.modals.RequestRideModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RequestRideFragment extends Fragment implements View.OnClickListener {

    private DatePickerDialog.OnDateSetListener mDateListener;
    EditText requesterLocation;
    EditText requesterDestination;
    EditText requesterDepartureDate;
    Button submitRequest;
    RecyclerView recyclerView;
    private RequestRideModel requestRideModel;
    FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase database;

    List<LongDistanceCommuteModal> longDistanceCommuteModalList;
    LongDistanceAdapter longDistanceAdapter;
    DatabaseReference reference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_ride, container, false);

        fAuth = FirebaseAuth.getInstance();
        requesterLocation = view.findViewById(R.id.requester_location);
        requesterDestination = view.findViewById(R.id.requester_destination);
        requesterDepartureDate = view.findViewById(R.id.requester_departure_date);
        submitRequest = view.findViewById(R.id.search_available_rides);
        requestRideModel = new RequestRideModel();
         database = FirebaseDatabase.getInstance();

        fAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.requestedRidesRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        longDistanceCommuteModalList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();

        requesterDepartureDate.setOnClickListener(view1 -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,//Theme_Holo_Dialog_MinWidth
                    mDateListener, year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            mDateListener = (datePicker, year1, month1, date) -> {
                month1 = month1 + 1;

                String selectedDate = date + "/" + month1 + "/" + year1;
                requesterDepartureDate.setText(selectedDate);
            };
        });

        submitRequest.setOnClickListener(view12 -> {
            if (validateFields()) {

                //writing the trip into firebase
                mDatabase = database.getReference("rideRequests");
                String keyId = fAuth.getCurrentUser().getUid();
                mDatabase.child(keyId).setValue(requestRideModel).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "ride request posted", Toast.LENGTH_LONG).show());
            }
        });
//        Timer timer = new Timer();
//        timer.schedule(new Check(), 0, 5000);

        return view;
    }

    class Check extends TimerTask {
        public void run() {
            //longDistanceCommuteModalList.clear();
            //getLongRides();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View view) {
    }

    public boolean validateFields() {
        boolean fieldsOkay = true;

        String start = requesterLocation.getText().toString().trim();
        String end = requesterDestination.getText().toString().trim();
        String date = requesterDepartureDate.getText().toString().trim();

        if (start.isEmpty()) {
            requesterLocation.setError("field/s required");
            requesterLocation.requestFocus();
            fieldsOkay = false;
        }
        if (end.isEmpty()) {
            requesterDestination.setError("field/s required");
            requesterDestination.requestFocus();
            fieldsOkay = false;
        }
        if (date.isEmpty()) {
            requesterDepartureDate.setError("field/s required");
            requesterDepartureDate.requestFocus();
            fieldsOkay = false;
        }
        if (fieldsOkay) {
            requestRideModel.setBeginning(start);
            requestRideModel.setEnd(end);
            requestRideModel.setDate(date);
            Bundle bundle = getActivity().getIntent().getExtras();
            requestRideModel.setRideSourceName(bundle.getString("lastNameFromDB") +" "+ bundle.getString("firstName"));
            requestRideModel.setRideSourceID(fAuth.getCurrentUser().getUid());

            Date currentDate = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

            requestRideModel.setTime(formatter.format(currentDate));
        }
        return fieldsOkay;
    }

    @Override
    public void onStart() {
        super.onStart();
        getLongRides();
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
                longDistanceAdapter = new LongDistanceAdapter(longDistanceCommuteModalList,getContext());
                recyclerView.setAdapter(longDistanceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
