package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 新版口令彈窗
 * @author zwm
 */
public class NewWordPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    EasyJSONObject extra;

    String commandType;

    public NewWordPopup(@NonNull Context context, EasyJSONObject extra) {
        super(context);

        this.context = context;
        this.extra = extra;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.new_word_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ImageView imageView = findViewById(R.id.image_view);
        TextView tvDesc = findViewById(R.id.tv_desc);
        LinearLayout llMainContentContainer = findViewById(R.id.ll_main_content_container);
        llMainContentContainer.setBackground(BackgroundDrawable.create(Color.WHITE, Util.dip2px(context, 8)));

        commandType = extra.optString("commandType");
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        /*
        "zoneId":52,"commandType":"shoppingZone","zoneName":"測試生成分享口令","appLogo":"image/8e/37/8e370775524a29f9a86d4a214be49424.jpg"
         */
        if ("shoppingZone".equals(commandType)) {  // 如果是跳轉到購物賣場
            String appLogo = extra.optString("appLogo");
            Glide.with(context).load(StringUtil.normalizeImageUrl(appLogo)).centerCrop().into(imageView);
            tvDesc.setText(extra.optString("zoneName"));
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
        if (id == R.id.btn_ok) {
            if ("shoppingZone".equals(commandType)) {
                Util.startFragment(NewShoppingSpecialFragment.newInstance(extra.optInt("zoneId")));
            }
            ClipboardUtils.copyText(context, ""); // 清空剪貼板
            dismiss();
        } else if (id == R.id.btn_close) {
            ClipboardUtils.copyText(context, ""); // 清空剪貼板
            dismiss();
        }
    }
}
