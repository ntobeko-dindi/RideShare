package com.xcoding.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.parse.ParseInstallation;
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

    private static String TAG = "ntobeko";
    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    Bundle bundle;
    boolean isDriver = true;
    List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //Inside onCreate()
        storageReference = FirebaseStorage.getInstance().getReference().child("Images/").child(firebaseAuth.getCurrentUser().getUid() + "/profilePic.jpeg");

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        //Creating a list of menu Items

        menuItems = new ArrayList<MenuItem>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.

        readUserInfo();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        Log.d(TAG, "internet " + connected);
        if (connected) {
            bundle = getIntent().getExtras();
            Log.d(TAG, "bundle " + bundle);
            Log.d(TAG, "is driver " + this.isDriver);

        } else {
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
        }

        menuItems.add(new MenuItem("Home", R.drawable.home));
        menuItems.add(new MenuItem("My Profile", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Offer Ride", R.drawable.ford_mustang_car_215784));
        menuItems.add(new MenuItem("Request Ride", R.drawable.request_ride));
        menuItems.add(new MenuItem("Online Registration", R.drawable.registration));
        menuItems.add(new MenuItem("More", R.drawable.moreaction));

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

        sNavigationDrawer.setOnMenuItemClickListener(position -> {
            System.out.println("Position " + position);
            if (isDriver) {
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
                    case 5: {
                        fragmentClass = MoreFragment.class;
                    }
                }
            } else {
                switch (position) {
                    case 0: {
                        fragmentClass = HomeFragment.class;
                        break;
                    }
                    case 1: {
                        fragmentClass = ProfileFragment.class;
                        break;
                    }
                    case 2:
                    case 4: {
                        menuItems.get(2).setTitle("Register Here First");
                        fragmentClass = DriverRegistrationFragment.class;
                        break;
                    }
                    case 3: {
                        fragmentClass = RequestRideFragment.class;
                        break;
                    }
                    case 5: {
                        fragmentClass = MoreFragment.class;
                    }
                }
            }

            sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                @Override
                public void onDrawerOpening() {

                }

                @Override
                public void onDrawerClosing() {
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
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //logout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //logout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragmentClass = null;
        fragment = null;

        readUserInfo();
        readUserCarInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();

        fragmentClass = null;
        fragment = null;

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        if (connected) {
            readUserInfo();
            readUserCarInfo();
        } else {
            Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();

        }
    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void readUserInfo() {

        final String userID = firebaseAuth.getCurrentUser().getUid();
        final String emailFromDB = firebaseAuth.getCurrentUser().getEmail();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstNameFromDB = snapshot.child("firstName").getValue(String.class);
                String lastNameFromDB = snapshot.child("lastName").getValue(String.class);
                String cellNumberFromDB = snapshot.child("cell").getValue(String.class);
                String genderFromDB = snapshot.child("gender").getValue(String.class);
                String dateOfBirth = snapshot.child("dateOfBirth").getValue(String.class);
                if (genderFromDB != null) {
                    isDriver = snapshot.child("driver").getValue(Boolean.class);
                }

                getIntent().putExtra("firstName", firstNameFromDB);
                getIntent().putExtra("lastNameFromDB", lastNameFromDB);
                getIntent().putExtra("email", emailFromDB);
                getIntent().putExtra("cell", cellNumberFromDB);
                getIntent().putExtra("gender", genderFromDB);
                getIntent().putExtra("isDriver", isDriver);
                getIntent().putExtra("dateOfBirth",dateOfBirth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUserCarInfo() {

        final String userID = firebaseAuth.getCurrentUser().getUid();
        final String emailFromDB = firebaseAuth.getCurrentUser().getEmail();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("carInfo").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getIntent().putExtra("licenseNumber", snapshot.child("licenseNumber").getValue(String.class));
                getIntent().putExtra("make", snapshot.child("make").getValue(String.class));
                getIntent().putExtra("model", snapshot.child("model").getValue(String.class));
                getIntent().putExtra("year", snapshot.child("year").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
