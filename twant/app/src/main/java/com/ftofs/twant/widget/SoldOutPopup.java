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
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
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
        if (partialAvailable) {
            tvTitle.setText("抱歉，部分產品已售罄！");
            btnOk.setText("移除");
        } else {
            tvTitle.setText("抱歉，產品已售罄！");
            btnOk.setText("返回");
        }

        // 设置弹窗背景
        Drawable backgroundDrawable = BackgroundDrawable.create(Color.WHITE, Util.dip2px(context, 6));
        findViewById(R.id.ll_popup_content_view).setBackground(backgroundDrawable);

        btnOk.setOnClickListener(this);

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
                onSelectedListener.onSelected(PopupType.HANDLE_SOLD_OUT_GOODS, 0, null);
                dismiss();
            }
        }
    }
}

