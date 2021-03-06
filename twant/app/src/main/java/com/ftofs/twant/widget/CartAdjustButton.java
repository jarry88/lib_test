package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

/**
 * 購物袋數量調節按鈕
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
        if (spuStatus == null) {
            return;
        }
        int buyNum = spuStatus.getCount()+delta;
        SLog.info("value[%d], delta[%d],buyNum[%d]", this.value, delta,buyNum);

        Context context = getContext();
//        int goodsId = skuStatus.getGoodsId();
        int cartId = spuStatus.getCartId();

        Util.modifyCartContent(context, cartId, buyNum, new SimpleCallback() {
            @Override
            public void onSimpleCall(Object data) {
                CartAdjustButton.super.changeValue(delta);
            }
        });
    }
}
