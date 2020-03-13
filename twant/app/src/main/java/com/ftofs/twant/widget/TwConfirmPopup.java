package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 自定義的確認彈窗
 * @author zwm
 */
public class TwConfirmPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    OnConfirmCallback callback;
    String title;
    String content;
    String textBtnYes = "確定";
    String textBtnNo = "取消";


    public TwConfirmPopup(@NonNull Context context, String title, String content, OnConfirmCallback callback) {
        this(context, title, content, null, null, callback);
    }

    public TwConfirmPopup(@NonNull Context context, String title, String content, String textBtnYes, String textBtnNo, OnConfirmCallback callback) {
        super(context);

        this.context = context;
        this.callback = callback;
        this.title = title;
        this.content = content;
        if (!StringUtil.isEmpty(textBtnYes)) {
            this.textBtnYes = textBtnYes;
        }
        if (!StringUtil.isEmpty(textBtnNo)) {
            this.textBtnNo = textBtnNo;
        } else {
            findViewById(R.id.btn_no).setVisibility(GONE);
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.tw_confirm_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView btnYes = findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(this);
        btnYes.setText(textBtnYes);

        TextView btnNo = findViewById(R.id.btn_no);
        btnNo.setOnClickListener(this);
        btnNo.setText(textBtnNo);

        TextView tvTitle = findViewById(R.id.tv_title);
        if (StringUtil.isEmpty(title)) {
            tvTitle.setVisibility(GONE);
        } else {
            tvTitle.setText(title);
        }

        TextView tvContent = findViewById(R.id.tv_content);
        if (StringUtil.isEmpty(content)) {
            tvContent.setVisibility(GONE);
        } else {
            tvContent.setText(content);
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
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.65f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_yes) {
            SLog.info("yes");
            if (callback != null) {
                callback.onYes();
            }
            dismiss();
        } else if (id == R.id.btn_no) {
            SLog.info("no");
            if (callback != null) {
                callback.onNo();
            }
            dismiss();
        }
    }
}
