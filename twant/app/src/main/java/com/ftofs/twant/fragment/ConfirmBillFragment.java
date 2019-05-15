package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 確認訂單Fragment
 * @author zwm
 */
public class ConfirmBillFragment extends BaseFragment implements View.OnClickListener {
    String buyData;

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

        Util.setOnClickListener(view, R.id.btn_back, this);


        loadBillData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    private void loadBillData() {
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", buyData,
                "clientType", Constant.CLIENT_TYPE_ANDROID);
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


            }
        });
    }
}