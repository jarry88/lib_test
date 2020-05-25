package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.seller.entity.TwDate;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.DrawerPopupView;

import java.util.ArrayList;
import java.util.List;

public class SellerOrderFilterDrawerPopupView extends DrawerPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;
    private int orderTypeSelectedIndex = 0;
    List<ListPopupItem> orderTypeList;
    private int orderSourceSelectedIndex = 0;
    List<ListPopupItem> orderSourceList;

    public SellerOrderFilterDrawerPopupView(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.seller_order_filter_drawer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        orderTypeList = new ArrayList<>();
        orderTypeList.add(new ListPopupItem(0, "所有", null));
        orderTypeList.add(new ListPopupItem(1, "零售", null));
        orderTypeList.add(new ListPopupItem(2, "代客下單", null));
        orderTypeList.add(new ListPopupItem(3, "跨城購", null));

        orderSourceList = new ArrayList<>();
        orderSourceList.add(new ListPopupItem(0, "所有", null));
        orderSourceList.add(new ListPopupItem(1, "PC", null));
        orderSourceList.add(new ListPopupItem(2, "門店", null));

        findViewById(R.id.ll_popup_content_view).setOnClickListener(this);
        findViewById(R.id.btn_select_order_type).setOnClickListener(this);
        findViewById(R.id.btn_select_order_source).setOnClickListener(this);
        findViewById(R.id.btn_select_begin_date).setOnClickListener(this);
        findViewById(R.id.btn_select_end_date).setOnClickListener(this);

        //通过设置topMargin，可以让Drawer弹窗进行局部阴影展示
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
//        params.topMargin = 450;
        reset();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }


    private void reset() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_select_order_type) {
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(context, "訂單類型",
                            PopupType.SELECT_ORDER_TYPE, orderTypeList, orderTypeSelectedIndex, this))
                    .show();
        } else if (id == R.id.btn_select_order_source) {
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(context, "訂單來源",
                            PopupType.SELECT_ORDER_TYPE, orderSourceList, orderSourceSelectedIndex, this))
                    .show();
        } else if (id == R.id.btn_select_begin_date) {
            TwDate twDate = new TwDate(2020, 5, 20);
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new CalendarPopup(context, twDate, this))
                    .show();
        } else if (id == R.id.btn_select_end_date) {

        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.SELECT_ORDER_TYPE) {
            orderTypeSelectedIndex = id;
        } else if (type == PopupType.SELECT_ORDER_SOURCE) {
            orderSourceSelectedIndex = id;
        }
    }
}

