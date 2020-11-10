package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    FloatingActionButton main, createGroup, joinGroup,logout;
    Animation open, close, clockwise, anticlockwise;
    TextView miniFab1Tag, miniFab2Tag,logoutText;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        main = findViewById(R.id.main_fab);
        joinGroup = findViewById(R.id.join_group_id);
        createGroup = findViewById(R.id.create_group_id);
        logout = findViewById(R.id.logout);
        miniFab1Tag = findViewById(R.id.mini_fab1_tag);
        miniFab2Tag = findViewById(R.id.mini_fab2_tag);
        logoutText = findViewById(R.id.logout_text);
        loadingBar = new ProgressDialog(HomeScreenActivity.this);


        miniFab1Tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });

        miniFab2Tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
            }
        });

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        final int[] isOpen = {0};
        main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isOpen[0] == 1) {
                    joinGroup.startAnimation(close);
                    createGroup.startAnimation(close);
                    logout.startAnimation(close);
                    miniFab1Tag.startAnimation(close);
                    miniFab2Tag.startAnimation(close);
                    logoutText.startAnimation(close);
                    main.setImageDrawable(ContextCompat.getDrawable(HomeScreenActivity.this, R.drawable.ic_baseline_add_24));

                    joinGroup.setClickable(false);
                    createGroup.setClickable(false);
                    logout.setClickable(false);
                    miniFab1Tag.setClickable(false);
                    miniFab2Tag.setClickable(false);
                    logoutText.setClickable(false);
                    isOpen[0] = 0;
                } else {
                    joinGroup.startAnimation(open);
                    createGroup.startAnimation(open);
                    logout.startAnimation(open);
                    miniFab1Tag.startAnimation(open);
                    miniFab2Tag.startAnimation(open);
                    logoutText.startAnimation(open);
                    main.setImageDrawable(ContextCompat.getDrawable(HomeScreenActivity.this, R.drawable.ic_baseline_close_24));

                    joinGroup.setClickable(true);
                    createGroup.setClickable(true);
                    logout.setClickable(true);
                    miniFab1Tag.setClickable(true);
                    miniFab2Tag.setClickable(true);
                    logoutText.setClickable(true);
                    isOpen[0] = 1;
                }
            }
        });

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //Inside onCreate()

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        //Creating a list of menu Items

        List<MenuItem> menuItems = new ArrayList<MenuItem>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.

        menuItems.add(new MenuItem("Home", R.drawable.home));
        menuItems.add(new MenuItem("My Profile", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Offer Ride", R.drawable.ford_mustang_car_215784));
        menuItems.add(new MenuItem("Request Ride", R.drawable.request_ride));
        menuItems.add(new MenuItem("Chats", R.drawable.chats));

        //then add them to navigation drawer

        sNavigationDrawer.setMenuItemList(menuItems);
        fragmentClass = HomeFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(
                    android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }

        //Listener to handle the menu item click. It returns the position of the menu item clicked. Based on that you can switch between the fragments.

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);

                switch (position) {
                    case 0: {
                        fragmentClass = HomeFragment.class;
                        break;
                    }
                    case 1: {
                        fragmentClass = ProfileFragment.class;
                        break;
                    }
                    case 2: {
                        fragmentClass = OfferRideFragment.class;
                        break;
                    }
                    case 3: {
                        fragmentClass = RequestRideFragment.class;
                        break;
                    }
                    case 4: {
                        fragmentClass = ChatsFragment.class;
                        break;
                    }
                }

                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpening() {

                    }

                    @Override
                    public void onDrawerClosing() {

                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }

                    }

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerClosed() {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                        System.out.println("State " + newState);
                    }
                });
            }
        });
    }

    private void createGroup() {
        Toast.makeText(HomeScreenActivity.this, "you clicked create group fab", Toast.LENGTH_LONG).show();
    }

    private void joinGroup() {
        Toast.makeText(HomeScreenActivity.this, "you clicked join group fab", Toast.LENGTH_LONG).show();
    }
    private void logout(){
        loadingBar.setTitle("Logging you out");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(HomeScreenActivity.this,new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(HomeScreenActivity.this,LoginActivity.class));
            }
        });
        loadingBar.cancel();
    }
}