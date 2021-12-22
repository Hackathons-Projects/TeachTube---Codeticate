package com.hackathon.teachtube.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CustomPagerAdapter extends FragmentPagerAdapter
{

    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public CustomPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public void addFragment(Fragment fragment)
    {
        arrayList.add(fragment);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }
}
