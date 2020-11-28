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
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xcoding.rideshare.R;
import com.xcoding.rideshare.modals.LongDistanceCommuteModal;

import java.util.Calendar;

public class OfferRideFragment extends Fragment implements View.OnClickListener {

    private DatePickerDialog.OnDateSetListener mDateListener;
    Button longDistance, dailyCommute;
    private BottomSheetDialog bottomSheetDialog;
    ImageView longDistanceSheetClose;
    EditText departureDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer_ride, container, false);
        longDistance = view.findViewById(R.id.button_long_distance_id);
        dailyCommute = view.findViewById(R.id.button_daily_commute);
        longDistanceSheetClose = view.findViewById(R.id.long_distance_sheet_close);
        departureDate = view.findViewById(R.id.long_distance_date);



        longDistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);

                    final View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.long_distance_bottom_sheet_layout,
                            (ViewGroup) view.findViewById(R.id.long_distance_bottom_sheet));

                    //getValues from a bottom sheet here
                    sheetView.findViewById(R.id.long_distance_sheet_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                        }
                    });

                    sheetView.findViewById(R.id.long_distance_date).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
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
                                    month += 1;
                                }
                            };
                        }
                    });

                    sheetView.findViewById(R.id.btn_long_distance_submit_ride_offer).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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

                            if(inputBeginning.equals("")){
                                showError(beginning, "departure location required");
                                fieldsOkay = false;
                            }
                            if(inputEnd.equals("")){
                                showError(end, "destination required");
                                fieldsOkay = false;
                            }
                            if(inputDate.equals("")){
                                showError(date, "departure date required");
                                fieldsOkay = false;
                            }
                            if(inputSits.equals("")){
                                showError(sits, "no of sits required");
                                fieldsOkay = false;
                            }
                            if(inputPrice.equals("")){
                                showError(price, "price per sit required");
                                fieldsOkay = false;
                            }

                            if(fieldsOkay){
                                LongDistanceCommuteModal longDistance = new LongDistanceCommuteModal();
                                longDistance.setBeginning(beginning.toString());
                                longDistance.setEnd(end.toString());
                                longDistance.setDate(date.toString());
                                longDistance.setSits(sits.toString());
                                longDistance.setPrice(price.toString());

                                //writing the trip into firebase

                            }
                        }
                    });


                    bottomSheetDialog.setContentView(sheetView);
                    bottomSheetDialog.show();
                    bottomSheetDialog.setCanceledOnTouchOutside(false);
                }
        });

        dailyCommute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);

                final View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.daily_commute_bottom_sheet_layout,
                        (ViewGroup) view.findViewById(R.id.daily_commute_bottom_sheet));

                //getValues from a bottom sheet here
                sheetView.findViewById(R.id.short_distance_sheet_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });


                sheetView.findViewById(R.id.short_distance_start_date);
                sheetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
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
                            }
                        };
                    }
                });


                sheetView.findViewById(R.id.short_distance_end_date);
                sheetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
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

                            }
                        };
                    }
                });

                sheetView.findViewById(R.id.btn_short_distance_submit_ride_offer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO code what happens when offer ride submit button is clicked in a method called submitShortRide()
                        submitShortRide();
                    }

                    private void submitShortRide() {
                        //TODO validate text-fields, get data and insert in into a database


                        EditText beginning, end, startDate, endDate, sits, price;

                        beginning = sheetView.findViewById(R.id.short_distance_beginning);
                        end = sheetView.findViewById(R.id.short_distance_destination);
                        startDate = sheetView.findViewById(R.id.short_distance_start_date);
                        endDate = sheetView.findViewById(R.id.short_distance_end_date);
                        sits = sheetView.findViewById(R.id.short_distance_number_of_sits);
                        price = sheetView.findViewById(R.id.short_distance_price);

                        String inputBeginning = beginning.getText().toString();
                        String inputEnd = end.getText().toString().trim();
                        String inputStartDate = startDate.getText().toString().trim();
                        String inputEndDate = startDate.getText().toString().trim();
                        String inputSits = sits.getText().toString().trim();
                        String inputPrice = price.getText().toString().trim();

                        boolean dataOkay = true;

                        if(inputBeginning.equals("")){
                            showError(beginning, "departure location required");
                            dataOkay = false;
                        }
                        if(inputEnd.equals("")){
                            showError(end, "destination required");
                            dataOkay = false;
                        }
                        if(inputStartDate.equals("")){
                            showError(startDate, "starting date required");
                            dataOkay = false;
                        }
                        if(inputEndDate.equals("")){
                            showError(endDate, "ending date required");
                            dataOkay = false;
                        }
                        if(inputSits.equals("")){
                            showError(sits, "no of sits required");
                            dataOkay = false;
                        }
                        if(inputPrice.equals("")){
                            showError(price, "price per sit required");
                            dataOkay = false;
                        }

                        if(dataOkay){

                        }
                    }
                });


                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
                bottomSheetDialog.setCanceledOnTouchOutside(false);
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
}