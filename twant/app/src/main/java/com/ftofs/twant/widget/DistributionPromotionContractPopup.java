package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class DistributionPromotionContractPopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    CountDownTimer countDownTimer;

    TextView btnOkText;
    ImageView btnOkBackground;

    boolean btnOkCanClick = false;  // 【確認】按钮是否能点击

    public DistributionPromotionContractPopup(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.distribution_promotion_contract_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        btnOkText = findViewById(R.id.btn_ok_text);
        btnOkBackground = findViewById(R.id.btn_ok_background);

        countDownTimer = new CountDownTimer(5000, 500) {
            public void onTick(long millisUntilFinished) {
                int remainSeconds = (int) (millisUntilFinished / 1000);
                String btnText = "確認";
                if (remainSeconds > 0) {
                    btnText = String.format("確認(%dS)", remainSeconds);
                } else if (remainSeconds == 0) {
                    btnOkBackground.setImageTintList(ColorStateList.valueOf(Util.getColor(R.color.tw_blue)));
                }

                btnOkText.setText(btnText);
            }
            public void onFinish() {
                // 倒計時結束
                btnOkCanClick = true;
                btnOkText.setText("確認");

                btnOkBackground.setImageTintList(ColorStateList.valueOf(Util.getColor(R.color.tw_blue)));
            }
        };
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();

        countDownTimer.start();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        super.dismiss();
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
        } else if (id == R.id.btn_ok) {
            if (!btnOkCanClick) {
                return;
            }

            dismiss();
        }
    }
}
