package com.ygn.ygn_store_management.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ygn.ygn_store_management.MenuFragments.FragmentOrders;
import com.ygn.ygn_store_management.MenuFragments.FragmentReports;
import com.ygn.ygn_store_management.MenuFragments.FragmentSettings;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentOrders();
            case 1:
                return new FragmentReports();
            case 2:
                return new FragmentSettings();
            default:
                return new FragmentReports();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Sipariş";
            case 1:
                return "Raporlama";
            case 2:
                return "Ayarlar";
            default:
                return "";
        }
    }
}