package com.example.vegeyuk.marketresto.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vegeyuk.marketresto.fragment.FragmentListFavorit;
import com.example.vegeyuk.marketresto.fragment.FragmentListHistory;

public class PagerAdapterTabFavorit extends FragmentStatePagerAdapter {

    private int number_tbas;

    public PagerAdapterTabFavorit(FragmentManager fm, int number_tbas) {
        super(fm);
        this.number_tbas = number_tbas;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentListFavorit();
            case 1:
                return new FragmentListHistory();
        }
        return null;
    }

    @Override
    public int getCount() {
        return number_tbas;
    }
}
