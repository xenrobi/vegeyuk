package com.example.vegeyuk.marketresto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vegeyuk.marketresto.R;
import com.example.vegeyuk.marketresto.fragment.AccountFragment;
import com.example.vegeyuk.marketresto.fragment.FavoriteFragment;
import com.example.vegeyuk.marketresto.fragment.RestoFragment;
import com.example.vegeyuk.marketresto.fragment.OrderFragment;
import com.example.vegeyuk.marketresto.fragment.SearchFragment;
import com.example.vegeyuk.marketresto.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    SessionManager sessionManager;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        sessionManager = new SessionManager(this);
        checkSessionLogin();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
//        getSupportActionBar().setElevation(0);


        //loading the default fragment
        Fragment f = new RestoFragment();


        loadFragment(f);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.action_resto:
                        fragment = new RestoFragment();
                        //       getSupportActionBar().setTitle("Market Resto");
                        break;

                    case R.id.action_order:
                        fragment = new OrderFragment();
                        //      getSupportActionBar().setTitle("Pesanan");
                        break;
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        break;

                    case R.id.action_favorite:
                        //        getSupportActionBar().setTitle("Favorit");
                        fragment = new FavoriteFragment();
                        break;

                    case R.id.action_account:
//                        getSupportActionBar().setTitle("Akun");
                        fragment = new AccountFragment();
                        break;
                }

                return loadFragment(fragment);
            }
        });
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    private void setPhoneNumber() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {
            //          tvPhoneNumber.setText(user.getPhoneNumber());
        } catch (Exception e) {
            Toast.makeText(this, "Phone Number Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkSessionLogin() {
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(mContext, SigninActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            MainActivity.this.finish();
        } else {
            if (!sessionManager.isGetLocation()) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }

    }

}
