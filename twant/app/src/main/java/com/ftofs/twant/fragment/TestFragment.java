package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.domain.goods.ArrivalNotice;
import com.ftofs.twant.entity.ButtonClickInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ImageProcess;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;
import com.ftofs.twant.widget.QuickClickButton;
import com.ftofs.twant.widget.TwProgressBar;

import java.io.File;
import java.io.IOException;
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

    int i = 0;
    TwProgressBar progressBar;
    ImageView imageView;
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
        buttonClickInfoMap.put(R.id.btn_test, new ButtonClickInfo());

        progressBar = view.findViewById(R.id.tw_progress_bar);
        progressBar.setColor(TwProgressBar.COLOR_ORANGE);

        imageView = view.findViewById(R.id.image_view);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            ButtonClickInfo buttonClickInfo = buttonClickInfoMap.get(id);
            if (!buttonClickInfo.getCanClick()) {
                SLog.info("不能點擊");
                return;
            }

            buttonClickInfo.canClick = false;
            buttonClickInfo.lastClickTime = System.currentTimeMillis();
            SLog.info("yesyes");

            // SLog.info("contains[%s]", Util.needLoginFragmentName.contains("AddPostFragment"));
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
