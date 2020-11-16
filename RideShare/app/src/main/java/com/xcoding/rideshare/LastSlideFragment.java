package com.xcoding.rideshare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LastSlideFragment extends Fragment {
    Animation imgin,imgout,btnanim;
    ImageView slideOneImg;
    TextView welcome,slide_count,header,text;
    public LastSlideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_last_slide, container, false);

        imgin = AnimationUtils.loadAnimation(getContext(),R.anim.imgin);
        imgout = AnimationUtils.loadAnimation(getContext(),R.anim.imgout);
        btnanim = AnimationUtils.loadAnimation(getContext(),R.anim.btnanim);

        slideOneImg = view.findViewById(R.id.slide_three_img);
        welcome = view.findViewById(R.id.slide_three_welcome);
        slide_count = view.findViewById(R.id.slide_three_slide_count);
        header = view.findViewById(R.id.slide_three_header);
        text = view.findViewById(R.id.slide_three_text);

        slideOneImg.startAnimation(imgin);
        welcome.startAnimation(imgout);
        slide_count.startAnimation(imgout);
        header.startAnimation(imgout);
        text.startAnimation(imgout);

        return view;
    }
        @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}