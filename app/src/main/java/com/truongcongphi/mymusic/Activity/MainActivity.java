package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.truongcongphi.mymusic.shownhac.Fragment.AccountFragment;
import com.truongcongphi.mymusic.shownhac.Fragment.HomeFragment;
import com.truongcongphi.mymusic.shownhac.Fragment.SearchFragment;
import com.truongcongphi.mymusic.R;




public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        accountFragment = new AccountFragment();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    setFragment(homeFragment);
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    setFragment(searchFragment);
                    return true;
                } else if (item.getItemId() == R.id.nav_account) {
                    setFragment(accountFragment);
                    return true;
                }
                return false;
            }
        });
        setFragment(homeFragment);
    }
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}