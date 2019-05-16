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
 * 地址添加Fragment
 * @author zwm
 */
public class AddAddressFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int mobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    TextView tvMobileZone;

    public static AddAddressFragment newInstance() {
        Bundle args = new Bundle();

        AddAddressFragment fragment = new AddAddressFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMobileZone = view.findViewById(R.id.tv_mobile_zone);

        Util.setOnClickListener(view, R.id.btn_select_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_select_area, this);

        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        SLog.info("id[%d]", id);
        if (id == R.id.btn_select_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            ListPopup.TYPE_DEFAULT, itemList, mobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_select_area) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new AreaPopup(_mActivity, null))
                    .show();
        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        tvMobileZone.setText(mobileZoneList.get(id).areaName);
    }

    /**
     * 获取区号列表
     */
    private void getMobileZoneList() {
        Api.getMobileZoneList(new TaskObserver() {
            @Override
            public void onMessage() {
                List<MobileZone> result = (List<MobileZone>) message;
                if (result == null || result.size() == 0) {
                    return;
                }
                mobileZoneList = result;
                SLog.info("mobileZoneList.size[%d]", mobileZoneList.size());
                if (mobileZoneList.size() > 0) {
                    tvMobileZone.setText(mobileZoneList.get(0).areaName);
                }
            }
        });
    }
}
