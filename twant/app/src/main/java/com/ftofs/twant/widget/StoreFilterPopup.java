package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.BizCircleMenuAdapter;
import com.ftofs.twant.adapter.GeneralFilterAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.BizCircleItem;
import com.ftofs.twant.entity.GeneralFilterItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class StoreFilterPopup extends PartShadowPopupView {
    public static final int TYPE_GENERAL = 1;
    public static final int TYPE_LOCATION = 2;
    public static final int TYPE_BIZ_CIRCLE = 3;

    int type;
    OnSelectedListener onSelectedListener;
    GeneralFilterAdapter adapter;
    List<GeneralFilterItem> generalFilterItemList;
    int selectedId;
    List<BizCircleItem> bizCircleItemList;

    FlowLayout flBizCircleContainer;
    int twBlack;

    public StoreFilterPopup(@NonNull Context context, int type, int selectedId,
                            List<BizCircleItem> bizCircleItemList, OnSelectedListener onSelectedListener) {
        super(context);

        this.type = type;
        this.selectedId = selectedId;
        this.bizCircleItemList = bizCircleItemList;
        this.onSelectedListener = onSelectedListener;
        SLog.info("type[%d]", type);

        twBlack = context.getColor(R.color.tw_black);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.store_filter_popup;
    }

    TextView text;
    @Override
    protected void onCreate() {
        super.onCreate();
        text = findViewById(R.id.text);
        flBizCircleContainer = findViewById(R.id.fl_biz_circle_container);
        Log.e("tag","StoreFilterPopup onCreate");

        if (type == TYPE_GENERAL) {
            findViewById(R.id.ll_biz_circle_container).setVisibility(GONE);
            LinearLayout llGeneralFilterContainer = findViewById(R.id.ll_general_filter_container);
            adapter = new GeneralFilterAdapter(getContext(), llGeneralFilterContainer, R.layout.general_filter_item);
            adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                @Override
                public void onClick(ViewGroupAdapter adapter, View view, int position) {
                    if (onSelectedListener != null) {
                        GeneralFilterItem generalFilterItem = generalFilterItemList.get(position);
                        selectedId = generalFilterItem.id;
                        refreshList();
                        SLog.info("generalFilterItem.id[%d]", generalFilterItem.id);
                        onSelectedListener.onSelected(PopupType.STORE_GENERAL_FILTER, generalFilterItem.id, generalFilterItem.name);
                        dismiss();
                    }
                }
            });

            refreshList();
        } else {
            SLog.info("bizCircleItemList.size[%d]", bizCircleItemList.size());
            findViewById(R.id.ll_general_filter_container).setVisibility(GONE);

            RecyclerView rvBizCircleMenuList = findViewById(R.id.rv_biz_circle_menu_list);
            rvBizCircleMenuList.setLayoutManager(new LinearLayoutManager(getContext()));
            BizCircleMenuAdapter adapter = new BizCircleMenuAdapter(R.layout.biz_circle_menu_item, bizCircleItemList);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<BizCircleItem> subItemList = bizCircleItemList.get(position).subItemList;
                    SLog.info("subItemList.size[%d]", subItemList.size());

                    flBizCircleContainer.removeAllViews();
                    for (BizCircleItem subItem : subItemList) {
                        TextView tvItem = new TextView(getContext());
                        tvItem.setText(subItem.name);
                        tvItem.setTextColor(twBlack);
                        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        marginLayoutParams.leftMargin = Util.dip2px(getContext(), 15);
                        marginLayoutParams.rightMargin = Util.dip2px(getContext(), 15);
                        marginLayoutParams.topMargin = Util.dip2px(getContext(), 20);
                        marginLayoutParams.bottomMargin = Util.dip2px(getContext(), 20);
                        tvItem.setLayoutParams(marginLayoutParams);

                        flBizCircleContainer.addView(tvItem);
                    }
                }
            });
            rvBizCircleMenuList.setAdapter(adapter);
        }

    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag","StoreFilterPopup onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag","StoreFilterPopup onDismiss");
    }

    private void refreshList() {
        generalFilterItemList = new ArrayList<>();
        generalFilterItemList.add(new GeneralFilterItem(1, "綜合", selectedId == 1));
        generalFilterItemList.add(new GeneralFilterItem(2, "關注量", selectedId == 2));
        generalFilterItemList.add(new GeneralFilterItem(3, "開店時長", selectedId == 3));
        adapter.setData(generalFilterItemList);
    }
}
