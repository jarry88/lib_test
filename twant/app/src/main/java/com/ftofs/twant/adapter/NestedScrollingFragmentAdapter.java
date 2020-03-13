package com.ftofs.twant.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class NestedScrollingFragmentAdapter extends BaseFragmentStateAdapter<Fragment>{

    public NestedScrollingFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm,fragments);
        this.fragments = fragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "test"+position;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object.getClass().getName().equals(Fragment.class.getName())){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
