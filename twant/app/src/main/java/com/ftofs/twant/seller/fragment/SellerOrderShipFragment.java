package com.ftofs.twant.seller.fragment;


import android.os.Bundle;
import android.util.Log;
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
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.ShipCompany;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.fragment.BaseFragment;
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
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 【商家】訂單發貨
 * @author zwm
 */
public class SellerOrderShipFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int orderId;
    String orderSn;
    List<ShipCompany> shipCompanyList = new ArrayList<>();

    public static final int SHIP_WAY_SEND_WANT = 1;
    public static final int SHIP_WAY_THIRD_PARTY = 2;
    public static final int SHIP_WAY_NO = 3;

    // 物流方式
    int shipWay = SHIP_WAY_SEND_WANT;
    // 快遞公司Id
    int shipId = 0;

    int receiverAreaId1;
    int receiverAreaId2;
    int receiverAreaId3;
    int receiverAreaId4;
    String receiverAreaInfo;
    String receiverName;
    String receiverPhone;
    String receiverAddress;

    EditText etPackageCount;
    EditText etPackageWeight;
    EditText etPackageLength;
    EditText etPackageWidth;
    EditText etPackageHeight;

    EditText etPackageDesc;
    EditText etShipRemark;
    EditText etShipDesc;

    public static SellerOrderShipFragment newInstance(int orderId, String orderSn) {
        Bundle args = new Bundle();

        SellerOrderShipFragment fragment = new SellerOrderShipFragment();
        fragment.setArguments(args);

        fragment.orderId = orderId;
        fragment.orderSn = orderSn;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order_ship, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPackageCount = view.findViewById(R.id.et_package_count);
        etPackageWeight = view.findViewById(R.id.et_package_weight);
        etPackageLength = view.findViewById(R.id.et_package_length);
        etPackageWidth = view.findViewById(R.id.et_package_width);
        etPackageHeight = view.findViewById(R.id.et_package_height);

        etPackageDesc = view.findViewById(R.id.et_package_desc);
        etShipRemark = view.findViewById(R.id.et_ship_remark);
        etShipDesc = view.findViewById(R.id.et_ship_desc);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);

        Util.setOnClickListener(view, R.id.btn_select_logistics_way, this);
        Util.setOnClickListener(view, R.id.btn_select_logistics_company, this);

        ((TextView) view.findViewById(R.id.tv_order_sn)).setText(orderSn);

        loadLogisticsCompany();
    }

    private void loadLogisticsCompany() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        String url = Api.PATH_SELLER_LOGISTICS_LIST + "/" + orderId;
        Api.getUI(url, params, new UICallback() {
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

                    EasyJSONArray shipCompanyArr = responseObj.getArray("datas.shipCompanyList");
                    for (Object object : shipCompanyArr) {
                        EasyJSONObject item = (EasyJSONObject) object;
                        ShipCompany shipCompany = (ShipCompany) EasyJSONBase.jsonDecode(ShipCompany.class, item.toString());

                        shipCompanyList.add(shipCompany);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void commitOrderShip() {
        try {
            String packageCountStr = etPackageCount.getText().toString().trim();
            if (StringUtil.isEmpty(packageCountStr)) {
                ToastUtil.error(_mActivity, "請輸入包裹數量");
                return;
            }
            int packageCount = Integer.parseInt(packageCountStr);


            String packageWeightStr = etPackageWeight.getText().toString().trim();
            if (StringUtil.isEmpty(packageWeightStr)) {
                ToastUtil.error(_mActivity, "請輸入包裹重量");
                return;
            }
            double packageWeight = Double.parseDouble(packageWeightStr);


            String packageLengthStr = etPackageLength.getText().toString().trim();
            if (StringUtil.isEmpty(packageLengthStr)) {
                ToastUtil.error(_mActivity, "請輸入包裹長度");
                return;
            }
            double packageLength = Double.parseDouble(packageLengthStr);


            String packageWidthStr = etPackageLength.getText().toString().trim();
            if (StringUtil.isEmpty(packageWidthStr)) {
                ToastUtil.error(_mActivity, "請輸入包裹寬度");
                return;
            }
            double packageWidth = Double.parseDouble(packageWidthStr);


            String packageHeightStr = etPackageHeight.getText().toString().trim();
            if (StringUtil.isEmpty(packageHeightStr)) {
                ToastUtil.error(_mActivity, "請輸入包裹高度");
                return;
            }
            double packageHeight = Double.parseDouble(packageHeightStr);


            EasyJSONObject params = EasyJSONObject.generate(
                    "areaId1", receiverAreaId1,
                    "areaId2", receiverAreaId2,
                    "areaId3", receiverAreaId3,
                    "areaId4", receiverAreaId4,
                    "areaInfo", receiverAreaInfo,
                    "ordersId", orderId,
                    "receiverName", receiverName,
                    "receiverPhone", receiverPhone,
                    "receiverAddress", receiverAddress,
                    "shipNote", etShipRemark.getText().toString().trim(),
                    "cargoName", etPackageDesc.getText().toString().trim(),
                    "explain", etShipDesc.getText().toString().trim(),
                    "cargoVo", EasyJSONObject.generate(
                            "quantity", packageCount,
                            "weight", packageWeight,
                            "height", packageHeight,
                            "width", packageWidth,
                            "cargoLong", packageLength
                    )
            );

            String json = params.toString();
            SLog.info("json[%s]", json);
            Api.postJsonUi(Api.PATH_SELLER_ORDER_SHIP, json, new UICallback() {
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

                        ToastUtil.success(_mActivity, "訂單發貨成功");
                        pop();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            ToastUtil.error(_mActivity, "請輸入合規的數據");
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_commit) {
            commitOrderShip();
        } else if (id == R.id.btn_select_logistics_way) {
            hideSoftInput();

            List<ListPopupItem> itemList = new ArrayList<>();
            itemList.add(new ListPopupItem(SHIP_WAY_SEND_WANT, "想送物流(自營物流)", null));
            itemList.add(new ListPopupItem(SHIP_WAY_SEND_WANT, "第三方物流", null));
            itemList.add(new ListPopupItem(SHIP_WAY_SEND_WANT, "不使用物流", null));
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, "選擇物流方式",
                            PopupType.SELECT_SELLER_LOGISTICS_WAY, itemList, 0, this))
                    .show();
        } else if (id == R.id.btn_select_logistics_company) {

        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        
    }
}


