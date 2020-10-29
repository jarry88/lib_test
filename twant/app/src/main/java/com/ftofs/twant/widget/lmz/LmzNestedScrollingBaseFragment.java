package com.ftofs.twant.widget.lmz;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.gzp.lib_common.base.BaseFragment;

public class LmzNestedScrollingBaseFragment extends BaseFragment implements LmzScrollableContainer {
    protected RecyclerView rvList;

    @Override
    public View getScrollableView() {
        return rvList;
    }
}
