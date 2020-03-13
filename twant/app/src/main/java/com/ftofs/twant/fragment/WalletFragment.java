package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 想付錢包Fragment
 * @author zwm
 */
public class WalletFragment extends BaseFragment implements View.OnClickListener {
    TextView btnActivateNow;
    TextView tvAccountBalance;

    public static WalletFragment newInstance() {
        Bundle args = new Bundle();
        
        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        btnActivateNow = view.findViewById(R.id.btn_activate_now);
        btnActivateNow.setOnClickListener(this);

        tvAccountBalance = view.findViewById(R.id.tv_account_balance);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_activate_now) {
            start(ResetPasswordFragment.newInstance(Constant.USAGE_SET_PAYMENT_PASSWORD, false));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        // 放在這里，如果激活返回，可以立即更新界面
        loadData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);
        SLog.info("params[%s]", params);

        Api.getUI(Api.PATH_WALLET_INFO, params, new UICallback() {
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

                    EasyJSONObject wantWallet = responseObj.getObject("datas.wantWallet");
                    if (Util.isJsonNull(wantWallet)) { // 如果為null，表示未激活
                        btnActivateNow.setVisibility(View.VISIBLE); // 未激活，顯示激活按鈕
                    } else {
                        btnActivateNow.setVisibility(View.GONE); // 已激活，隱藏激活按鈕

                        // 獲取余額
                        double balance = responseObj.getDouble("datas.memberInfo.predepositAvailable");
                        SLog.info("__balance[%s], __balance2[%s]", balance, StringUtil.formatFloat(balance));
                        tvAccountBalance.setText(StringUtil.formatFloat(balance));
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}
