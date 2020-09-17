package com.ftofs.twant.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
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
    int areaId;
    int areaId1;
    int areaId2;
    int areaId3;
    int areaId4;

    String areaInfo;
    String mobileAreaCode;  // 手機區號

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

    EditText etMobile;
    EditText etAddress;
    
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
        Util.setOnClickListener(view, R.id.btn_save, this);

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

        etMobile = view.findViewById(R.id.et_mobile);
        etAddress = view.findViewById(R.id.et_address);

        tvMobileZone = view.findViewById(R.id.tv_mobile_zone);
        tvLocation = view.findViewById(R.id.tv_location);

        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
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
                            PopupType.MOBILE_ZONE, itemList, selectedMobileZoneIndex == -1 ? 0 : selectedMobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_select_location) {
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new AreaPopup(_mActivity, PopupType.AREA, this))
                    .show();
        } else if (id == R.id.btn_save) {
            Bundle bundle = new Bundle();

            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (StringUtil.isEmpty(name)) {
                if (dataType == DATA_TYPE_SENDER_INFO) {
                    ToastUtil.error(_mActivity, "請" + getString(R.string.input_sender_name_hint));
                } else {
                    ToastUtil.error(_mActivity, "請" + getString(R.string.input_receiver_name_hint));
                }
                return;
            }

            if (StringUtil.isEmpty(mobile)) {
                ToastUtil.error(_mActivity, "請" + getString(R.string.input_contact_hint));
                return;
            }

            if (StringUtil.isEmpty(areaInfo)) {
                ToastUtil.error(_mActivity, "請" + getString(R.string.select_location_hint));
                return;
            }

            if (StringUtil.isEmpty(address)) {
                ToastUtil.error(_mActivity, "請" + getString(R.string.input_detail_address_hint));
                return;
            }

            bundle.putString("name", name);
            bundle.putInt("areaId1", areaId1);
            bundle.putInt("areaId2", areaId2);
            bundle.putInt("areaId3", areaId3);
            bundle.putInt("areaId4", areaId4);
            bundle.putInt("areaId", areaId);
            bundle.putString("areaInfo", areaInfo);
            bundle.putString("address", address);
            bundle.putString("mobile", mobile);
            bundle.putString("mobileAreaCode", mobileAreaCode);

            SLog.info("bundle[%s]", bundle);
            setFragmentResult(RESULT_OK, bundle);
            hideSoftInputPop();
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
                    MobileZone mobileZone = mobileZoneList.get(0);
                    tvMobileZone.setText(mobileZone.areaName);
                    mobileAreaCode = mobileZone.areaCode;
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.MOBILE_ZONE) {
            if (this.selectedMobileZoneIndex == id) {
                return;
            }

            this.selectedMobileZoneIndex = id;
            MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);
            tvMobileZone.setText(mobileZone.areaName);
            mobileAreaCode = mobileZone.areaCode;
        } else if (type == PopupType.AREA) {
            SLog.info("extra[%s]", extra);

            if (extra == null) {
                return;
            }

            areaId = 0; areaId1 = 0; areaId2 = 0; areaId3 = 0; areaId4 = 0;
            areaInfo = "";

            List<Area> selectedAreaList = (List<Area>) extra;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < selectedAreaList.size(); i++) {
                Area area = selectedAreaList.get(i);
                if (i == 0) {
                    areaId1 = area.getAreaId();
                    areaId = areaId1;
                } else if (i == 1) {
                    areaId2 = area.getAreaId();
                    areaId = areaId2;
                } else if (i == 2) {
                    areaId3 = area.getAreaId();
                    areaId = areaId3;
                } else if (i == 3) {
                    areaId4 = area.getAreaId();
                    areaId = areaId4;
                }

                if (i != 0) {
                    sb.append(" ");
                }

                sb.append(area.getAreaName());
            }

            areaInfo = sb.toString();
            tvLocation.setText(areaInfo);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
