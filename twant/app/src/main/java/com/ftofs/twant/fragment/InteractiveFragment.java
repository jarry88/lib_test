package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.lxj.xpopup.XPopup;

/**
 * 互動Fragment
 * @author zwm
 */
public class InteractiveFragment extends BaseFragment implements View.OnClickListener {
    public static InteractiveFragment newInstance() {
        Bundle args = new Bundle();

        InteractiveFragment fragment = new InteractiveFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactive, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_my_like, this);
        Util.setOnClickListener(view, R.id.btn_my_comment, this);
        Util.setOnClickListener(view, R.id.btn_my_follow, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_my_like:
                Util.startFragment(MyLikeFragment.newInstance());
                break;
            case R.id.btn_my_comment:
                Util.startFragment(MyCommentFragment.newInstance());
                break;
            case R.id.btn_my_follow:
                Util.startFragment(MyFollowFragment.newInstance());
                break;
            case R.id.btn_menu:
                new XPopup.Builder(_mActivity)
                        .offsetX(-Util.dip2px(_mActivity, 11))
                        .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_HOME_AND_MY))
                        .show();
                break;
            default:
                break;
        }
        
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
