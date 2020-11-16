package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewPager = findViewById(R.id.viewpager);
        getStarted = findViewById(R.id.get_started_btn);

        List<Fragment> list = new ArrayList<Fragment>();

        list.add(new FirstSlideFragment());
        list.add(new SecondSlideFragment());
        list.add(new LastSlideFragment());

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),list);

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