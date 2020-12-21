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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.xcoding.rideshare.adapters.RequestsAdapter;
import com.xcoding.rideshare.modals.LongDistanceCommuteModal;
import com.xcoding.rideshare.modals.RequestRideModel;
import com.xcoding.rideshare.modals.ShortDistanceCommuteModal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OfferRideFragment extends Fragment implements View.OnClickListener {

    private DatePickerDialog.OnDateSetListener mDateListener;
    Button longDistance, dailyCommute;
    private BottomSheetDialog bottomSheetDialog;
    ImageView longDistanceSheetClose;
    EditText departureDate;


    RecyclerView recyclerView;
    List<RequestRideModel> requestRideModel;
    RequestsAdapter requestsAdapter;
    DatabaseReference reference;
    FirebaseAuth fAuth;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer_ride, container, false);
        longDistance = view.findViewById(R.id.button_long_distance_id);
        dailyCommute = view.findViewById(R.id.button_daily_commute);
        longDistanceSheetClose = view.findViewById(R.id.long_distance_sheet_close);
        departureDate = view.findViewById(R.id.long_distance_date);
        firebaseAuth = FirebaseAuth.getInstance();
        fAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.offerRidesRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRideModel = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final Bundle bundle = getActivity().getIntent().getExtras();

        longDistance.setOnClickListener((View.OnClickListener) view17 -> {
            String carMake = "";
            boolean driver = false;
            try {
                carMake = bundle.getString("make");
                driver = bundle.getBoolean("isDriver");
            } catch (Exception ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (carMake != null) {
                if (driver) {

                    bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);

                    final View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.long_distance_bottom_sheet_layout,
                            (ViewGroup) view17.findViewById(R.id.long_distance_bottom_sheet));

                    //getValues from a bottom sheet here
                    sheetView.findViewById(R.id.long_distance_sheet_close).setOnClickListener(view16 -> bottomSheetDialog.dismiss());

                    sheetView.findViewById(R.id.long_distance_date).setOnClickListener(view15 -> {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,//Theme_Holo_Dialog_MinWidth
                                mDateListener, year, month, day);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        mDateListener = (datePicker, year13, month13, date) -> {
                            month13 += 1;

                            String fullDate = date + "/" + month13 + "/" + year13;
                            EditText dateInput = sheetView.findViewById(R.id.long_distance_date);
                            dateInput.setText(fullDate);
                        };
                    });

                    sheetView.findViewById(R.id.btn_long_distance_submit_ride_offer).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view17) {
                            //TODO code what happens when offer ride submit button is clicked in a method called submitLongRide()
                            submitLongRide();
                        }

                        private void submitLongRide() {
                            //TODO validate text-fields, get data and insert in into a database


                            EditText beginning, end, date, sits, price;

                            beginning = sheetView.findViewById(R.id.long_distance_beginning);
                            end = sheetView.findViewById(R.id.long_distance_destination);
                            date = sheetView.findViewById(R.id.long_distance_date);
                            sits = sheetView.findViewById(R.id.long_distance_number_of_sits);
                            price = sheetView.findViewById(R.id.long_distance_price);

                            String inputBeginning = beginning.getText().toString();
                            String inputEnd = end.getText().toString().trim();
                            String inputDate = date.getText().toString().trim();
                            String inputSits = sits.getText().toString().trim();
                            String inputPrice = price.getText().toString().trim();

                            boolean fieldsOkay = true;

                            if (inputBeginning.equals("")) {
                                showError(beginning, "departure location required");
                                fieldsOkay = false;
                            }
                            if (inputEnd.equals("")) {
                                showError(end, "destination required");
                                fieldsOkay = false;
                            }
                            if (inputDate.equals("")) {
                                showError(date, "departure date required");
                                fieldsOkay = false;
                            }
                            if (inputDate.length() > 0) {
                                int d, m, y;
                                try {
                                    d = Integer.parseInt(inputDate.substring(0, 2));
                                    m = Integer.parseInt(inputDate.substring(3, 5));
                                    y = Integer.parseInt(inputDate.substring(6, 10));

                                    int year = Calendar.getInstance().get(Calendar.YEAR);
                                    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                                    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                                    if (y < year) {
                                        fieldsOkay = false;
                                        showError(date, "date invalid");
                                        date.requestFocus();
                                    } else if (y == year && m < month) {
                                        fieldsOkay = false;
                                        showError(date, "date invalid");
                                        date.requestFocus();
                                    }
                                    else if (y == year && m == month && d < day) {
                                        fieldsOkay = false;
                                        showError(date, "date invalid");
                                        date.requestFocus();
                                    }
                                } catch (Exception ex) {
                                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                    showError(date, "date invalid");
                                    date.requestFocus();
                                    fieldsOkay = false;
                                }
                            }
                            if (inputSits.equals("")) {
                                showError(sits, "no of sits required");
                                fieldsOkay = false;
                            } else {
                                try {
                                    if (Integer.parseInt(inputSits) > 4) {
                                        showError(sits, "too many passenger sits, you can only offer 4 or less");
                                        fieldsOkay = false;
                                    } else if (Integer.parseInt(inputSits) <= 0) {
                                        showError(sits, "sits cannot be less than 1");
                                        fieldsOkay = false;
                                    }
                                } catch (Exception ignored) {

                                }
                            }
                            if (inputPrice.equals("")) {
                                showError(price, "price per sit required");
                                fieldsOkay = false;
                            } else {
                                try {
                                    if (Integer.parseInt(inputSits) <= 0) {
                                        showError(price, "invalid number sits");
                                        fieldsOkay = false;
                                    }
                                } catch (Exception ignored) {

                                }
                            }


                            if (fieldsOkay) {
                                LongDistanceCommuteModal longDistance = new LongDistanceCommuteModal();
                                longDistance.setBeginning(inputBeginning);
                                longDistance.setDestination(inputEnd);
                                longDistance.setDate(inputDate);
                                longDistance.setSits(inputSits);
                                longDistance.setPrice(inputPrice);
                                longDistance.setRideSourceID(firebaseAuth.getCurrentUser().getUid());
                                String fnam = bundle.getString("lastNameFromDB") + " " + bundle.getString("firstName");
                                longDistance.setRideSourceName(fnam);

                                Date currentDate = new Date();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
                                longDistance.setTime(formatter.format(currentDate));

                                //writing the trip into firebase
                                mDatabase = database.getReference("offeredRides/longDistance");
                                String keyId = firebaseAuth.getCurrentUser().getUid();
                                mDatabase.child(keyId).setValue(longDistance).addOnSuccessListener(aVoid -> {
                                    bottomSheetDialog.dismiss();
                                    String message = "Ride Offer Successfully Created";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Confirmation");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("OKAY!",
                                            (dialog, which) -> {
                                            });
                                    builder.setNegativeButton(null, (dialog, which) -> {
                                        //TODO
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                });
                            }
                        }
                    });


                    bottomSheetDialog.setContentView(sheetView);
                    bottomSheetDialog.show();
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                } else {
                    Toast.makeText(getContext(), "you have not been approved as a driver", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "please register as driver first", Toast.LENGTH_LONG).show();
            }
        });

        dailyCommute.setOnClickListener((View.OnClickListener) view14 -> {

            String carMake = "";
            boolean driver = false;
            try {
                carMake = bundle.getString("make");
                driver = bundle.getBoolean("isDriver");
            } catch (Exception ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (carMake != null) {
                if (driver) {

                    bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);

                    final View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.daily_commute_bottom_sheet_layout,
                            (ViewGroup) view14.findViewById(R.id.daily_commute_bottom_sheet));

                    //getValues from a bottom sheet here
                    sheetView.findViewById(R.id.short_distance_sheet_close).setOnClickListener(view12 -> bottomSheetDialog.dismiss());


                    sheetView.findViewById(R.id.short_distance_start_date).setOnClickListener(view1 -> {
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
                            String fullDate = date + "/" + month1 + "/" + year1;
                            EditText dateInput = sheetView.findViewById(R.id.short_distance_start_date);
                            dateInput.setText(fullDate);
                        };
                    });


                    sheetView.findViewById(R.id.short_distance_end_date).setOnClickListener(view13 -> {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,//Theme_Holo_Dialog_MinWidth
                                mDateListener, year, month, day);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        mDateListener = (datePicker, year12, month12, date) -> {
                            month12 = month12 + 1;

                            String fullDate = date + "/" + month12 + "/" + year12;
                            EditText dateInput = sheetView.findViewById(R.id.short_distance_end_date);
                            dateInput.setText(fullDate);
                        };
                    });

                    sheetView.findViewById(R.id.btn_short_distance_submit_ride_offer).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view14) {
                            //TODO code what happens when offer ride submit button is clicked in a method called submitShortRide()
                            submitShortRide();
                        }

                        private void submitShortRide() {
                            //TODO validate text-fields, get data and insert in into a database


                            EditText beginning, destination, startDate, endDate, sits, price;

                            beginning = sheetView.findViewById(R.id.short_distance_beginning);
                            destination = sheetView.findViewById(R.id.short_distance_destination);
                            startDate = sheetView.findViewById(R.id.short_distance_start_date);
                            endDate = sheetView.findViewById(R.id.short_distance_end_date);
                            sits = sheetView.findViewById(R.id.short_distance_number_of_sits);
                            price = sheetView.findViewById(R.id.short_distance_price);

                            String inputBeginning = beginning.getText().toString();
                            String inputDestination = destination.getText().toString().trim();
                            String inputStartDate = startDate.getText().toString().trim();
                            String inputEndDate = startDate.getText().toString().trim();
                            String inputSits = sits.getText().toString().trim();
                            String inputPrice = price.getText().toString().trim();

                            boolean dataOkay = true;

                            if (inputBeginning.equals("")) {
                                showError(beginning, "departure location required");
                                dataOkay = false;
                            }
                            if (inputDestination.equals("")) {
                                showError(destination, "destination required");
                                dataOkay = false;
                            }
                            if (inputStartDate.equals("")) {
                                showError(startDate, "starting date required");
                                dataOkay = false;
                            }
                            if (inputStartDate.length() > 0) {
                                int d, m, y;
                                try {
                                    d = Integer.parseInt(inputStartDate.substring(0, 2));
                                    m = Integer.parseInt(inputStartDate.substring(3, 5));
                                    y = Integer.parseInt(inputStartDate.substring(6, 10));

                                    int year = Calendar.getInstance().get(Calendar.YEAR);
                                    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                                    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                                    if (y < year) {
                                        dataOkay = false;
                                        showError(startDate, "date invalid");
                                        startDate.requestFocus();
                                    } else if (y == year && m < month) {
                                        dataOkay = false;
                                        showError(startDate, "date invalid");
                                        startDate.requestFocus();
                                    }
                                    else if (y == year && m == month && d < day) {
                                        dataOkay = false;
                                        showError(startDate, "date invalid");
                                        startDate.requestFocus();
                                    }
                                } catch (Exception ex) {
                                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                    showError(startDate, "date invalid");
                                    startDate.requestFocus();
                                    dataOkay = false;
                                }
                            }

                            if (inputEndDate.equals("")) {
                                showError(endDate, "ending date required");
                                dataOkay = false;
                            }if (inputEndDate.length() > 0) {
                                int d, m, y;
                                try {
                                    d = Integer.parseInt(inputEndDate.substring(0, 2));
                                    m = Integer.parseInt(inputEndDate.substring(3, 5));
                                    y = Integer.parseInt(inputEndDate.substring(6, 10));

                                    int year = Calendar.getInstance().get(Calendar.YEAR);
                                    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                                    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                                    if (y < year) {
                                        dataOkay = false;
                                        showError(endDate, "date invalid");
                                        endDate.requestFocus();
                                    } else if (y == year && m < month) {
                                        dataOkay = false;
                                        showError(endDate, "date invalid");
                                        endDate.requestFocus();
                                    }
                                    else if (y == year && m == month && d < day) {
                                        dataOkay = false;
                                        showError(endDate, "date invalid");
                                        endDate.requestFocus();
                                    }
                                } catch (Exception ex) {
                                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                    showError(endDate, "date invalid");
                                    endDate.requestFocus();
                                    dataOkay = false;
                                }
                            }
                            if (inputSits.equals("")) {
                                showError(sits, "no of sits required");
                                dataOkay = false;
                            } else {
                                try {
                                    if (Integer.parseInt(inputSits) > 4) {
                                        showError(sits, "too many passenger sits, you can only offer 4 or less");
                                        dataOkay = false;
                                    } else if (Integer.parseInt(inputSits) <= 0) {
                                        showError(sits, "sits cannot be less than 1");
                                        dataOkay = false;
                                    }
                                } catch (Exception ignored) {

                                }
                            }
                            if (inputPrice.equals("")) {
                                showError(price, "price per sit required");
                                dataOkay = false;
                            } else {
                                try {
                                    if (Integer.parseInt(inputSits) <= 0) {
                                        showError(sits, "invalid amount");
                                        dataOkay = false;
                                    }
                                } catch (Exception ignored) {

                                }
                            }

                            if (dataOkay) {
                                ShortDistanceCommuteModal commuteModal = new ShortDistanceCommuteModal();

                                Bundle bundle1 = getActivity().getIntent().getExtras();
                                commuteModal.setBeginning(inputBeginning);
                                commuteModal.setDestination(inputDestination);
                                commuteModal.setStartDate(inputStartDate);
                                commuteModal.setEndDate(inputEndDate);
                                commuteModal.setSits(inputSits);
                                commuteModal.setPrice(inputPrice);
                                commuteModal.setRideSourceID(firebaseAuth.getCurrentUser().getUid());
                                commuteModal.setRideSourceName(bundle1.getString("lastNameFromDB") + " " + bundle1.getString("firstName"));

                                Date currentDate = new Date();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
                                commuteModal.setTime(formatter.format(currentDate));

                                //writing the trip into firebase
                                mDatabase = database.getReference("offeredRides/shortDistance");
                                String keyId = firebaseAuth.getCurrentUser().getUid();
                                mDatabase.child(keyId).setValue(commuteModal).addOnSuccessListener(aVoid -> {
                                    bottomSheetDialog.dismiss();
                                    String message = "Ride Offer Successfully Created";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Confirmation");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("OKAY!",
                                            (dialog, which) -> {
                                            });
                                    builder.setNegativeButton(null, (dialog, which) -> {
                                        //TODO
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                });
                            }
                        }
                    });

                    bottomSheetDialog.setContentView(sheetView);
                    bottomSheetDialog.show();
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                } else {
                    Toast.makeText(getContext(), "you have not been approved as a driver", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "please register as driver first", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void showError(EditText inputField, String errorMessage) {
        inputField.setError(errorMessage);
        inputField.requestFocus();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onStart() {
        super.onStart();
        getRideRequests();
    }

    void getRideRequests() {
        reference = FirebaseDatabase.getInstance().getReference("rideRequests");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RequestRideModel modal = dataSnapshot.getValue(RequestRideModel.class);
                    requestRideModel.add(modal);
                }
                requestsAdapter = new RequestsAdapter(requestRideModel, getContext());
                recyclerView.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}