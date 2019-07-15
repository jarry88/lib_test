package com.ftofs.twant.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaPopup;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;


/**
 * 寄件人/收件人信息Fragment
 * @author zwm
 */
public class SenderInfoFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    /**
     * 標記是寄件人信息還是收件人信息
     */
    int dataType;
    
    public static final int DATA_TYPE_SENDER_INFO = 1;
    public static final int DATA_TYPE_RECEIVER_INFO = 2;

    int selectedMobileZoneIndex = -1;
    List<MobileZone> mobileZoneList = new ArrayList<>();

    TextView tvFragmentTitle;
    TextView etName;
    TextView tvMobileZone;
    TextView tvLocation;
    
    public static SenderInfoFragment newInstance(int dataType) {
        Bundle args = new Bundle();

        args.putInt("dataType", dataType);

        SenderInfoFragment fragment = new SenderInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sender_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        dataType = args.getInt("dataType");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_select_location, this);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);

        if (dataType == DATA_TYPE_SENDER_INFO) {
            tvFragmentTitle.setText(getString(R.string.text_sender_info));
        } else {
            tvFragmentTitle.setText(getString(R.string.text_receiver_info));
        }
        etName = view.findViewById(R.id.et_name);
        if (dataType == DATA_TYPE_SENDER_INFO) {
            etName.setHint(getString(R.string.input_sender_name_hint));
        } else {
            etName.setHint(getString(R.string.input_receiver_name_hint));
        }

        tvMobileZone = view.findViewById(R.id.tv_mobile_zone);
        tvLocation = view.findViewById(R.id.tv_location);

        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            Constant.POPUP_TYPE_MOBILE_ZONE, itemList, selectedMobileZoneIndex == -1 ? 0 : selectedMobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_select_location) {
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new AreaPopup(_mActivity, Constant.POPUP_TYPE_MEMBER_ADDRESS, this))
                    .show();
        }
    }

    /**
     * 获取区号列表
     */
    private void getMobileZoneList() {
        Api.getMobileZoneList(new TaskObserver() {
            @Override
            public void onMessage() {
                mobileZoneList = (List<MobileZone>) message;
                if (mobileZoneList == null) {
                    return;
                }
                SLog.info("mobileZoneList.size[%d]", mobileZoneList.size());
                if (mobileZoneList.size() > 0) {
                    tvMobileZone.setText(mobileZoneList.get(0).areaName);
                }
            }
        });
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        if (type == Constant.POPUP_TYPE_MOBILE_ZONE) {
            if (this.selectedMobileZoneIndex == id) {
                return;
            }

            this.selectedMobileZoneIndex = id;
            String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
            tvMobileZone.setText(areaName);
        } else if (type == Constant.POPUP_TYPE_MEMBER_ADDRESS) {
            SLog.info("extra[%s]", extra);
            String location = (String) extra;
            tvLocation.setText(location);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
