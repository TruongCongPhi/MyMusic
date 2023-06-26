package com.truongcongphi.mymusic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.truongcongphi.mymusic.Fragment.FragmentSongBefore;
import com.truongcongphi.mymusic.Fragment.FragmentSongCurrent;
import com.truongcongphi.mymusic.Fragment.FragmentSongLater;

import java.util.ArrayList;

public class ViewPagerPlaylistSong extends FragmentPagerAdapter {
    public final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    public ViewPagerPlaylistSong(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new FragmentSongBefore();
            case 1:
                return new FragmentSongCurrent();
            case 2:
                return new FragmentSongLater();
            default:
                return new FragmentSongCurrent();
        }
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentArrayList.add(fragment);

    }


}
