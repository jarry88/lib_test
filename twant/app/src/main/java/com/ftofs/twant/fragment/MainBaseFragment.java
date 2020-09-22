package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.appserver.AppServiceImpl;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

public class MainBaseFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 更新MainFragment裏選中的子Fragment
     * @param selectedFragmentIndex
     */
    public void updateMainSelectedFragment(int selectedFragmentIndex) {
        SLog.info("updateMainSelectedFragment:selectedFragmentIndex[%d]", selectedFragmentIndex);
        AppServiceImpl.Companion.getInstance().updateMainSelectedFragment(this,selectedFragmentIndex);
    }
}
