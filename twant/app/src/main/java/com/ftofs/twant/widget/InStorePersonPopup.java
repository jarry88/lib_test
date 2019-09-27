package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.InStorePersonListAdapter;
import com.ftofs.twant.adapter.ShopGoodsAdapter;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.InStorePersonItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.MemberInfoFragment;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 進店人員列表
 * @author zwm
 */
public class InStorePersonPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    InStorePersonListAdapter adapter;
    int inStorePersonCount;
    List<InStorePersonItem> inStorePersonItemList;

    /**
     * 構造方法
     * @param context
     * @param inStorePersonCount 進店人數
     * @param inStorePersonItemList
     */
    public InStorePersonPopup(@NonNull Context context, int inStorePersonCount, List<InStorePersonItem> inStorePersonItemList) {
        super(context);

        this.context = context;
        this.inStorePersonCount = inStorePersonCount;
        this.inStorePersonItemList = inStorePersonItemList;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.in_store_person_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView tvPopupTitle = findViewById(R.id.tv_popup_title);
        String title = String.format(context.getString(R.string.text_in_store_person_list) + "(%d人)", inStorePersonCount); // 減去2個Label
        tvPopupTitle.setText(title);

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvList = findViewById(R.id.rv_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
        rvList.setLayoutManager(gridLayoutManager);
        adapter = new InStorePersonListAdapter(inStorePersonItemList);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                InStorePersonItem item = inStorePersonItemList.get(position);
                if (item.getItemType() == InStorePersonItem.TYPE_LABEL || item.getItemType() == InStorePersonItem.TYPE_EMPTY_HINT) {
                    return 5;
                } else {
                    return 1;
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InStorePersonItem item = inStorePersonItemList.get(position);
                // Label忽略點擊
                if (item.getItemType() == InStorePersonItem.TYPE_LABEL) {
                    return;
                }
                Util.startFragment(MemberInfoFragment.newInstance(item.memberName));
                dismiss();
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
        }
    }
}
