package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 確認訂單Fragment
 * @author zwm
 */
public class ConfirmBillFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    String buyData;

    /**
     * 是否來源于購物車
     * todo 暫時寫死
     */
    boolean isFromCart = true;

    List<ListPopupItem> payWayItemList = new ArrayList<>();
    List<ListPopupItem> shippingItemList = new ArrayList<>();

    private static final int LIST_POPUP_TYPE_PAY_WAY = 1;
    private static final int LIST_POPUP_TYPE_SHIPPING_TIME = 2;

    // 當前支付方式
    int payWayIndex = 0;
    // 當前配送時間索引
    int shippingTimeIndex = 0;

    int addressId = 0;
    int isExistTrys;

    TextView tvPayWay;
    TextView tvShippingTime;
    TextView tvReceiverName;
    TextView tvMobile;
    TextView tvAddress;

    RelativeLayout btnAddShippingAddress;
    LinearLayout btnChangeShippingAddress;

    /**
     * 提交訂單時上去的店鋪列表
     */
    EasyJSONArray commitStoreList = EasyJSONArray.generate();

    public static ConfirmBillFragment newInstance(String buyData) {
        Bundle args = new Bundle();

        args.putString("buyData", buyData);

        ConfirmBillFragment fragment = new ConfirmBillFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_bill, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        buyData = args.getString("buyData");
        SLog.info("buyData[%s]", buyData);

        // 初始化支付方式數據
        payWayItemList.add(new ListPopupItem(0, getResources().getString(R.string.text_pay_online), null));
        payWayItemList.add(new ListPopupItem(1, getResources().getString(R.string.text_pay_delivery), null));
        payWayItemList.add(new ListPopupItem(2, getResources().getString(R.string.text_pay_fetch), null));

        tvPayWay = view.findViewById(R.id.tv_pay_way);
        tvShippingTime = view.findViewById(R.id.tv_shipping_time);

        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        tvMobile = view.findViewById(R.id.tv_mobile);
        tvAddress = view.findViewById(R.id.tv_address);

        btnAddShippingAddress = view.findViewById(R.id.btn_add_shipping_address);
        btnChangeShippingAddress = view.findViewById(R.id.btn_change_shipping_address);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_change_pay_way, this);
        Util.setOnClickListener(view, R.id.btn_change_shipping_time, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
        Util.setOnClickListener(view, R.id.btn_add_shipping_address, this);
        Util.setOnClickListener(view, R.id.btn_change_shipping_address, this);

        loadBillData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_change_pay_way) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_pay_fetch),
                            LIST_POPUP_TYPE_PAY_WAY, payWayItemList, payWayIndex, this))
                    .show();
        } else if (id == R.id.btn_change_shipping_time) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_shipping_time),
                            LIST_POPUP_TYPE_SHIPPING_TIME, shippingItemList, shippingTimeIndex, this))
                    .show();
        } else if (id == R.id.btn_commit) {
            if (addressId == 0) {
                SLog.info("Error!地址信息無效");
                ToastUtil.show(_mActivity, "地址信息無效");
                return;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }


            // 收集表單信息
            EasyJSONObject commitBuyData = EasyJSONObject.generate(
                    "addressId", addressId,
                    "paymentTypeCode", "online",
                    "isCart", 1,
                    "isExistTrys", isExistTrys,
                    "storeList", commitStoreList
            );

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "clientType", Constant.CLIENT_TYPE_ANDROID,
                    "buyData", commitBuyData.toString()
            );

            SLog.info("params[%s]", params.toString());
            Api.postUI(Api.PATH_COMMIT_BILL_DATA, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(getContext(), responseObj)) {
                        return;
                    }
                    ToastUtil.show(_mActivity, "提交訂單成功");
                    pop();
                }
            });
        } else if (id == R.id.btn_add_shipping_address) {
            startForResult(AddAddressFragment.newInstance(), Constant.REQUEST_CODE_ADD_ADDRESS);
        } else if (id == R.id.btn_change_shipping_address) {
            startForResult(AddrManageFragment.newInstance(), Constant.REQUEST_CODE_CHANGE_ADDRESS);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        SLog.info("requestCode[%d], resultCode[%d]", requestCode, resultCode);
    }

    private void loadBillData() {
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", buyData,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "isCart", 1);
        SLog.info("params[%s]", params.toString());
        Api.postUI(Api.PATH_DISPLAY_BILL_DATA, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(getContext(), responseObj)) {
                    return;
                }

                // 獲取配送時間列表
                try {
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.shipTimeTypeList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        shippingItemList.add(new ListPopupItem(easyJSONObject.getInt("id"), easyJSONObject.getString("name"), null));
                    }

                    EasyJSONObject address = responseObj.getObject("datas.address");
                    if (address != null) {
                        btnAddShippingAddress.setVisibility(View.GONE);
                        btnChangeShippingAddress.setVisibility(View.VISIBLE);
                        addressId = address.getInt("addressId");

                        tvReceiverName.setText(getResources().getString(R.string.text_receiver) + ": " + address.getString("realName"));
                        tvMobile.setText(address.getString("mobPhone"));
                        tvAddress.setText(address.getString("areaInfo") + " " + address.getString("address"));
                    } else {
                        // 用戶沒有收貨地址，顯示【新增收貨地址】按鈕
                        btnAddShippingAddress.setVisibility(View.VISIBLE);
                        btnChangeShippingAddress.setVisibility(View.GONE);
                    }

                    isExistTrys = responseObj.getInt("datas.isExistTrys");

                    easyJSONArray = responseObj.getArray("datas.buyStoreVoList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        int storeId = easyJSONObject.getInt("storeId");
                        String storeName = easyJSONObject.getString("storeName");
                        int shipTimeType = 0;

                        EasyJSONArray goodsList = EasyJSONArray.generate();
                        EasyJSONArray buyGoodsItemVoList = easyJSONObject.getArray("buyGoodsItemVoList");
                        for (Object object2 : buyGoodsItemVoList) {
                            EasyJSONObject buyGoodsItem = (EasyJSONObject) object2;
                            int cartId = buyGoodsItem.getInt("cartId");
                            int buyNum = buyGoodsItem.getInt("buyNum");
                            goodsList.append(EasyJSONObject.generate("cartId", cartId, "buyNum", buyNum));
                        }

                        commitStoreList.append(EasyJSONObject.generate(
                                "storeId", storeId,
                                "storeName", storeName,
                                "goodsList", goodsList,
                                "shipTimeType", shipTimeType));
                    }

                    SLog.info("commitStoreList[%s]", commitStoreList.toString());
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        if (type == LIST_POPUP_TYPE_PAY_WAY) {
            payWayIndex = id;
            tvPayWay.setText(payWayItemList.get(payWayIndex).title);
        } else if (type == LIST_POPUP_TYPE_SHIPPING_TIME) {
            shippingTimeIndex = id;
            tvShippingTime.setText(shippingItemList.get(shippingTimeIndex).title);
        }
    }

    /*
    {"code":200,"datas":{"address":null,
     */
}
