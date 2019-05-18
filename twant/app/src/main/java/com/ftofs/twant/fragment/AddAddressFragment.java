package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaPopup;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 地址添加Fragment
 * @author zwm
 */
public class AddAddressFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int mobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    List<Area> areaList = new ArrayList<>();
    TextView tvMobileZone;

    EditText etReceiverName;
    EditText etMobile;
    TextView tvArea;
    EditText etDetailAddress;


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
        etReceiverName = view.findViewById(R.id.et_receiver_name);
        etMobile = view.findViewById(R.id.et_mobile);
        tvArea = view.findViewById(R.id.tv_area);
        etDetailAddress = view.findViewById(R.id.et_detail_address);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_select_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_select_area, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        SLog.info("id[%d]", id);
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_select_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            Constant.POPUP_TYPE_MOBILE_ZONE, itemList, mobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_select_area) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new AreaPopup(_mActivity, Constant.POPUP_TYPE_AREA, this))
                    .show();
        } else if (id == R.id.btn_ok) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            // 收集信息
            try {
                String realName = etReceiverName.getText().toString().trim();
                if (realName.length() < 1) {
                    ToastUtil.show(_mActivity, getResources().getText(R.string.hint_input_receiver_name).toString());
                    return;
                }

                String mobile = etMobile.getText().toString().trim();
                if (mobile.length() < 1) {
                    ToastUtil.show(_mActivity, getResources().getText(R.string.input_mobile_hint).toString());
                    return;
                }

                if (areaList.size() < 1) {
                    ToastUtil.show(_mActivity, getResources().getText(R.string.hint_select_area).toString());
                    return;
                }

                String detailAddress = etDetailAddress.getText().toString().trim();
                if (detailAddress.length() < 1) {
                    ToastUtil.show(_mActivity, getResources().getText(R.string.hint_input_detail_address).toString());
                    return;
                }

                String fullMobile = mobileZoneList.get(mobileZoneIndex).areaCode + "" + mobile;
                EasyJSONObject params = EasyJSONObject.generate(
                        "token", token,
                        "realName", realName,
                        "address", detailAddress,
                        "mobPhone", fullMobile,
                        "isDefault", 1);

                int areaId = 0;
                int i = 1;
                for (Area area : areaList) {
                    areaId = area.getAreaId();
                    params.set("areaId" + i, areaId);
                    i++;
                }


                // 最后一層areaId
                params.set("areaId", areaId);
                params.set("areaInfo", tvArea.getText().toString());

                SLog.info("params[%s]", params.toString());

                Api.postUI(Api.PATH_ADD_ADDRESS, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        ToastUtil.show(_mActivity, "地址添加成功");
                        pop();
                    }
                });
            } catch (EasyJSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        if (type == Constant.POPUP_TYPE_MOBILE_ZONE) {
            tvMobileZone.setText(mobileZoneList.get(id).areaName);
            mobileZoneIndex = id;
        } else if (type == Constant.POPUP_TYPE_AREA) {
            areaList = (List<Area>) extra;
            String text = "";
            for (Area area : areaList) {
                SLog.info("areaName[%s]", area.getAreaName());
                text += area.getAreaName() + " ";
            }
            text = text.trim();

            tvArea.setText(text);
        }

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
