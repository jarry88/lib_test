package com.ftofs.twant.seller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.fragment.AddAddressFragment;
import com.ftofs.twant.fragment.BaseFragment;
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

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerEditReceiverInfoFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
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
    private int DETAIL_ADDRESS_MAXLENTH=80;
    private int REAL_NAME_MAXLENTH=50;

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
        View view = inflater.inflate(R.layout.fragment_seller_edit_receiver_info, container, false);
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
        etReceiverName.setOnClickListener((v)-> {
            ((TextView) view.findViewById(R.id.tv_receiver_name)).setTextColor(Color.GRAY);
        });
        etReceiverName.setOnFocusChangeListener((v,b)->{
            if (b) {
                ((TextView) view.findViewById(R.id.tv_receiver_name)).setTextColor(Color.BLACK);
            }
        });
        etReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= REAL_NAME_MAXLENTH) {
                    ToastUtil.success(_mActivity,String.format("姓名長度不能超過%d字",REAL_NAME_MAXLENTH));
                }

            }
        });
        etReceiverName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(REAL_NAME_MAXLENTH)});
        etReceiverName.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etReceiverName.setGravity(Gravity.TOP);
        //改变默认的单行模式
        etReceiverName.setSingleLine(false);
        //水平滚动设置为False
        etReceiverName.setHorizontallyScrolling(false);

        etMobile = view.findViewById(R.id.et_mobile);
        etMobile.setOnClickListener((v)-> {
            ((TextView) view.findViewById(R.id.tv_phone_number)).setTextColor(Color.GRAY);
        });
        etMobile.setOnFocusChangeListener((v,b)->{
            if (b) {
                ((TextView) view.findViewById(R.id.tv_phone_number)).setTextColor(Color.BLACK);
            }
        });
        tvArea = view.findViewById(R.id.tv_area);
        etDetailAddress = view.findViewById(R.id.et_detail_address);
        etDetailAddress.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DETAIL_ADDRESS_MAXLENTH)});
        etDetailAddress.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        etDetailAddress.setGravity(Gravity.TOP);
        //改变默认的单行模式
        etDetailAddress.setSingleLine(false);
        //水平滚动设置为False
        etDetailAddress.setHorizontallyScrolling(false);
        etDetailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() >= DETAIL_ADDRESS_MAXLENTH) {
                    ToastUtil.success(_mActivity,String.format("地址長度不能超過%d字",DETAIL_ADDRESS_MAXLENTH));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= DETAIL_ADDRESS_MAXLENTH) {
                    ToastUtil.success(_mActivity,String.format("地址長度不能超過%d字",DETAIL_ADDRESS_MAXLENTH));
                }
            }
        });
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
                mSbDefaultAddr.setIconResource(R.drawable.icon_checked);
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
        Util.setOnClickListener(view, R.id.btn_clear_name, this);

        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        SLog.info("id[%d]", id);
        if (id == R.id.btn_back) {
            hideSoftInputPop();
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
                            PopupType.MOBILE_ZONE, itemList, mobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_select_area) {
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new AreaPopup(_mActivity, PopupType.AREA, this))
                    .show();
        } else if (id == R.id.sb_default_addr) {
            if (mIsDefaultAddr == 1) {
                mSbDefaultAddr.setIconResource(R.drawable.icon_cart_item_unchecked);
            } else {
                mSbDefaultAddr.setIconResource(R.drawable.icon_checked);
            }
            mIsDefaultAddr = 1 - mIsDefaultAddr;
            SLog.info("mIsDefaultAddr[%d]", mIsDefaultAddr);
        } else if (id == R.id.btn_clear_detail_address) {
            etDetailAddress.setText("");
        } else if (id == R.id.btn_clear_name) {
            etReceiverName.setText("");
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
                ToastUtil.error(_mActivity, getResources().getText(R.string.hint_input_receiver_name).toString());
                return;
            } else if (realName.length() > REAL_NAME_MAXLENTH){
                ToastUtil.error(_mActivity,"姓名長度超過上限");
                return;
            }

            final String mobile = etMobile.getText().toString().trim();
            if (mobile.length() < 1) {
                ToastUtil.error(_mActivity, getResources().getText(R.string.input_mobile_hint).toString());
                return;
            }

            if (areaList.size() < 1) {
                ToastUtil.error(_mActivity, getResources().getText(R.string.hint_select_area).toString());
                return;
            }

            MobileZone mobileZone = mobileZoneList.get(mobileZoneIndex);
            if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
                String[] areaArray = new String[] {
                        "",
                        getString(R.string.text_hongkong),
                        getString(R.string.text_mainland),
                        getString(R.string.text_macao)
                };

                String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
                ToastUtil.error(_mActivity, msg);
                return;
            }

            final String detailAddress = etDetailAddress.getText().toString().trim();
            if (detailAddress.length() < 1) {
                ToastUtil.error(_mActivity, getResources().getText(R.string.hint_input_detail_address).toString());
                return;
            } else if (detailAddress.length() > DETAIL_ADDRESS_MAXLENTH){
                ToastUtil.error(_mActivity,"詳細地址長度超過上限");
                return;
            }

            String fullMobile = mobileZone.areaCode + "" + mobile;
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
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

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

                            ToastUtil.success(_mActivity, "地址添加成功");
                        } else {
                            ToastUtil.success(_mActivity, "地址編輯成功");
                        }

                        hideSoftInputPop();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.MOBILE_ZONE) {
            tvMobileZone.setText(mobileZoneList.get(id).areaName);
            mobileZoneIndex = id;
        } else if (type == PopupType.AREA) {
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
        hideSoftInputPop();
        return true;
    }
}
