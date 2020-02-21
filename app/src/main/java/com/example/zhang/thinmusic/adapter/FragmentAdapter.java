package com.example.zhang.thinmusic.adapter;

import 	androidx.fragment.app.Fragment;
import 	androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment适配器
 * Created by zhang on 2018/3/26.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> Fragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fragmentManager) {super(fragmentManager);}

    public void addFragment(Fragment fragment) {Fragments.add(fragment);}

    @Override
    public Fragment getItem(int position){return Fragments.get(position);}

    @Override
    public int getCount(){return  Fragments.size();}
}
