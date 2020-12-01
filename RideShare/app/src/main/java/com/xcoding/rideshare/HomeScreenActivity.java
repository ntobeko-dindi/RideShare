package com.xcoding.rideshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;
import com.xcoding.rideshare.fragments.DriverRegistrationFragment;
import com.xcoding.rideshare.fragments.HomeFragment;
import com.xcoding.rideshare.fragments.MoreFragment;
import com.xcoding.rideshare.fragments.OfferRideFragment;
import com.xcoding.rideshare.fragments.ProfileFragment;
import com.xcoding.rideshare.fragments.RequestRideFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    Animation open, close, clockwise, anticlockwise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

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
        menuItems.add(new MenuItem("Online Registration", R.drawable.registration));
        menuItems.add(new MenuItem("More",R.drawable.moreaction));

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
                        fragmentClass = DriverRegistrationFragment.class;
                        break;
                    }
                    case 5:{
                        fragmentClass = MoreFragment.class;
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
}