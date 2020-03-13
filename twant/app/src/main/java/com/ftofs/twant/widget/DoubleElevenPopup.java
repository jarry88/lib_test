package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 雙十一活動彈窗
 * @author zwm
 */
public class DoubleElevenPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    String content;


    public DoubleElevenPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.double_eleven_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.img_activity).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
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
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 1f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_activity) {
            Util.startDoubleElevenFragment();
            dismiss();
        } else if (id == R.id.btn_close) {
            dismiss();
        }
    }
}

