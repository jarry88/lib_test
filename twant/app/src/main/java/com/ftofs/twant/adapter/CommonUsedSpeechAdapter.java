package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.CommonUsedSpeech;

public class CommonUsedSpeechAdapter extends ViewGroupAdapter<CommonUsedSpeech> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public CommonUsedSpeechAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, CommonUsedSpeech itemData) {
        setText(itemView, R.id.tv_content, itemData.content);

        LinearLayout llContainer = itemView.findViewById(R.id.ll_container);
        // 如果是最后一項，不用分隔線
        if (position == getItemCount() - 1) {
            llContainer.setBackground(null);
        }
    }
}
