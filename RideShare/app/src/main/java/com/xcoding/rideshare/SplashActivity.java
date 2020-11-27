package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xcoding.rideshare.adapters.PagerAdapter;
import com.xcoding.rideshare.fragments.FirstSlideFragment;
import com.xcoding.rideshare.fragments.LastSlideFragment;
import com.xcoding.rideshare.fragments.SecondSlideFragment;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ViewPager viewPager = findViewById(R.id.viewpager);
        Button getStarted = findViewById(R.id.get_started_btn);

        List<Fragment> list = new ArrayList<Fragment>();

        list.add(new FirstSlideFragment());
        list.add(new SecondSlideFragment());
        list.add(new LastSlideFragment());

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), list);

        viewPager.setAdapter(pagerAdapter);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                //finish();
            }
        });
    }
}