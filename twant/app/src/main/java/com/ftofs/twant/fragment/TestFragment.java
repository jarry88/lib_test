package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentationMagician;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AppUpdatePopup;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.util.List;

import me.yokeyword.fragmentation.SupportHelper;

import static android.support.v4.app.FragmentationMagician.getActiveFragments;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {

    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            start(WalletFragment.newInstance());
            SupportHelper.logFragmentStackHierarchy((MainActivity) _mActivity,"ddd");
            // start(ResetPasswordConfirmFragment.newInstance(Constant.USAGE_RESET_PASSWORD, "0086", "13417785707", 30));
        } else if (id == R.id.btn_test2) {
            // start(ResetPasswordConfirmFragment.newInstance(Constant.USAGE_SET_PAYMENT_PASSWORD, "0086", "13417785708", 31));
            SLog.info("fragment[%s]", Util.getFragmentByLayer(_mActivity, 1).getClass().getName());
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
