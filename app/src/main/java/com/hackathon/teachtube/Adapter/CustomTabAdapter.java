package com.hackathon.teachtube.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CustomTabAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> TabsLayoutFragments = new ArrayList<>();
    private ArrayList<String> TabsLayoutTitle = new ArrayList<>();

    public CustomTabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return TabsLayoutFragments.get(position);
    }

    @Override
    public int getCount() {
        return TabsLayoutFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TabsLayoutTitle.get(position);
    }

    public void addFragment(Fragment fragment , String title)
    {
        TabsLayoutTitle.add(title);
        TabsLayoutFragments.add(fragment);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
    }
}
