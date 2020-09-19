package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.BizCircleMenuAdapter;
import com.ftofs.twant.adapter.StoreSortCriteriaAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.BizCircleId;
import com.ftofs.twant.entity.BizCircleItem;
import com.ftofs.twant.entity.StoreSortCriteriaItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 商店搜索結果排序、過濾彈窗
 * @author zwm
 */
public class StoreFilterPopup extends PartShadowPopupView {
    public static final int FILTER_TYPE_NONE = 0;
    public static final int FILTER_TYPE_LOCATION = 1;
    public static final int FILTER_TYPE_BIZ_CIRCLE = 2;

    PopupType popupType;
    OnSelectedListener onSelectedListener;
    StoreSortCriteriaAdapter adapter;
    List<StoreSortCriteriaItem> storeSortCriteriaItemList;
    Object selectedData;
    List<BizCircleItem> bizCircleItemList;

    FlowLayout flBizCircleContainer;
    int twBlack;
    private BizCircleMenuAdapter bizCircleMenuAdapter;

    public StoreFilterPopup(@NonNull Context context, PopupType popupType, Object selectedData,
                            List<BizCircleItem> bizCircleItemList, OnSelectedListener onSelectedListener) {
        super(context);

        this.popupType = popupType;
        this.selectedData = selectedData;
        this.bizCircleItemList = bizCircleItemList;
        this.onSelectedListener = onSelectedListener;
        SLog.info("popupType[%s]", popupType);

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

        if (popupType == PopupType.STORE_SORT_TYPE) {
            findViewById(R.id.ll_biz_circle_container).setVisibility(GONE);
            LinearLayout llGeneralFilterContainer = findViewById(R.id.ll_general_filter_container);
            adapter = new StoreSortCriteriaAdapter(getContext(), llGeneralFilterContainer, R.layout.general_filter_item);
            adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                @Override
                public void onClick(ViewGroupAdapter adapter, View view, int position) {
                    if (onSelectedListener != null) {
                        if (popupType == PopupType.STORE_SORT_TYPE) {
                            StoreSortCriteriaItem storeSortCriteriaItem = storeSortCriteriaItemList.get(position);
                            selectedData = storeSortCriteriaItem.id;
                            refreshList();
                            SLog.info("storeSortCriteriaItem.id[%d]", storeSortCriteriaItem.id);
                            onSelectedListener.onSelected(popupType, storeSortCriteriaItem.id, storeSortCriteriaItem.name);
                            dismiss();
                        }
                    }
                }
            });

            refreshList();
        } else {
            SLog.info("bizCircleItemList.size[%d]", bizCircleItemList.size());
            findViewById(R.id.ll_general_filter_container).setVisibility(GONE);

            RecyclerView rvBizCircleMenuList = findViewById(R.id.rv_biz_circle_menu_list);
            rvBizCircleMenuList.setLayoutManager(new LinearLayoutManager(getContext()));
            bizCircleMenuAdapter = new BizCircleMenuAdapter(R.layout.biz_circle_menu_item, bizCircleItemList,getContext());
            bizCircleMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    displaySubItem(position);
                }
            });
            rvBizCircleMenuList.setAdapter(bizCircleMenuAdapter);

            displaySubItem(0);
        }
    }

    private void displaySubItem(int position) {
        int count = 0;
        for (BizCircleItem bizCircleItem : bizCircleItemList) {
            bizCircleItem.selected = false;
            if (position == count) {
                bizCircleItem.selected = true;
            }
            count++;
        }
        if (bizCircleMenuAdapter != null) {
            bizCircleMenuAdapter.notifyDataSetChanged();
        }

        List<BizCircleItem> subItemList = bizCircleItemList.get(position).subItemList;
        SLog.info("subItemList.size[%d]", subItemList.size());

        flBizCircleContainer.removeAllViews();
        LinearLayout linearLayoutLeft = new LinearLayout(getContext());
        LinearLayout linearLayoutMiddle = new LinearLayout(getContext());
        LinearLayout linearLayoutRight = new LinearLayout(getContext());
        linearLayoutLeft.setOrientation(LinearLayout.VERTICAL);
        linearLayoutMiddle.setOrientation(LinearLayout.VERTICAL);
        linearLayoutRight.setOrientation(LinearLayout.VERTICAL);
        int itemIndex = 0;
        for (BizCircleItem subItem : subItemList) {
            TextView tvItem = new TextView(getContext());
            tvItem.setText(subItem.name);
            tvItem.setTextColor(twBlack);
            MarginLayoutParams marginLayoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            marginLayoutParams.leftMargin = Util.dip2px(getContext(), 15);
            marginLayoutParams.rightMargin = Util.dip2px(getContext(), 15);
            marginLayoutParams.topMargin = Util.dip2px(getContext(), 12);
            marginLayoutParams.bottomMargin = Util.dip2px(getContext(), 12);
            tvItem.setLayoutParams(marginLayoutParams);
            tvItem.setTag(subItem.bizCircleId);

            tvItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BizCircleId bizCircleId = (BizCircleId) v.getTag();
                    if (popupType == PopupType.STORE_FILTER_LOCATION&&bizCircleId!=null) {
                        bizCircleId.id = subItem.id;
                    }
                    if (onSelectedListener != null) {
                        SLog.info("onSelectedListener, idType[%d], id[%d]", bizCircleId.idType, bizCircleId.id);
                        onSelectedListener.onSelected(popupType, bizCircleId.id,subItem);

                        dismiss();
                    }
                }
            });
            if (itemIndex % 3 == 0) {
                linearLayoutLeft.addView(tvItem);
            } else if(itemIndex%3==1) {
                linearLayoutMiddle.addView(tvItem);
            } else {
                linearLayoutRight.addView(tvItem);
            }
            itemIndex++;
        }
        LinearLayout.LayoutParams marginLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        linearLayoutLeft.setLayoutParams(marginLayoutParams);
        linearLayoutMiddle.setLayoutParams(marginLayoutParams);
        linearLayoutRight.setLayoutParams(marginLayoutParams);
        LinearLayout llContainer = new LinearLayout(getContext());
        llContainer.addView(linearLayoutLeft);
        llContainer.addView(linearLayoutMiddle);
        llContainer.addView(linearLayoutRight);
        MarginLayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContainer.setLayoutParams(layoutParams);
        flBizCircleContainer.addView(llContainer);
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    private void refreshList() {
        storeSortCriteriaItemList = new ArrayList<>();
        storeSortCriteriaItemList.add(new StoreSortCriteriaItem(1, "綜合", (int) selectedData == 1));
        storeSortCriteriaItemList.add(new StoreSortCriteriaItem(2, "關注量", (int) selectedData == 2));
        storeSortCriteriaItemList.add(new StoreSortCriteriaItem(3, "開店時長", (int) selectedData == 3));
        adapter.setData(storeSortCriteriaItemList);
    }
}
