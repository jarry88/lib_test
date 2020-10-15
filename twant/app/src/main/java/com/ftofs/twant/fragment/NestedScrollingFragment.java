package com.ftofs.twant.fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;


/**
 * 嵌套滚动Fragment
 * @author zwm
 */
public class NestedScrollingFragment extends BaseFragment {
    protected RecyclerView rvList;
    protected boolean NestedScrollingEnabled = false;

    public void setNestedScrollingEnabled(boolean enabled) {
        // SLog.info("rvList[%s]", rvList);
        if (rvList != null) {
            rvList.setNestedScrollingEnabled(enabled);
        }
    }
}
