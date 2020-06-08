package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.Area;
import com.ftofs.twant.domain.store.Seller;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerSpecAdapter;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaItemView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.litepal.util.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerSelectSpecPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    // 規格組名稱的List
    List<SellerSpecItem> sellerSpecItemList;
    // 當前的規格值列表
    List<SellerSpecItem> sellerSpecValueItemList;

    // 規格組Id與規格值列表的映射
    Map<Integer, List<SellerSpecItem>> sellerSpecValueMap;
    int type = SellerSpecItem.TYPE_SPEC;

    int action;  // 編輯還是添加
    int specId; // 當前選中的SpecId
    String specName; // 當前選中的規格組名稱

    TextView tvCurrentSpecName;

    OnSelectedListener onSelectedListener;
    SellerSpecAdapter adapter;
    int twBlack;

    LinearLayout llAreaContainer;

    TextView btnOk;

    /**
     * 選擇規格彈窗
     * @param context
     * @param action  編輯或添加
     * @param specId  編輯時用到，表示在編輯哪個規格
     * @param sellerSpecItemList 添加時用到，
     * @param sellerSpecValueMap
     * @param onSelectedListener
     */
    public SellerSelectSpecPopup(@NonNull Context context, int action, int specId, List<SellerSpecItem> sellerSpecItemList,
                                 @Nullable Map<Integer, List<SellerSpecItem>> sellerSpecValueMap, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.action = action;
        this.specId = specId;
        this.sellerSpecItemList = sellerSpecItemList;
        this.sellerSpecValueMap = sellerSpecValueMap;
        SLog.info("sellerSpecItemList.size[%d], sellerSpecValueMap.size[%d]",
                sellerSpecItemList.size(), sellerSpecValueMap == null ? -1 : sellerSpecValueMap.size());

        this.onSelectedListener = onSelectedListener;
        twBlack = getResources().getColor(R.color.tw_black, null);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.seller_select_spec_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        btnOk.setVisibility(INVISIBLE);

        if (action == Constant.ACTION_EDIT) {
            type = SellerSpecItem.TYPE_SPEC_VALUE;
            btnOk.setVisibility(VISIBLE);

            sellerSpecValueItemList = sellerSpecItemList;
        }

        tvCurrentSpecName = findViewById(R.id.tv_current_spec_name);
        llAreaContainer = findViewById(R.id.ll_area_container);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvList = findViewById(R.id.rv_area_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new SellerSpecAdapter(context, R.layout.seller_spec_item, sellerSpecItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("position[%d]", position);

                if (type == SellerSpecItem.TYPE_SPEC) { // 選擇規格組
                    SellerSpecItem sellerSpecItem = sellerSpecItemList.get(position);
                    specId = sellerSpecItem.id;
                    specName = sellerSpecItem.name;

                    tvCurrentSpecName.setText(specName);
                    tvCurrentSpecName.setVisibility(VISIBLE);

                    SLog.info("specId[%d]", specId);
                    sellerSpecValueItemList = sellerSpecValueMap.get(specId);
                    SLog.info("sellerSpecValueItemList[%d]", sellerSpecValueItemList.size());
                    adapter.setNewData(sellerSpecValueItemList);

                    type = SellerSpecItem.TYPE_SPEC_VALUE;

                    btnOk.setVisibility(VISIBLE);
                } else { // 選擇規格值
                    SellerSpecItem sellerSpecItem = sellerSpecValueItemList.get(position);
                    sellerSpecItem.selected = !sellerSpecItem.selected;

                    adapter.notifyItemChanged(position);
                }
            }
        });

        rvList.setAdapter(adapter);
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
            case R.id.btn_ok:
                if (onSelectedListener != null) {
                    EasyJSONArray specValueIdList = EasyJSONArray.generate();
                    if (action == Constant.ACTION_ADD) {
                        List<SellerSpecItem> sellerSpecItems = sellerSpecValueMap.get(specId);
                        if (sellerSpecItems == null) {
                            return;
                        }

                        for (SellerSpecItem item : sellerSpecItems) {
                            if (item.selected) {
                                specValueIdList.append(item.id);
                            }
                        }
                    } else {
                        for (SellerSpecItem item : sellerSpecItemList) {
                            if (item.selected) {
                                specValueIdList.append(item.id);
                            }
                        }
                    }


                    if (specValueIdList.length() < 1) {
                        ToastUtil.error(context, String.format("請選擇【%s】值", specName));
                        return;
                    }

                    EasyJSONObject dataObj = EasyJSONObject.generate(
                            "specId", specId,
                            "action", action,
                            "specValueIdList", specValueIdList
                    );
                    SLog.info("dataObj[%s]", dataObj.toString());
                    onSelectedListener.onSelected(PopupType.SELLER_SELECT_SPEC, 0, dataObj);

                    dismiss();
                }
                break;
            default:
                break;
        }
    }
}

