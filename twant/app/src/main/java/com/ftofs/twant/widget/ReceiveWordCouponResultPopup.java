package com.ftofs.twant.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class ReceiveWordCouponResultPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    int result;
    EasyJSONObject data;
    String activityType;

    public static final int RESULT_SUCCESS = 1;  // 領取成功
    public static final int RESULT_RUN_OUT = 2;  // 已經領完
    public static final int RESULT_REMAINING = 3; // 還有剩餘的優惠券
    public static final int RESULT_HAS_RECEIVED = 4; // 已經領過了

    private String[] resultDesc = new String[] {
            "",
            "領取成功",
            "手慢啦\n" + "優惠券已經被領光",
            "您還有優惠券沒有用完\n" + "使用後再來領取哦",
            "您還有優惠券沒有用完\n" + "使用後再來領取哦",
    };

    private String[] buttonText = new String[] {
            "",
            "立即使用",
            "我知道了",
            "立即使用",
            "我知道了",
    };

    TextView tvMessage;
    TextView btnOk;

    public ReceiveWordCouponResultPopup(@NonNull Context context, int result, EasyJSONObject data) {
        super(context);

        this.context = context;
        this.result = result;
        this.data = data;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.receive_word_coupon_result_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        try {
            activityType = data.getSafeString("activityType");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        tvMessage = findViewById(R.id.tv_message);
        tvMessage.setText(resultDesc[result]);

        btnOk = findViewById(R.id.btn_ok);
        btnOk.setText(buttonText[result]);
        btnOk.setOnClickListener(this);
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
        if (id == R.id.btn_ok) {
            if (result == RESULT_SUCCESS) {
                try {
                    if (Constant.WORD_COUPON_TYPE_STORE.equals(activityType)) {
                        int storeId = data.getInt("storeId");

                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                    } else if (Constant.WORD_COUPON_TYPE_PLATFORM.equals(activityType)) {
                        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("keyword", "").toString()));
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
            dismiss();
        }
    }
}
