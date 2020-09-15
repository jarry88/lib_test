package com.ftofs.twant.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.lib_net.model.Goods;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

public class VideoGoodsListAdapter extends ViewGroupAdapter<Goods> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public VideoGoodsListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, Goods itemData) {
        SLog.info("itemData.imageUrl[%s]", itemData.imageUrl);
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);

        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.imageUrl)).centerCrop().into(goodsImage);
    }
}
