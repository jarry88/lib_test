package com.ftofs.twant.widget.lmz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class LmzNestedScrollingFragmentAdapter extends LmzBaseFragmentAdapter<LmzNestedScrollingDemoFragment>{

    public LmzNestedScrollingFragmentAdapter(FragmentManager fm, List<LmzNestedScrollingDemoFragment> fragments) {
        super(fm, fragments);
        this.fragments = fragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Fragment:" + position;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object.getClass().getName().equals(LmzNestedScrollingDemoFragment.class.getName())){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
