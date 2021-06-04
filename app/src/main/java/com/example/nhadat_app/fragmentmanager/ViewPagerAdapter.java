package com.example.nhadat_app.fragmentmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int numPager=3;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new TinDang();
            case 1:return new ChuaDuyet();
            case 2:return new TinAn();
            default:return new TinDang();
        }
    }

    @Override
    public int getCount() {
        return numPager;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:return "Tin đăng";
            case 1:return "Tin chưa duyệt";
            case 2:return "Tin đã ẩn";
            default:return "Tin đăng";
        }
    }
}
