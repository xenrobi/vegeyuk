package com.example.vegeyuk.marketresto.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.vegeyuk.marketresto.fragment.SearchMenuFragment;
import com.example.vegeyuk.marketresto.fragment.SearchRestoranFragment;
import com.example.vegeyuk.marketresto.models.Menu;
import com.example.vegeyuk.marketresto.models.Restoran;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagerAdapterTabSearch extends FragmentStatePagerAdapter {

    private int number_tbas;
    private List<Menu> menuList = new ArrayList<>();
    private List<Restoran> restoranList = new ArrayList<>();

    public PagerAdapterTabSearch(FragmentManager fm, int number_tbas, List<Menu> menuList, List<Restoran> restoranList) {
        super(fm);
        this.number_tbas = number_tbas;
        this.menuList = menuList;
        this.restoranList = restoranList;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle b = new Bundle();
        Fragment frag;
        switch (position) {
            case 0:
                b.putSerializable("restoran", (Serializable) restoranList);
                frag = SearchRestoranFragment.newInstance();
                frag.setArguments(b);
                return frag;

            case 1:
                b.putSerializable("menu", (Serializable) menuList);
                frag = SearchMenuFragment.newInstance();
                frag.setArguments(b);
                return frag;


        }
        return null;
    }

    @Override
    public int getCount() {
        return number_tbas;
    }
}
