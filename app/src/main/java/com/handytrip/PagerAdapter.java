package com.handytrip;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FindPassword findPassword = new FindPassword();
                return findPassword;
            case 1:
                FindEmail findEmail = new FindEmail();
                return findEmail;
            default:
                FindPassword findPassword1 = new FindPassword();
                return findPassword1;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
