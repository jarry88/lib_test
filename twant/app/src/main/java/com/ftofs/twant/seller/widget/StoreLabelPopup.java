package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaItemView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreLabelPopup extends BottomPopupView implements View.OnClickListener {
    private final OnSelectedListener onSelectedListener;
    private final int twBlack;
    Context context;
        PopupType popupType;

        List<StoreLabel> labelList;
        List<StoreLabel> selectedAreaList = new ArrayList<>();
        List<AreaItemView> areaItemViewList = new ArrayList<>();
    private LinearLayout llStoreLabelContainer;
    private AreaPopupAdapter adapter;

    public StoreLabelPopup(@NonNull Context context, PopupType popupType, List<StoreLabel> labelList, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.popupType = popupType;
        this.labelList = labelList;
        this.onSelectedListener = onSelectedListener;
        twBlack = getResources().getColor(R.color.tw_black, null);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.area_popup;
    }
    @Override
    protected void onCreate() {
        super.onCreate();

        llStoreLabelContainer = findViewById(R.id.ll_area_container);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);


        RecyclerView rvList = findViewById(R.id.rv_area_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new AreaPopupAdapter(R.layout.area_popup_item, labelList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("position[%d]", position);
                if (position >= labelList.size()) {
                    return;
                }

                AreaItemView areaItemView = new AreaItemView(getContext());
                StoreLabel area = labelList.get(position);

                int depth = area.getAreaDeep();
                if (selectedAreaList.size() >= depth) {
                    SLog.info("已经是最后一级, SIZE[%d], DEPTH[%d]", labelList.size(), depth);
                    setSelectLabelId();
                    return;
                }
                selectedAreaList.add(area);

                // 將之前的AreaItemView取消高亮
                for (AreaItemView itemView : areaItemViewList) {
                    // itemView.setStatus(Constant.STATUS_UNSELECTED);
                }
                areaItemView.setText(area.getStoreLabelName());
                areaItemView.setStatus(Constant.STATUS_SELECTED);
                areaItemView.setDepth(depth);
                areaItemView.setAreaId(area.getStoreLabelId());
                areaItemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 如果點擊自己，將自己和后面的數據出隊列，重新加載上一級的地址列表
                        AreaItemView itemView = (AreaItemView) v;
                        int depth = itemView.getDepth();
                        SLog.info("depth[%d]", depth);
                        int index = depth - 1;  // 點擊到的item的索引，從0開始
                        for (int i = selectedAreaList.size() - 1; i >= index ; i--) {
                            selectedAreaList.remove(i);
                        }

                        for (int i = areaItemViewList.size() - 1; i >= index ; i--) {
                            areaItemViewList.remove(i);
                        }

                        int childCount = llStoreLabelContainer.getChildCount();
                        if (childCount - index > 0) {
                            llStoreLabelContainer.removeViews(index, childCount - index);
                        }

                        int parentIndex = index - 1;
                        if (parentIndex == -1) {

loadLabelData(0);
                        } else {
                            StoreLabel parentArea = selectedAreaList.get(parentIndex);

loadLabelData(parentArea.getStoreLabelId());
                        }
                    }
                });
                areaItemViewList.add(areaItemView);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, Util.dip2px(getContext(), 15), 0);
                llStoreLabelContainer.addView(areaItemView, layoutParams);


loadLabelData(area.getStoreLabelId());
            }
        });

        rvList.setAdapter(adapter);

loadLabelData(0);
    }

    private void setSelectLabelId() {
    }

    private void loadLabelData(int storeLabelId) {

    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        SLog.info("onDismiss");
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_dismiss:
                dismiss();
                break;
            default:
                break;
        }
    }
}
