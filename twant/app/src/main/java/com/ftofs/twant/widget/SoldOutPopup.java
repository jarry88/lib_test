package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SoldOutGoodsAdapter;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 售罄通知的弹窗
 * @author zwm
 */
public class SoldOutPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    List<SoldOutGoodsItem> soldOutGoodsItemList = new ArrayList<>();

    public SoldOutPopup(@NonNull Context context, List<SoldOutGoodsItem> soldOutGoodsItemList) {
        super(context);

        this.context = context;
        this.soldOutGoodsItemList = soldOutGoodsItemList;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.sold_out_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 设置弹窗背景
        Drawable backgroundDrawable = BackgroundDrawable.create(Color.WHITE, Util.dip2px(context, 6));
        findViewById(R.id.ll_popup_content_view).setBackground(backgroundDrawable);

        findViewById(R.id.btn_ok).setOnClickListener(this);

        LinearLayout llSoldOutListContainer = findViewById(R.id.ll_sold_out_list_container);
        SoldOutGoodsAdapter adapter = new SoldOutGoodsAdapter(context, llSoldOutListContainer, R.layout.sold_out_item);
        adapter.setData(soldOutGoodsItemList);
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
        return (int) (XPopupUtils.getWindowWidth(getContext()) * .8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_ok) {

        }
    }
}

