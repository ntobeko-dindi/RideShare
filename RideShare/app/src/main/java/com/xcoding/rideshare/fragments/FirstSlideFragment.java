package com.xcoding.rideshare.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcoding.rideshare.R;

public class FirstSlideFragment extends Fragment {

    Animation imgin,imgout,btnanim;
    ImageView slideOneImg;
    TextView slide_one_welcome,slide_one_slide_count,slide_one_header,slide_one_text;

    public FirstSlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_slide, container, false);

        imgin = AnimationUtils.loadAnimation(getContext(),R.anim.imgin);
        imgout = AnimationUtils.loadAnimation(getContext(),R.anim.imgout);
        btnanim = AnimationUtils.loadAnimation(getContext(),R.anim.btnanim);

        slideOneImg = view.findViewById(R.id.slide_one_img);
        slide_one_welcome = view.findViewById(R.id.slide_one_welcome);
        slide_one_slide_count = view.findViewById(R.id.slide_one_slide_count);
        slide_one_header = view.findViewById(R.id.slide_one_header);
        slide_one_text = view.findViewById(R.id.slide_one_text);

        slideOneImg.startAnimation(imgin);
        slide_one_welcome.startAnimation(imgout);
        slide_one_slide_count.startAnimation(imgout);
        slide_one_header.startAnimation(imgout);
        slide_one_text.startAnimation(imgout);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}