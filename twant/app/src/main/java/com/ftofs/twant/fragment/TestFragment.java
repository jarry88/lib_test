package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.EasySwipeMenuState;
import com.ftofs.twant.domain.goods.ArrivalNotice;
import com.ftofs.twant.entity.ButtonClickInfo;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ImageProcess;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;
import com.ftofs.twant.widget.EasySwipeMenuLayout;
import com.ftofs.twant.widget.QuickClickButton;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.ftofs.twant.widget.TwProgressBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    Map<Integer, ButtonClickInfo> buttonClickInfoMap = new HashMap<>();

    HorizontalScrollView hsv;
    int i = 0;
    TwProgressBar progressBar;
    ImageView imageView;

    EasySwipeMenuLayout swipeMenuLayout;

    boolean open;
    TextView textView;

    Animation animation;

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

        textView = view.findViewById(R.id.text_view);
        Util.setOnClickListener(view, R.id.btn_test, this);
        buttonClickInfoMap.put(R.id.btn_test, new ButtonClickInfo());

        progressBar = view.findViewById(R.id.tw_progress_bar);
        progressBar.setColor(TwProgressBar.COLOR_ORANGE);

        imageView = view.findViewById(R.id.image_view);

        hsv = view.findViewById(R.id.hsv);
        swipeMenuLayout = view.findViewById(R.id.swipe_menu_layout);

        animation = AnimationUtils.loadAnimation(_mActivity,R.anim.welcome_message);
        //动画监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            //动画开始的时候触发
            @Override
            public void onAnimationStart(Animation animation) {

            }
            //动画结束的时候触发
            @Override
            public void onAnimationEnd(Animation animation) {
                SLog.info("onAnimationEnd");
            }
            //动画重新执行的时候触发
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            start(PayVendorFragment.newInstance());
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
