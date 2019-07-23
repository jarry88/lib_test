package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenuMessage;
import com.ftofs.twant.widget.ScaledButton;
import com.lxj.xpopup.XPopup;


/**
 * 消息
 * @author zwm
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;
    ScaledButton btnBack;

    public static MessageFragment newInstance(boolean isStandalone) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");

        Util.setOnClickListener(view, R.id.btn_message_menu, this);
        Util.setOnClickListener(view, R.id.btn_view_logistics_message, this);
        Util.setOnClickListener(view, R.id.btn_view_refund_message, this);

        btnBack = view.findViewById(R.id.btn_back);

        if (isStandalone) {
            btnBack.setOnClickListener(this);
            btnBack.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainFragment mainFragment = MainFragment.getInstance();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_message_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 6))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenuMessage(_mActivity, this))
                    .show();
        } else if (id == R.id.btn_view_logistics_message) {
            mainFragment.start(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_LOGISTICS));
        } else if (id == R.id.btn_view_refund_message) {
            mainFragment.start(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_REFUND));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (isStandalone) {
            pop();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SLog.info("onActivityResult, requestCode[%d]", requestCode);

        /*
         * 处理二维码扫描结果
         */
        if (requestCode == RequestCode.SCAN_QR_CODE.ordinal()) {
            Util.handleQRCodeResult(_mActivity, data);
        }
    }
}
