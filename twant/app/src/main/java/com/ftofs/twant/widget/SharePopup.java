package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 分享底部彈窗
 * @author zwm
 */
public class SharePopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    int storeId;

    public SharePopup(@NonNull Context context, int storeId) {
        super(context);

        this.context = context;
        this.storeId = storeId;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.share_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        findViewById(R.id.btn_share_to_friend).setOnClickListener(this);
        findViewById(R.id.btn_share_to_timeline).setOnClickListener(this);
        findViewById(R.id.btn_copy_link).setOnClickListener(this);
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
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_share_to_friend) {
            ToastUtil.info(context, "暫未實現");
        } else if (id == R.id.btn_share_to_timeline) {
            ToastUtil.info(context, "暫未實現");
        } else if (id == R.id.btn_copy_link) {
            String url = Config.WEB_BASE_URL + "/store/" + storeId;
            ClipboardUtils.copyText(context, url);
            new XPopup.Builder(context)
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                        }
                        @Override
                        public void onDismiss() {
                        }
                    }).asCustom(new TwConfirmPopup(context, "分享鏈接已復制", url, new OnConfirmCallback() {
                @Override
                public void onYes() {
                    SLog.info("onYes");
                }

                @Override
                public void onNo() {
                    SLog.info("onNo");
                }
            }))
                    .show();
        }
    }
}
