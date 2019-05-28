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
import com.ftofs.twant.entity.AddrItem;
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
import com.ftofs.twant.widget.ScaledButton;
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
 * 注意：地址編輯也是共用這個Fragment
 * @author zwm
 */
public class AddAddressFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int mobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    List<Area> areaList = new ArrayList<>();
    TextView tvMobileZone;
    AddrItem addrItem;

    EditText etReceiverName;
    EditText etMobile;
    TextView tvArea;
    EditText etDetailAddress;
    ScaledButton mSbDefaultAddr;
    int mIsDefaultAddr;

    int action;  // 標記是編輯還是添加

    public static AddAddressFragment newInstance(int action, AddrItem addrItem) {
        Bundle args = new Bundle();

        args.putInt("action", action);
        if (addrItem != null) {
            args.putParcelable("addrItem", addrItem);
        }
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

        Bundle bundle = getArguments();
        action = bundle.getInt("action");
        if (action == Constant.ACTION_EDIT) {
            addrItem = bundle.getParcelable("addrItem");

            mIsDefaultAddr = addrItem.isDefault;
        }

        tvMobileZone = view.findViewById(R.id.tv_mobile_zone);
        etReceiverName = view.findViewById(R.id.et_receiver_name);
        etMobile = view.findViewById(R.id.et_mobile);
        tvArea = view.findViewById(R.id.tv_area);
        etDetailAddress = view.findViewById(R.id.et_detail_address);
        mSbDefaultAddr = view.findViewById(R.id.sb_default_addr);
        mSbDefaultAddr.setOnClickListener(this);

        TextView tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (action == Constant.ACTION_EDIT) {
            // 地址編輯
            tvFragmentTitle.setText(R.string.text_edit_address);

            etReceiverName.setText(addrItem.realName);
            etMobile.setText(addrItem.mobPhone);
            tvArea.setText(addrItem.areaInfo);
            etDetailAddress.setText(addrItem.address);
            for (int areaId : addrItem.areaIdList) {
                Area area = new Area();
                area.setAreaId(areaId);
                areaList.add(area);
            }

            if (mIsDefaultAddr == 1) {
                mSbDefaultAddr.setIconResource(R.drawable.icon_cart_item_checked);
            }
        } else {
            // 地址添加
            tvFragmentTitle.setText(R.string.text_add_address);
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_select_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_select_area, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_clear_detail_address, this);

        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        SLog.info("id[%d]", id);
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_select_mobile_zone) {
            hideSoftInput();
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
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new AreaPopup(_mActivity, Constant.POPUP_TYPE_AREA, this))
                    .show();
        } else if (id == R.id.sb_default_addr) {
            if (mIsDefaultAddr == 1) {
                mSbDefaultAddr.setIconResource(R.drawable.icon_cart_item_unchecked);
            } else {
                mSbDefaultAddr.setIconResource(R.drawable.icon_cart_item_checked);
            }
            mIsDefaultAddr = 1 - mIsDefaultAddr;
            SLog.info("mIsDefaultAddr[%d]", mIsDefaultAddr);
        } else if (id == R.id.btn_clear_detail_address) {
            etDetailAddress.setText("");
        } else if (id == R.id.btn_ok) {
            saveAddress();
        }
    }

    private void saveAddress() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        // 收集信息
        try {
            final String realName = etReceiverName.getText().toString().trim();
            if (realName.length() < 1) {
                ToastUtil.show(_mActivity, getResources().getText(R.string.hint_input_receiver_name).toString());
                return;
            }

            final String mobile = etMobile.getText().toString().trim();
            if (mobile.length() < 1) {
                ToastUtil.show(_mActivity, getResources().getText(R.string.input_mobile_hint).toString());
                return;
            }

            if (areaList.size() < 1) {
                ToastUtil.show(_mActivity, getResources().getText(R.string.hint_select_area).toString());
                return;
            }

            final String detailAddress = etDetailAddress.getText().toString().trim();
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
                    "isDefault", mIsDefaultAddr);

            int areaId = 0;
            int i = 1;
            for (Area area : areaList) {
                areaId = area.getAreaId();
                params.set("areaId" + i, areaId);
                i++;
            }


            // 最后一層areaId
            params.set("areaId", areaId);
            final int finalAreaId = areaId;
            final String areaInfo = tvArea.getText().toString();
            params.set("areaInfo", areaInfo);

            String path;
            if (action == Constant.ACTION_EDIT) {
                params.set("addressId", addrItem.addressId);
                path = Api.PATH_EDIT_ADDRESS;
            } else {
                path = Api.PATH_ADD_ADDRESS;
            }

            SLog.info("params[%s]", params.toString());
            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        if (action == Constant.ACTION_ADD) {
                            Bundle bundle = new Bundle();
                            bundle.putString("from", AddAddressFragment.class.getName());
                            int addressId = responseObj.getInt("datas.addressId");
                            AddrItem addrItem = new AddrItem(addressId, realName, null, finalAreaId, areaInfo,
                                    detailAddress, "", mobile, 0);
                            SLog.info("addrItem: %s", addrItem);
                            bundle.putParcelable("addrItem", addrItem);
                            setFragmentResult(RESULT_OK, bundle);

                            ToastUtil.show(_mActivity, "地址添加成功");
                        } else {
                            ToastUtil.show(_mActivity, "地址編輯成功");
                        }

                        pop();
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (EasyJSONException e) {
            e.printStackTrace();
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
                if (action == Constant.ACTION_ADD) {
                    // 如果是添加地址，默認顯示第1個區號
                    if (mobileZoneList.size() > 0) {
                        tvMobileZone.setText(mobileZoneList.get(0).areaName);
                    }
                } else {
                    // 編輯地址，則顯示傳進來的區號
                    SLog.info("mobileAreaCode[%s]", addrItem.mobileAreaCode);
                    int index = 0;
                    for (MobileZone mobileZone : mobileZoneList) {
                        if (mobileZone.areaCode.equals(addrItem.mobileAreaCode)) {
                            tvMobileZone.setText(mobileZone.areaName);
                            mobileZoneIndex = index;
                            break;
                        }
                        ++index;
                    }
                }

            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
