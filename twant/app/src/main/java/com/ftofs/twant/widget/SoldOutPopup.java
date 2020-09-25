package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SoldOutGoodsAdapter;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;


/**
 * 售罄通知的弹窗
 * @author zwm
 */
public class SoldOutPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    List<SoldOutGoodsItem> soldOutGoodsItemList;
    boolean partialAvailable;  // true -- 部分售罄, false -- 全部售罄
    OnSelectedListener onSelectedListener;

    public SoldOutPopup(@NonNull Context context, List<SoldOutGoodsItem> soldOutGoodsItemList, boolean partialAvailable, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.soldOutGoodsItemList = soldOutGoodsItemList;
        this.partialAvailable = partialAvailable;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.sold_out_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView btnOk = findViewById(R.id.btn_ok);
        TextView btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("抱歉，您購買的商品無貨");

        // 设置弹窗背景
        Drawable backgroundDrawable = BackgroundDrawable.create(Color.WHITE, Util.dip2px(context, 6));
        findViewById(R.id.ll_popup_content_view).setBackground(backgroundDrawable);

        btnOk.setOnClickListener(this);
        btnBack.setOnClickListener(this);

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
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(PopupType.HANDLE_SOLD_OUT_GOODS, 1, null);
                dismiss();
            }
        } else if (id == R.id.btn_back) {
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(PopupType.HANDLE_SOLD_OUT_GOODS, 2, null);
                dismiss();
            }
        }
    }
}

