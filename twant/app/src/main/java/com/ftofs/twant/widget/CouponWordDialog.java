package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.sxu.shadowdrawable.ShadowDrawable;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class CouponWordDialog extends CenterPopupView implements View.OnClickListener {
    Context context;

    String couponWord;
    EasyJSONObject extraData;


    ImageView imgStoreAvatar;
    TextView tvStoreName;
    TextView tvAmount;
    TextView tvCouponTypeDesc;
    TextView tvCouponValidTime;

    View rlCouponBannerContainer;


    int storeId;
    public CouponWordDialog(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.coupon_word_dialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_receive).setOnClickListener(this);

        imgStoreAvatar = findViewById(R.id.img_store_avatar);
        tvStoreName = findViewById(R.id.tv_store_name);
        tvAmount = findViewById(R.id.tv_amount);
        tvCouponTypeDesc = findViewById(R.id.tv_coupon_type_desc);
        tvCouponValidTime = findViewById(R.id.tv_coupon_valid_time);
        rlCouponBannerContainer = findViewById(R.id.rl_coupon_banner_container);

        ShadowDrawable.setShadowDrawable(rlCouponBannerContainer, Color.parseColor("#FFFFFF"), Util.dip2px(context, 5),
                Color.parseColor("#19000000"), Util.dip2px(context, 5), 0, 0);


    }

    public void setData(String couponWord, EasyJSONObject extraData) {
        this.couponWord = couponWord;
        this.extraData = extraData;

        try {
            String activityType = extraData.getSafeString("activityType");
            double price = extraData.getDouble("price");

            tvAmount.setText(StringUtil.formatFloat(price));
            String useStartTimeText = extraData.getSafeString("useStartTimeText");
            String useEndTimeText = extraData.getSafeString("useEndTimeText");
            tvCouponValidTime.setText(useStartTimeText + "  -  " + useEndTimeText);

            if (Constant.WORD_COUPON_TYPE_STORE.equals(activityType)) { // 店鋪券
                tvCouponTypeDesc.setText("店鋪專用");

                String storeAvatar = extraData.getSafeString("storeAvatar");
                String storeName = extraData.getSafeString("storeName");
                tvStoreName.setText(storeName);
                Glide.with(context).load(StringUtil.normalizeImageUrl(storeAvatar)).centerCrop().into(imgStoreAvatar);
            } else if (Constant.WORD_COUPON_TYPE_PLATFORM.equals(activityType)) { // 平臺券
                tvCouponTypeDesc.setText("平台專用");

                tvStoreName.setText("全平台適用");
                Glide.with(context).load(R.drawable.app_logo).centerCrop().into(imgStoreAvatar);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * .8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            dismiss();
        } else if (id == R.id.btn_receive) {
            String token = User.getToken();

            if (StringUtil.isEmpty(token)) {
                Util.showLoginFragment();
                dismiss();
                return;
            }

            receiveCoupon(token);
        }
    }

    private void receiveCoupon(String token) {
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "command", couponWord);
        SLog.info("params%s",params);
        Api.postUI(Api.PATH_RECEIVE_WORD_COUPON, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try{
                    SLog.info("responseStr",responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (responseObj.exists("datas.storeId")) {
                        storeId = responseObj.getInt("datas.storeId");
                    }

                    EasyJSONObject dataObj = EasyJSONObject.generate(
                            "storeId", storeId
                    );


                    if (ToastUtil.isError(responseObj)) { // 領取錯誤
                        new XPopup.Builder(context)
                                .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                                .dismissOnTouchOutside(true) // 点击外部是否关闭弹窗，默认为true
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new ReceiveWordCouponResultPopup(context, ReceiveWordCouponResultPopup.RESULT_SUCCESS, dataObj))
                                .show();
                    } else { // 領取成功
                        new XPopup.Builder(context)
                                .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                                .dismissOnTouchOutside(true) // 点击外部是否关闭弹窗，默认为true
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new ReceiveWordCouponResultPopup(context, ReceiveWordCouponResultPopup.RESULT_RUN_OUT, dataObj))
                                .show();
                    }

                    dismiss();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}

