package com.truongcongphi.mymusic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.truongcongphi.mymusic.Fragment.AccountFragment;
import com.truongcongphi.mymusic.Fragment.AccountFragment2;
import com.truongcongphi.mymusic.Fragment.HomeFragment;
import com.truongcongphi.mymusic.Fragment.SearchFragment;
import com.truongcongphi.mymusic.Fragment.SettingFragment;
import com.truongcongphi.mymusic.R;

public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private AccountFragment accountFragment;
    private Fragment currentFragment;


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
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof AccountFragment2) {
            // Quay lại AccountFragment nếu đang ở AccountFragment2
            setFragment(accountFragment);
        }
        if (fragment instanceof HomeFragment) {
            if (!((HomeFragment) fragment).onBackPressed()) {
                // Nếu Fragment không xử lý sự kiện "back", gọi lại phương thức của Activity
                super.onBackPressed();
            }
        }
        if (fragment instanceof AccountFragment) {
            setFragment(homeFragment);
        }
        if (fragment instanceof SearchFragment) {
            setFragment(homeFragment);
        }
        if (fragment instanceof SettingFragment) {
            AccountFragment accountFragment2= new AccountFragment();
            setFragment(accountFragment2);
        }
    }


}