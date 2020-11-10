package com.xcoding.rideshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ChatsFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    RequestsFragment requestsFragment;
    ChatHistoryFragment chatHistoryFragment;
    FriendsFragment friendsFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);
        requestsFragment = new RequestsFragment();
        chatHistoryFragment = new ChatHistoryFragment();
        friendsFragment = new FriendsFragment();

        PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager());
        pagerAdapter.addFragment(requestsFragment,"REQUESTS");
        pagerAdapter.addFragment(chatHistoryFragment,"CHATS");
        pagerAdapter.addFragment(friendsFragment,"FRIENDS");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(requestsFragment).commit();
        getFragmentManager().beginTransaction().remove(chatHistoryFragment).commit();
        getFragmentManager().beginTransaction().remove(friendsFragment).commit();
    }
}
