package com.xcoding.rideshare.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xcoding.rideshare.R;

import java.util.Calendar;

public class RequestRideFragment extends Fragment implements View.OnClickListener {

    private DatePickerDialog.OnDateSetListener mDateListener;
    EditText requesterLocation;
    EditText requesterDestination;
    EditText requesterDepartureDate;
    Button submitRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request_ride, container, false);

        requesterLocation = view.findViewById(R.id.requester_location);
        requesterDestination = view.findViewById(R.id.requester_destination);
        requesterDepartureDate = view.findViewById(R.id.requester_departure_date);
        submitRequest = view.findViewById(R.id.search_available_rides);

        requesterDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,//Theme_Holo_Dialog_MinWidth
                        mDateListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                mDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month = month + 1;

                        String selectedDate = date + "/" + month + "/" + year;
                        requesterDepartureDate.setText(selectedDate);
                    }
                };
            }
        });

        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Toast.makeText(getContext(), "Searching", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View view) {
    }

    public boolean validateFields() {
        boolean fieldsOkay = true;

        if (requesterLocation.getText().toString().trim().isEmpty()) {
            requesterLocation.setError("field/s required");
            requesterLocation.requestFocus();
            fieldsOkay = false;
        }
        if (requesterDestination.getText().toString().trim().isEmpty()) {
            requesterDestination.setError("field/s required");
            requesterDestination.requestFocus();
            fieldsOkay = false;
        }
        if (requesterDepartureDate.getText().toString().trim().isEmpty()) {
            requesterDepartureDate.setError("field/s required");
            requesterDepartureDate.requestFocus();
            fieldsOkay = false;
        }
        return fieldsOkay;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
