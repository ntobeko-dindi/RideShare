package com.xcoding.rideshare.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xcoding.rideshare.R;
import com.xcoding.rideshare.adapters.LongDistanceAdapter;
import com.xcoding.rideshare.modals.LongDistanceCommuteModal;
import com.xcoding.rideshare.modals.PendingRides;
import com.xcoding.rideshare.modals.RequestRideModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class RequestRideFragment extends Fragment implements View.OnClickListener, LongDistanceAdapter.OnRideListener {

    private DatePickerDialog.OnDateSetListener mDateListener;
    EditText requesterLocation;
    EditText requesterDestination;
    EditText requesterDepartureDate;
    Button submitRequest;
    LongDistanceAdapter dailyRideAdapter;
    RecyclerView recyclerView;
    private RequestRideModel requestRideModel;
    FirebaseAuth fAuth;
    private DatabaseReference mDatabase;
    FirebaseDatabase database;

    List<LongDistanceCommuteModal> longDistanceCommuteModalList;
    LongDistanceAdapter longDistanceAdapter;
    DatabaseReference reference;
    private BottomSheetDialog bottomSheetDialog;
    private View rootView = null;

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

        this.rootView = view;
        return view;
    }

    @Override
    public void onRideClick(int position) {

        if (longDistanceCommuteModalList.get(position).getRideSourceID().equals(fAuth.getCurrentUser().getUid())) {
            Toast.makeText(getContext(), "you cannot accept your on ride offer", Toast.LENGTH_SHORT).show();
        } else {
            bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);

            View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.ride_booking_sheet,
                    (ViewGroup) rootView.findViewById(R.id.book_ride_bottom_sheet));

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.setCanceledOnTouchOutside(false);

            sheetView.findViewById(R.id.close).setOnClickListener(view -> bottomSheetDialog.dismiss());

            EditText sits = sheetView.findViewById(R.id.availableSits);
            String leftSits = longDistanceCommuteModalList.get(position).getSits();
            String txt = leftSits + " Sits Available";
            sits.setText(txt);

            EditText price = sheetView.findViewById(R.id.booking_price);
            String pricePerSit = longDistanceCommuteModalList.get(position).getPrice();
            String bookingPrice = "R " + pricePerSit;
            price.setText(bookingPrice);

            EditText bookingSits = sheetView.findViewById(R.id.bookingSits);
            bookingSits.setText("1");

            if (leftSits.equals("0")) {
                String message = "Unfortunately there are no More Sits Available for this Ride";

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("ERROR");
                builder.setMessage(message);
                builder.setPositiveButton("OKAY!",
                        (dialog, which) -> {
                        });
                builder.setNegativeButton(null, (dialog, which) -> {
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                bottomSheetDialog.show();
                sheetView.findViewById(R.id.submitBooking).setOnClickListener(view -> {
                    String inputSits = bookingSits.getText().toString();

                    if (Integer.parseInt(inputSits) > Integer.parseInt(leftSits)) {
                        bookingSits.setError("booked sits exceed available sits");
                        bookingSits.requestFocus();
                    } else if (Integer.parseInt(inputSits) < 1) {
                        bookingSits.setError("cannot book less than 1 sit");
                        bookingSits.requestFocus();
                    } else {
                        double totP = Double.parseDouble(pricePerSit) * Integer.parseInt(inputSits);
                        String message = "Number of Sits : " + inputSits + "\n\nTotalPrice : R " + totP;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(true);
                        builder.setTitle("Confirm Ride Booking");
                        builder.setMessage(message);
                        builder.setPositiveButton("Confirm",
                                (dialog, which) -> {
                                    bottomSheetDialog.dismiss();
                                    writeRide(longDistanceCommuteModalList.get(position).getRideSourceID(),
                                            fAuth.getCurrentUser().getUid(), bookingSits.getText().toString(),
                                            longDistanceCommuteModalList.get(position).getTime(), totP,
                                            longDistanceCommuteModalList.get(position).getSits());
                                    Toast.makeText(getContext(), String.valueOf(totP), Toast.LENGTH_LONG).show();
                                });
                        builder.setNegativeButton("Back", (dialog, which) -> {
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        }
    }

    private void writeRide(String rideSourceID, String uid, String sitsBooked, String date, double totalPrice, String originalSits) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pendingRides/").child(rideSourceID + uid);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String bs = snapshot.child("bookedSits").getValue(String.class);

                    String message = "you have already booked a " + bs + " sit(s) in this ride, would you like to add to the existing sits?";
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Ride Booking Prompt");
                    builder.setMessage(message);
                    builder.setPositiveButton("YES!",
                            (dialog, which) -> {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("pendingRides").child(rideSourceID + uid);

                                // Read from the database
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.child("bookedSits").getValue(String.class);
                                        String oldPrice = dataSnapshot.child("totalRidePrice").getValue(String.class);

                                        Toast.makeText(getContext(), "booked sits from user " + sitsBooked, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(), "booked sits from database " + value, Toast.LENGTH_LONG).show();


                                        int newNumSit = Integer.parseInt(sitsBooked) + Integer.parseInt(value);
                                        double newPrice = (totalPrice / Double.parseDouble(sitsBooked)) * newNumSit;

                                        Toast.makeText(getContext(), "old price R " + oldPrice, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(), "new price R " + newPrice, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getContext(), "new number sits " + newNumSit, Toast.LENGTH_LONG).show();


                                        myRef.removeEventListener(this);
                                        myRef.child("bookedSits").setValue(String.valueOf(newNumSit))
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(getContext(), "ride booked successfully", Toast.LENGTH_LONG).show();
                                                    reduceRemainingSits(rideSourceID, sitsBooked, originalSits);
                                                });
                                        myRef.child("totalRidePrice").setValue(String.valueOf(newPrice));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                reference.removeEventListener(this);
                            });
                    builder.setNegativeButton("NO!", (dialog, which) -> {
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    PendingRides pendingRides = new PendingRides();
                    pendingRides.setDriverID(rideSourceID);
                    pendingRides.setPassengerID(uid);
                    pendingRides.setBookedSits(sitsBooked);
                    pendingRides.setTotalRidePrice(String.valueOf(totalPrice));
                    String date1 = date.substring(0, 8);
                    pendingRides.setExpiryDate(date1);
                    mDatabase.child(rideSourceID + uid).setValue(pendingRides).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "ride booked successfully", Toast.LENGTH_LONG).show());
                    reduceRemainingSits(rideSourceID, sitsBooked, originalSits);
                    reference.removeEventListener(this);
                }
            }

            private void reduceRemainingSits(String rideSourceID, String bookedSits, String originalSits) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("offeredRides").child("longDistance").child(rideSourceID).child("sits");

                int newSits = Integer.parseInt(originalSits) - Integer.parseInt(bookedSits);
                myRef.setValue(String.valueOf(newSits)).addOnSuccessListener(aVoid -> {
                    longDistanceCommuteModalList.clear();
                    getLongRides();
                    getShortRides();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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
            requestRideModel.setRideSourceName(bundle.getString("lastNameFromDB") + " " + bundle.getString("firstName"));
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

    void getLongRides() {
        reference = FirebaseDatabase.getInstance().getReference("offeredRides/longDistance");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LongDistanceCommuteModal modal = dataSnapshot.getValue(LongDistanceCommuteModal.class);
                    longDistanceCommuteModalList.add(modal);
                }
                longDistanceAdapter = new LongDistanceAdapter(longDistanceCommuteModalList, getContext(), RequestRideFragment.this::onRideClick);
                recyclerView.setAdapter(longDistanceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getShortRides() {
        reference = FirebaseDatabase.getInstance().getReference("offeredRides/shortDistance");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LongDistanceCommuteModal modal = dataSnapshot.getValue(LongDistanceCommuteModal.class);
                    longDistanceCommuteModalList.add(modal);
                }
                dailyRideAdapter = new LongDistanceAdapter(longDistanceCommuteModalList, getContext(), RequestRideFragment.this::onRideClick);
                recyclerView.setAdapter(dailyRideAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
