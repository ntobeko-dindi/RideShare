package com.xcoding.rideshare.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener, LongDistanceAdapter.OnRideListener {

    RecyclerView recyclerView;
    List<LongDistanceCommuteModal> longDistanceCommuteModalList;
    LongDistanceAdapter dailyRideAdapter;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private BottomSheetDialog bottomSheetDialog;
    LinearLayout linearLayout;
    private DatabaseReference mDatabase;
    private View rootView = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        linearLayout = view.findViewById(R.id.loading_layout);
        fAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        longDistanceCommuteModalList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("pendingRides");
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

                    String message = "you have already booked a "+ bs +" sit(s) in this ride, would you like to add to the existing sits?";
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Ride Booking Prompt");
                    builder.setMessage(message);
                    builder.setPositiveButton("YES!",
                            (dialog, which) -> {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("pendingRides").child(rideSourceID + uid);

                                int x = 0;
                                // Read from the database
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String value = dataSnapshot.child("bookedSits").getValue(String.class);

                                        int newNumSit = Integer.parseInt(sitsBooked) + Integer.parseInt(value);
                                        double newPrice = (totalPrice / Double.parseDouble(sitsBooked)) * newNumSit;

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
            longDistanceCommuteModalList.clear();
            getLongRides();
            getShortRides();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        if (connected) {
            longDistanceCommuteModalList.clear();
            getShortRides();
            getLongRides();
        } else {
            Toast.makeText(getContext(), "no internet connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
    }

    public void getLongRides() {
        reference = FirebaseDatabase.getInstance().getReference("offeredRides/longDistance");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LongDistanceCommuteModal modal = dataSnapshot.getValue(LongDistanceCommuteModal.class);
                    longDistanceCommuteModalList.add(modal);
                }
                dailyRideAdapter = new LongDistanceAdapter(longDistanceCommuteModalList, getContext(), HomeFragment.this::onRideClick);
                recyclerView.setAdapter(dailyRideAdapter);
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);

                TextView noRides = rootView.findViewById(R.id.noRides);
                if(longDistanceCommuteModalList.size() != 0){
                    noRides.setVisibility(View.GONE);
                }else {
                    noRides.setVisibility(View.VISIBLE);
                }
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
                dailyRideAdapter = new LongDistanceAdapter(longDistanceCommuteModalList, getContext(), HomeFragment.this::onRideClick);
                recyclerView.setAdapter(dailyRideAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
