package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.adapter.StoreLabelPopupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaItemView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class StoreLabelPopup extends BottomPopupView implements View.OnClickListener {
    private final OnSelectedListener onSelectedListener;
    private final int twBlack;
    private  List<StoreLabel> dataList;
    Context context;
        PopupType popupType;

        List<StoreLabel> labelList;
        List<Category> selectedAreaList = new ArrayList<>();
        List<AreaItemView> areaItemViewList = new ArrayList<>();
    List<Category> firstCategoryList =new ArrayList<>();
    List<Category> secondCategoryList =new ArrayList<>();
    List<Category> thirdCategoryList =new ArrayList<>();
    List<ArrayList<Category>> categoryData=new ArrayList<>();
    private LinearLayout llStoreLabelContainer;
    private StoreLabelPopupAdapter adapter;

    private int categoryId;
    private int depth;
    private StoreLabel categoryName;

    public StoreLabelPopup(@NonNull Context context, PopupType popupType, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.popupType = popupType;
        this.onSelectedListener = onSelectedListener;
        twBlack = getResources().getColor(R.color.tw_black, null);
    }

    public StoreLabelPopup(@NonNull Context context, PopupType popupType, List<StoreLabel> dataList,OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.popupType = popupType;
        this.onSelectedListener = onSelectedListener;
        this.dataList = dataList;
        twBlack = getResources().getColor(R.color.tw_black, null);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.area_popup;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        categoryData.add((ArrayList<Category>) firstCategoryList);
        categoryData.add((ArrayList<Category>) secondCategoryList);
        categoryData.add((ArrayList<Category>) thirdCategoryList);
        llStoreLabelContainer = findViewById(R.id.ll_area_container);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);


        RecyclerView rvList = findViewById(R.id.rv_area_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new StoreLabelPopupAdapter(R.layout.area_popup_item, firstCategoryList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("position[%d]", position);
                if (position >= firstCategoryList.size()) {
                    return;
                }

                AreaItemView areaItemView = new AreaItemView(getContext());
                Category category = firstCategoryList.get(position);


                int depth = category.getDeep();
                if (depth>=selectedAreaList.size()) {
                    SLog.info("已经是最后一级, SIZE[%d], DEPTH[%d]", selectedAreaList.size(), depth);
                    setSelectLabelId(category);
                    dismiss();
                    return;
                }
                selectedAreaList.add(category);
                // 將之前的AreaItemView取消高亮
//                for (AreaItemView itemView : areaItemViewList) {
//                     itemView.setStatus(Constant.STATUS_UNSELECTED);
//                }
                areaItemView.setText(category.getCategoryName());
                areaItemView.setStatus(Constant.STATUS_SELECTED);
                areaItemView.setDepth(category.getDeep());
                areaItemView.setAreaId(category.getCategoryId());
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
                            Category parentArea = selectedAreaList.get(parentIndex);

                            loadLabelData(parentArea.getCategoryId());
                        }
                    }
                });
                areaItemViewList.add(areaItemView);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, Util.dip2px(getContext(), 15), 0);
                llStoreLabelContainer.addView(areaItemView, layoutParams);


                loadLabelData(category.getCategoryId());
            }
        });

        rvList.setAdapter(adapter);

        loadLabelData(0);
    }

    private void setSelectLabelId(Category category) {
        if (selectedAreaList.size() >= category.getDeep()) {
            selectedAreaList.set(category.getDeep()-1, category);
        } else {
            selectedAreaList.add(category);
        }
        onSelectedListener.onSelected(PopupType.STORE_LABEL,categoryId,selectedAreaList);
    }

    private void loadLabelData(int categoryId) {
        if (popupType == PopupType.STORE_CATEGORY) {
            loadLocalData(categoryId);
            return;
        }
         EasyJSONObject params =EasyJSONObject.generate("token", User.getToken());
          SLog.info("params[%s]", params);
          Api.getUI(Api.PATH_SELLER_GOODS_CATEGORY+String.format("/%d",categoryId), params, new UICallback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 ToastUtil.showNetworkError(context, e);
             }

             @Override
             public void onResponse(Call call, String responseStr) throws IOException {
                 try {
                     SLog.info("responseStr[%s]", responseStr);

                     EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                     if (ToastUtil.checkError(context, responseObj)) {
                         return;
                     }

                     EasyJSONArray categoryList = responseObj.getSafeArray("datas.categoryList");
                     if (categoryList != null) {
                         categoryData.get(depth).clear();
                         for (Object object : categoryList) {
                             categoryData.get(depth).add(Category.parse((EasyJSONObject) object));
                         }

                         if (depth == 0) {
                             adapter.setNewData(categoryData.get(0));
                         }
                     }
                 } catch (Exception e) {
                     SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                 }
             }
          });

    }

    private void loadLocalData(int categoryId) {
        if (dataList == null) {
            return;
        }
        List<StoreLabel> list =new ArrayList<>();
        if (depth == 0) {
            list = dataList;
        } else {
            for (StoreLabel label : dataList) {
                if (label.getStoreLabelId() == categoryId) {
                    list = label.getStoreLabelList();
                }
            }
        }
        categoryData.get(depth).clear();
        for (StoreLabel label : list) {
            Category category = new Category();
            category.setCategoryId(label.getStoreLabelId());
            category.setCategoryName(label.getStoreLabelName());
            categoryData.get(depth).add(category);
        }

        if (depth == 0) {
            adapter.setNewData(categoryData.get(0));
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
                onSelectedListener.onSelected(PopupType.DEFAULT,categoryId,null);
                dismiss();
                break;
            default:
                break;
        }
    }
}
