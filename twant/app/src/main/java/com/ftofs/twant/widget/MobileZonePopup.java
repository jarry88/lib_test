package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ftofs.twant.MobileZoneSelectedListener;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MobileZoneAdapter;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.fragment.RegisterFragment;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;


/**
 * 區號選擇彈窗
 * @author zwm
 */
public class MobileZonePopup extends BottomPopupView {
    MobileZoneSelectedListener mobileZoneSelectedListener;
    RecyclerView recyclerView;
    MobileZoneAdapter adapter;
    List<MobileZone> mobileZoneList;
    Context context;

    /**
     * 高亮的區號的索引
     */
    int highlightedIndex;
    
    public MobileZonePopup(@NonNull Context context, List<MobileZone> mobileZoneList, int selectedIndex,
                           MobileZoneSelectedListener mobileZoneSelectedListener) {
        super(context);

        this.context = context;
        this.mobileZoneList = mobileZoneList;
        this.highlightedIndex = selectedIndex;
        this.mobileZoneSelectedListener = mobileZoneSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.mobile_zone_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MobileZoneAdapter(context, mobileZoneList, highlightedIndex);
        recyclerView.setAdapter(adapter);

        // 點擊【確定】按鈕
        findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                highlightedIndex = adapter.getHighlightedIndex();
                mobileZoneSelectedListener.onMobileZoneSelected(highlightedIndex);

                dismiss();
            }
        });
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
}
