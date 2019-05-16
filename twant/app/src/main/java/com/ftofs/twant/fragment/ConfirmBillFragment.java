package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
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

    List<ListPopupItem> payWayItemList = new ArrayList<>();
    List<ListPopupItem> shippingItemList = new ArrayList<>();

    private static final int LIST_POPUP_TYPE_PAY_WAY = 1;
    private static final int LIST_POPUP_TYPE_SHIPPING_TIME = 2;

    // 當前支付方式
    int payWayIndex = 0;
    // 當前配送時間索引
    int shippingTimeIndex = 0;

    TextView tvPayWay;
    TextView tvShippingTime;

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

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_change_pay_way, this);
        Util.setOnClickListener(view, R.id.btn_change_shipping_time, this);

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
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_pay_fetch),
                            LIST_POPUP_TYPE_SHIPPING_TIME, shippingItemList, shippingTimeIndex, this))
                    .show();
        }
    }

    private void loadBillData() {
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", buyData,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "isCart", 1);
        SLog.info("params[%s]", params.toString());
        Api.postUI(Api.PATH_BILL_DATA, params, new UICallback() {
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
}
