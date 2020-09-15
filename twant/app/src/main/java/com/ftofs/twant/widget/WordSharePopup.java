package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.WeixinUtil;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.orhanobut.hawk.Hawk;

/**
 * 口令碼分享彈窗
 * @author zwm
 */
public class WordSharePopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    String word;  // 口令內容
    boolean wxInstalled; // 微信是否已安裝

    public WordSharePopup(@NonNull Context context, String word) {
        super(context);

        this.context = context;
        this.word = word;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.word_share_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView tvWord = findViewById(R.id.tv_word);
        tvWord.setText(word);

        wxInstalled = TwantApplication.Companion.get().getWxApi().isWXAppInstalled();
        TextView btnShare = findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);
        // 檢測微信是否已經安裝
        if (wxInstalled) {
            btnShare.setText("去微信粘貼給好友");
        } else {
            btnShare.setText("粘貼給好友");
        }

        // 將口令複製到剪貼析
        if (!StringUtil.isEmpty(word)) {
            Hawk.put(SPField.FIELD_IN_APP_COPY_TIMESTAMP, System.currentTimeMillis());
            ClipboardUtils.copyText(context, word);
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
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_share) {
            if (wxInstalled) {
                WeixinUtil.WeixinShareInfo shareInfo = new WeixinUtil.WeixinShareInfo();
                shareInfo.text = word;
                WeixinUtil.share(context, WeixinUtil.WX_SCENE_SESSION, WeixinUtil.SHARE_MEDIA_TEXT, shareInfo);
            }
            dismiss();
        }
    }
}
