package com.ftofs.twant.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreAnnouncementListAdapter;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.entity.StoreMapInfo;
import com.ftofs.twant.fragment.ExplorerFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

public class StoreAnnouncementPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    List<StoreAnnouncement> storeAnnouncementList;

    public StoreAnnouncementPopup(@NonNull Context context, List<StoreAnnouncement> storeAnnouncementList) {
        super(context);

        this.context = context;
        this.storeAnnouncementList = storeAnnouncementList;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.store_announcement_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        RecyclerView rvStoreAnnouncementList = findViewById(R.id.rv_store_announcement_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvStoreAnnouncementList.setLayoutManager(layoutManager);
        BaseQuickAdapter adapter = new StoreAnnouncementListAdapter(R.layout.store_announcement_list_item, storeAnnouncementList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreAnnouncement storeAnnouncement = storeAnnouncementList.get(position);
                String url = Config.WEB_BASE_URL + "/store/announcement/" + storeAnnouncement.id;

                // 使用瀏覽器顯示店鋪公告
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(ExplorerFragment.newInstance(url));

                dismiss();
            }
        });

        rvStoreAnnouncementList.setAdapter(adapter);
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
