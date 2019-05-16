package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 購物車數量調節按鈕
 * @author zwm
 */
public class CartAdjustButton extends AdjustButton {
    public CartAdjustButton(Context context) {
        this(context, null);
    }

    public CartAdjustButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartAdjustButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void changeValue(final int delta) {
        SLog.info("value[%d], delta[%d]", this.value, delta);

        int goodsId = skuStatus.getGoodsId();
        String token = User.getToken();
        EasyJSONArray buyData = EasyJSONArray.generate(EasyJSONObject.generate("buyNum", delta, "goodsId", goodsId));
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", buyData.toString(),
                "clientType", Constant.CLIENT_TYPE_ANDROID);
        SLog.info("buyData[%s]", buyData.toString());

        SLog.info("params[%s]", params.toString());
        Api.postUI(Api.PATH_ADD_CART, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(getContext(), responseObj)) {
                    return;
                }

                CartAdjustButton.super.changeValue(delta);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_ADD_CART, null);
            }
        });
    }
}
