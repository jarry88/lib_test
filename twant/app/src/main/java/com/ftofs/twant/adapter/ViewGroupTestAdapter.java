package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

/**
 * ViewGroupAdapter的測試
 * @author zwm
 */
public class ViewGroupTestAdapter extends ViewGroupAdapter<String> {
    public ViewGroupTestAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        // 添加可點擊的childView Id
        addClickableChildrenId(R.id.btn_receive_voucher_now, R.id.tv_store_name);
    }

    @Override
    public void bindView(int position, View itemView, String itemData) {
        SLog.info("bindView[%d]", position);

        TextView tvStoreName = itemView.findViewById(R.id.tv_store_name);
        tvStoreName.setText(itemData);
    }
}