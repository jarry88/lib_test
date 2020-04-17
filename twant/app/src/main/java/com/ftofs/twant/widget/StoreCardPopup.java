package com.ftofs.twant.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.fragment.ShopHomeFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

class StoreCardPopup extends CenterPopupView implements View.OnClickListener{
    private final Context context;
    private final int storeId;

    public StoreCardPopup(@NonNull Context context,int storeId) {
        super(context);
        this.context = context;
        this.storeId = storeId;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.store_instruction_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_goto_store).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
        Api.getUI(Api.STORE_CARD_DETAIL, EasyJSONObject.generate("storeId", storeId), new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }
                if (responseObj.exists("datas.storeVo")) {
                    try {
                        EasyJSONObject storeVo = responseObj.getObject("datas.storeVo");
                        ImageView avatar = getRootView().findViewById(R.id.img_avatar);

                        String imgUrl =storeVo.getSafeString("storeAvatar");
                        Glide.with(context).load(StringUtil.normalizeImageUrl(imgUrl)).centerCrop().into(avatar);
                    } catch (EasyJSONException e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

                    }
                }

            }
        });
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
        if (id == R.id.btn_ok || id == R.id.btn_close) {
            Util.startFragment(ShopMainFragment.newInstance(storeId));
            dismiss();
        }
    }
}
