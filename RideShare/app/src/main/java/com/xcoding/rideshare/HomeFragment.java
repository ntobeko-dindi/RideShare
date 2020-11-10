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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeFragment extends Fragment implements View.OnClickListener {

    //TextView textView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

       // textView = view.findViewById(R.id.someText);

        //StringBuilder stringBuilder = new StringBuilder();

        //String text = "i hope this shit works okay this time.";

       // for (int c = 0; c < 500; c++){
            //stringBuilder.append(text);
        //}

       // textView.setText(stringBuilder.toString());

        return view;
    }

    @Override
    public void onClick(View view) {
    }
}
