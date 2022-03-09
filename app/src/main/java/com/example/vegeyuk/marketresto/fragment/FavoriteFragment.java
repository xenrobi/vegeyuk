package com.example.vegeyuk.marketresto.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.activities.CartListActivity;
import com.example.vegeyuk.marketresto.adapter.PagerAdapterTabFavorit;


public class FavoriteFragment extends Fragment {


    Context mContext;
    ImageButton cart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        final TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        cart = (ImageButton) view.findViewById(R.id.cart);
        mContext = getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        PagerAdapterTabFavorit pagerAdapterTabFavorit = new PagerAdapterTabFavorit(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapterTabFavorit);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CartListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}