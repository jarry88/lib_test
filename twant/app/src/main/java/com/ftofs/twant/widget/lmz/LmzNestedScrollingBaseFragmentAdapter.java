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
public class LmzNestedScrollingBaseFragmentAdapter extends LmzBaseFragmentAdapter<LmzNestedScrollingBaseFragment>{

    public LmzNestedScrollingBaseFragmentAdapter(FragmentManager fm, List<LmzNestedScrollingBaseFragment> fragments) {
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
        if (object instanceof LmzNestedScrollingBaseFragment) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}

