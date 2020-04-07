package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
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
        SLog.info("value[%d], delta[%d]", this.value, delta);

        Context context = getContext();
//        int goodsId = skuStatus.getGoodsId();
        int cartId = spuStatus.getCartId();

        Util.modifyCartContent(context, cartId, delta, new SimpleCallback() {
            @Override
            public void onSimpleCall(Object data) {
                CartAdjustButton.super.changeValue(delta);
            }
        });
    }
}
