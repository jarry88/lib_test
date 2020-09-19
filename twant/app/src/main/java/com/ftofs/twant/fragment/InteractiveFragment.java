package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

/**
 * 互動Fragment
 * @author zwm
 */
public class InteractiveFragment extends BaseFragment implements View.OnClickListener {
    String memberName;

    public static InteractiveFragment newInstance() {
        Bundle args = new Bundle();

        InteractiveFragment fragment = new InteractiveFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static InteractiveFragment newInstance(String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
        InteractiveFragment fragment = new InteractiveFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactive, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        replaceWord(view);
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
                hideSoftInputPop();
                break;
            case R.id.btn_my_like:
                Util.startFragment(MyLikeFragment.newInstance(memberName));
                break;
            case R.id.btn_my_comment:
                Util.startFragment(MyCommentFragment.newInstance(memberName));
                break;
            case R.id.btn_my_follow:
                Util.startFragment(MyFollowFragment.newInstance(memberName));
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
        hideSoftInputPop();
        return true;
    }
    private void replaceWord(View v){
        memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME,null);
        if(getArguments().containsKey("memberName")) {
            if (!memberName.equals(getArguments().getString("memberName"))) {
                memberName = getArguments().getString("memberName");
                ((TextView) v.findViewById(R.id.tv_my_like)).setText(getString(R.string.text_him_like));
                ((TextView) v.findViewById(R.id.tv_like_me)).setText(getString(R.string.text_like_me));
                ((TextView) v.findViewById(R.id.tv_my_comment)).setText(getString(R.string.text_him_comment));
                ((TextView) v.findViewById(R.id.tv_comment_me)).setText(getString(R.string.text_comment_me));
                ((TextView) v.findViewById(R.id.tv_my_follow)).setText(getString(R.string.text_him_follow));
                ((TextView) v.findViewById(R.id.tv_my_fans)).setText(getString(R.string.text_his_fans));
            }
        }
    }
}
