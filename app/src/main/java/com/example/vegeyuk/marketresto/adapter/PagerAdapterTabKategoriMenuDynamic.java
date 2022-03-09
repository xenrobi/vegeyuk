package com.example.vegeyuk.marketresto.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vegeyuk.marketresto.fragment.MenuFragment;
import com.example.vegeyuk.marketresto.models.Kategori;
import com.example.vegeyuk.marketresto.models.Menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagerAdapterTabKategoriMenuDynamic extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private String id_resto;
    private List<Menu> menuList = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();
    private String oprasional;

    public PagerAdapterTabKategoriMenuDynamic(FragmentManager fm, int NumOfTabs, List<Menu> menuList, List<Kategori> kategoriList, String operasional) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.menuList = menuList;
        this.kategoriList = kategoriList;
        this.oprasional = operasional;

    }

    @Override
    public Fragment getItem(int position) {

        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putSerializable("menu", (Serializable) menuList);
        b.putSerializable("kategori", (Serializable) kategoriList);
        b.putInt("oprasinal", Integer.valueOf(oprasional));
        Fragment frag = MenuFragment.newInstance();
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
