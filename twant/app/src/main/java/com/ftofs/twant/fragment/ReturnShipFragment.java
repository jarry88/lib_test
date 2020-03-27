package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.ShipItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 退貨發貨Fragment
 * @author zwm
 */
public class ReturnShipFragment extends BaseFragment implements View.OnClickListener {
    int refundId;
    int shipId = 0;

    EditText etNum;

    List<ShipItem> shipItemList = new ArrayList<>();

    public static ReturnShipFragment newInstance(int refundId) {
        Bundle args = new Bundle();

        ReturnShipFragment fragment = new ReturnShipFragment();
        fragment.setArguments(args);
        fragment.setRefundId(refundId);

        return fragment;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return_ship, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNum = view.findViewById(R.id.et_num);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "refundId", refundId
        );

        Api.postUI(Api.PATH_RETURN_SHIP, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try{
                    SLog.info("responseStr[%s]",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray shipList = responseObj.getArray("datas.shipList");
                    for (Object object : shipList) {
                        EasyJSONObject shipObj = (EasyJSONObject) object;

                        ShipItem shipItem = new ShipItem();
                        shipItem.shipId = shipObj.getInt("shipId");
                        shipItem.shipName = shipObj.getSafeString("shipName");

                        shipItemList.add(shipItem);
                    }
                } catch (Exception e){
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void saveData() {
        String token = User.getToken();

        /*
        token String 用户登录token
refundId int 订单号
shipId int 快递公司id
shipSn string 快递单号
         */

        if (shipId == 0) {
            ToastUtil.error(_mActivity, "請選擇快遞公司");
            return;
        }

        String shipSn = etNum.getText().toString().trim();
        if (StringUtil.isEmpty(shipSn)) {
            ToastUtil.error(_mActivity, "請輸入物流單號");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "refundId", refundId,
                "shipId", shipId,
                "shipSn", shipSn
        );

        Api.postUI(Api.PATH_RETURN_SHIP_SAVE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try{
                    SLog.info("responseStr[%s]",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, "保存成功");
                    pop();
                } catch (Exception e){
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_ok) {

        }
    }
}

