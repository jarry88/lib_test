package com.ftofs.twant.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author gzp
 * @param <T>
 */
public abstract class BaseFragmentStateAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    protected List<T> fragments;

    public BaseFragmentStateAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments!=null && position < fragments.size()?fragments.get(position):null;
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.size():0;
    }
}
