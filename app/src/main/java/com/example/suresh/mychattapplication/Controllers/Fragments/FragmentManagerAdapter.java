package com.example.suresh.mychattapplication.Controllers.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class FragmentManagerAdapter extends FragmentPagerAdapter {


    public FragmentManagerAdapter(FragmentManager fm)  {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ActiveUsers();
            case 1:
                return new Chats();
            case 2:
                return new FriendList();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public String getPageTitle(int position){
        switch (position){
            case 0:
                return "ACTIVE USERS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";
            default:
                    return null;
        }
    }
}
