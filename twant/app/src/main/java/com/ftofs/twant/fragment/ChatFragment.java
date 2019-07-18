package com.ftofs.twant.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.SoftInputUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.KeyboardLayout;

/**
 * 聊天會話Fragment
 * @author zwm
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {
    KeyboardLayout keyboardLayout;
    TextView tvEmojiPanel;
    TextView tvToolPanel;
    LinearLayout llPanelContainer;
    long lastClickTime;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        keyboardLayout = view.findViewById(R.id.keyboard_layout);
        tvEmojiPanel = view.findViewById(R.id.tv_emoji_panel);
        tvEmojiPanel.setOnClickListener(this);
        tvToolPanel = view.findViewById(R.id.tv_tool_panel);
        llPanelContainer = view.findViewById(R.id.ll_panel_container);

        keyboardLayout.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                long now = System.currentTimeMillis();
                long diff = now - lastClickTime;
                SLog.info("isActive[%s], diff[%s], now[%s], lastClickTime[%s]", isActive, diff, now, lastClickTime);
                if (isActive && diff > 200) {
                    tvEmojiPanel.setVisibility(View.GONE);
                    tvToolPanel.setVisibility(View.GONE);
                    llPanelContainer.setVisibility(View.GONE);
                }
                if (isActive) {
                    SLog.info("keyboardHeight[%s]", keyboardHeight);
                    SoftInputUtil.setSoftInputHeight(keyboardHeight);
                }
            }
        });

        Util.setOnClickListener(view, R.id.btn_emoji, this);
        Util.setOnClickListener(view, R.id.btn_circle_add, this);
        Util.setOnClickListener(view, R.id.ll_keyboard_container, this);  // 因為Fragment設置了全局點擊空白的地方，就隱藏軟鍵盤，所以要攔截事件
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_emoji) {
            lastClickTime = System.currentTimeMillis();
            SLog.info("here");
            hideSoftInput();
            tvToolPanel.setVisibility(View.GONE);

            int keyboardHeight = SoftInputUtil.getSoftInputHeight();
            SLog.info("keyboardHeight[%s]", keyboardHeight);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvEmojiPanel.getLayoutParams();
            layoutParams.height = keyboardHeight;
            tvEmojiPanel.setLayoutParams(layoutParams);
            tvEmojiPanel.setVisibility(View.VISIBLE);
            llPanelContainer.setVisibility(View.VISIBLE);
        } else if (id == R.id.btn_circle_add) {
            lastClickTime = System.currentTimeMillis();
            SLog.info("here");
            hideSoftInput();
            tvEmojiPanel.setVisibility(View.GONE);

            int keyboardHeight = SoftInputUtil.getSoftInputHeight();
            SLog.info("keyboardHeight[%s]", keyboardHeight);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvToolPanel.getLayoutParams();
            layoutParams.height = keyboardHeight;
            tvToolPanel.setLayoutParams(layoutParams);
            tvToolPanel.setVisibility(View.VISIBLE);
            llPanelContainer.setVisibility(View.VISIBLE);
        } else if (id == R.id.ll_keyboard_container) {
            SLog.info("ll_keyboard_container");
        } else if (id == R.id.tv_emoji_panel) {
            SLog.info("%s", tvEmojiPanel.getHeight());
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
