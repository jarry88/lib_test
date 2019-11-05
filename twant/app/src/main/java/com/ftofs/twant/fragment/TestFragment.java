package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.BadgeUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.util.Vendor;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    LinearLayout llMenu;
    TextView tvContent;
    int screenWidth;

    int menuShrunkWidth;
    int menuExpandedWidth;
    int contentWidth;

    boolean isShrunk = true;
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

        llMenu = view.findViewById(R.id.ll_menu);
        tvContent = view.findViewById(R.id.tv_content);

        Pair<Integer, Integer> dim = Util.getScreenDimemsion(_mActivity);
        screenWidth = dim.first;

        menuShrunkWidth = Util.dip2px(_mActivity, 100);
        menuExpandedWidth = screenWidth * 2 / 3;
        contentWidth = screenWidth - menuShrunkWidth;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvContent.getLayoutParams();
        layoutParams.width = contentWidth;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            // new BadgeUtil().setHuaweiBadgeNum(_mActivity, 12);
            BadgeUtil.setBadgeNum(_mActivity, 22);
            SLog.info("prop[%s]", "prop");
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
