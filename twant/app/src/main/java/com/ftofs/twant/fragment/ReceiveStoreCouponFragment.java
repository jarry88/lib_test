package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 領取【店鋪券】
 * @author zwm
 */
public class ReceiveStoreCouponFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 卡券類型
     */
    int couponType;

    String captchaKey;
    ImageView btnRefreshCaptcha;

    TextView tvCardPassTitle;
    EditText etCouponCardPass;
    EditText etCaptcha;


    public static ReceiveStoreCouponFragment newInstance(int couponType) {
        Bundle args = new Bundle();

        args.putInt("couponType", couponType);
        ReceiveStoreCouponFragment fragment = new ReceiveStoreCouponFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive_store_coupon, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        couponType = args.getInt("couponType");
        SLog.info("couponType[%d]", couponType);

        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        tvCardPassTitle = view.findViewById(R.id.tv_card_pass_title);
        etCouponCardPass = view.findViewById(R.id.et_coupon_card_pass);
        etCaptcha = view.findViewById(R.id.et_captcha);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_refresh_captcha, this);

        SimpleTabManager tabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.tab_platform_coupon) {
                    SLog.info("平臺券");
                    tvCardPassTitle.setText(R.string.text_platform_coupon_card_pass);
                    etCouponCardPass.setHint(R.string.input_platform_coupon_card_pass_hint);
                } else if (id == R.id.tab_store_coupon) {
                    SLog.info("店鋪券");
                    tvCardPassTitle.setText(R.string.text_store_coupon_card_pass);
                    etCouponCardPass.setHint(R.string.input_store_coupon_card_pass_hint);
                }
            }
        };

        tabManager.add(view.findViewById(R.id.tab_platform_coupon));
        tabManager.add(view.findViewById(R.id.tab_store_coupon));

        // 初始化選中哪個Tab
        if (couponType == Constant.COUPON_TYPE_STORE) {
            SLog.info("HERE");
            tabManager.performClick(1);
        } else if (couponType == Constant.COUPON_TYPE_PLATFORM) {
            SLog.info("HERE");
            tabManager.performClick(0);
        }

        refreshCaptcha();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_ok) {
            receiveStoreCoupon();
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void receiveStoreCoupon() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "pwdCode", etCouponCardPass.getText().toString().trim(),
                "captchaKey", captchaKey,
                "captchaVal", etCaptcha.getText().toString().trim());

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_RECEIVE_STORE_COUPON, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        etCaptcha.setText("");
                        refreshCaptcha();
                        return;
                    }

                    ToastUtil.success(_mActivity, "領取成功");
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 刷新驗證碼
     */
    private void refreshCaptcha() {
        Api.refreshCaptcha(new TaskObserver() {
            @Override
            public void onMessage() {
                Pair<Bitmap, String> result = (Pair<Bitmap, String>) message;
                if (result == null) {
                    return;
                }
                captchaKey = result.second;
                btnRefreshCaptcha.setImageBitmap(result.first);
            }
        });
    }
}
