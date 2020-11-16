 package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation imgin,imgout,btnanim;
    ImageView slideOneImg;
    TextView slide_one_welcome,slide_one_slide_count,slide_one_header,slide_one_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button next = findViewById(R.id.first_slide_btn);
        imgin = AnimationUtils.loadAnimation(this,R.anim.imgin);
        imgout = AnimationUtils.loadAnimation(this,R.anim.imgout);
        btnanim = AnimationUtils.loadAnimation(this,R.anim.btnanim);

        slideOneImg = findViewById(R.id.slide_one_img);
        slide_one_welcome = findViewById(R.id.slide_one_welcome);
        slide_one_slide_count = findViewById(R.id.slide_one_slide_count);
        slide_one_header = findViewById(R.id.slide_one_header);
        slide_one_text = findViewById(R.id.slide_one_text);

        slideOneImg.startAnimation(imgin);
        slide_one_welcome.startAnimation(imgout);
        slide_one_slide_count.startAnimation(imgout);
        slide_one_header.startAnimation(imgout);
        slide_one_text.startAnimation(imgout);
        next.startAnimation(btnanim);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SplashActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
        //NextActivity();
    }
//    public void NextActivity()
//    {
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//
//                Intent mainIntent = new Intent(MainActivity.this,LoginActivity.class);
//                MainActivity.this.startActivity(mainIntent);
//                MainActivity.this.finish();
//            }
//        }, 300);
//    }

    /*APP LOGO IMPLEMENTATION
     *   <ImageView
     *   android:id="@+id/imageView"
     *   android:layout_width="wrap_content"
     *   android:layout_height="wrap_content"
     *  android:layout_margin="0dp"
     *  app:srcCompat="@drawable/logo"
     *   android:contentDescription="TODO" />
     */
}