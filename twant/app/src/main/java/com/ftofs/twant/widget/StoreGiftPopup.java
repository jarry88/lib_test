package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsConformAdapter;
import com.ftofs.twant.adapter.GoodsGiftAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.GoodsConformItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 商店贈品彈窗
 * (商店促銷也使用這個彈窗)
 * @author zwm
 */
public class StoreGiftPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;

    RecyclerView rvList;
    List<GiftItem> giftItemList;
    List<GoodsConformItem> goodsConformItemList;
    GoodsGiftAdapter goodsGiftAdapter;
    GoodsConformAdapter goodsConformAdapter;

    /**
     * TabId常量定義
     */
    public static final int TAB_ID_CONFORM = 1;
    public static final int TAB_ID_GIFT = 2;

    int tabId; // 當前在顯示哪個Tab

    TwTabButton ttbConform;
    TwTabButton ttbGift;
    RelativeLayout btnConformTab;
    RelativeLayout btnGiftTab;

    /**
     * 構造方法
     * @param context
     * @param tabId
     * @param giftItemList 贈品列表（SKU級別）
     * @param goodsConformItemList 滿優惠列表（SPU或商店級別）
     */
    public StoreGiftPopup(@NonNull Context context, int tabId, List<GiftItem> giftItemList, List<GoodsConformItem> goodsConformItemList) {
        super(context);

        this.context = context;
        this.tabId = tabId;

        this.giftItemList = giftItemList;
        if (this.giftItemList == null) {
            this.giftItemList = new ArrayList<>();
        }
        this.goodsConformItemList = goodsConformItemList;
        if (this.goodsConformItemList == null) {
            this.goodsConformItemList = new ArrayList<>();
        }
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.store_gift_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        ttbConform = findViewById(R.id.ttb_conform);
        ttbConform.setText(context.getString(R.string.text_conform_simplify) + "(" + goodsConformItemList.size() + ")");
        ttbConform.setOnClickListener(this);
        ttbGift = findViewById(R.id.ttb_gift);
        ttbGift.setText(context.getString(R.string.text_gift) + "(" + giftItemList.size() + ")");
        ttbGift.setOnClickListener(this);
        btnConformTab = findViewById(R.id.btn_conform_tab);
        btnConformTab.setOnClickListener(this);
        btnGiftTab = findViewById(R.id.btn_gift_tab);
        btnGiftTab.setOnClickListener(this);

        rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(layoutManager);
        goodsGiftAdapter = new GoodsGiftAdapter(context, R.layout.goods_gift_list_item, giftItemList);
        goodsGiftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GiftItem giftItem = giftItemList.get(position);
                dismiss();
                Util.startFragment(GoodsDetailFragment.newInstance(giftItem.commonId, giftItem.goodsId));
            }
        });
        goodsConformAdapter = new GoodsConformAdapter(context, R.layout.goods_conform_list_item, goodsConformItemList, this);
        if (tabId == TAB_ID_CONFORM) {
            rvList.setAdapter(goodsConformAdapter);
            ttbConform.setStatus(Constant.STATUS_SELECTED);
        } else {
            rvList.setAdapter(goodsGiftAdapter);
            ttbGift.setStatus(Constant.STATUS_SELECTED);
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
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_conform_tab || id == R.id.ttb_conform) { // 滿優惠
            if (tabId == TAB_ID_CONFORM) {
                return;
            }
            rvList.setAdapter(goodsConformAdapter);
            ttbConform.setStatus(Constant.STATUS_SELECTED);
            ttbGift.setStatus(Constant.STATUS_UNSELECTED);
            tabId = TAB_ID_CONFORM;
        } else if (id == R.id.btn_gift_tab || id == R.id.ttb_gift) { // 贈品
            if (tabId == TAB_ID_GIFT) {
                return;
            }
            rvList.setAdapter(goodsGiftAdapter);
            ttbConform.setStatus(Constant.STATUS_UNSELECTED);
            ttbGift.setStatus(Constant.STATUS_SELECTED);
            tabId = TAB_ID_GIFT;
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.DEFAULT) {
            dismiss();
        }
    }
}


