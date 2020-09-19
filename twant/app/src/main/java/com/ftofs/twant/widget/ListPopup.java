package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ListPopupAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;


/**
 * 通用列表彈窗
 * @author zwm
 */
public class ListPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;

    String title; // 彈出框的標題
    List<ListPopupItem> itemList;
    int index; // 選中的index
    OnSelectedListener onSelectedListener;
    boolean hasSeparator = true;  // 是否顯示分隔線
    boolean showUncheckedIndicator;  // 是否顯示未選中的提示圖標

    PopupType type;
    Object args; // 傳進來的調用參數


    /**
     * 列表彈框的構造方法
     * @param context
     * @param title
     * @param type
     * @param itemList
     * @param index 初始選中的索引
     * @param onSelectedListener
     */
    public ListPopup(@NonNull Context context, String title, PopupType type, List<ListPopupItem> itemList, int index, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.title = title;
        this.type = type;
        this.index = index;
        this.itemList = itemList;
        this.onSelectedListener = onSelectedListener;
    }


    public ListPopup(@NonNull Context context, String title, PopupType type, List<ListPopupItem> itemList, int index, OnSelectedListener onSelectedListener, Object args) {
        this(context, title, type, itemList, index, onSelectedListener);
        this.args = args;
    }

    public ListPopup(@NonNull Context context, String title, PopupType type, List<ListPopupItem> itemList,
                     int index, OnSelectedListener onSelectedListener, Object args, boolean hasSeparator, boolean showUncheckedIndicator) {
        this(context, title, type, itemList, index, onSelectedListener, args);
        this.hasSeparator = hasSeparator;
        this.showUncheckedIndicator = showUncheckedIndicator;
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.list_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_popup_title)).setText(title);

        RecyclerView rvList = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(layoutManager);
        ListPopupAdapter adapter = new ListPopupAdapter(context, type, this, itemList, index, hasSeparator, showUncheckedIndicator);
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

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, type[%s], id[%d], args[%s], extra[%s]", type, id, args, extra);
        if (type == PopupType.SHIPPING_TIME) {
            extra = args;
        }
        onSelectedListener.onSelected(type, id, extra);
        dismiss();
    }
}
