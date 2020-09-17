package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.VideoItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

import java.util.List;

public class VideoListAdapter extends BaseMultiItemQuickAdapter<VideoItem, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public VideoListAdapter(List<VideoItem> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.video_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoItem item) {
        if (item.getItemType() == Constant.ITEM_TYPE_NORMAL) {
            /*
            Youtube封面圖片， 以videoId為cwDqjmSmtMQ作例子
            //默认大小封面图
            https://img.youtube.com/vi/cwDqjmSmtMQ/default.jpg
            //高清封面图
            https://img.youtube.com/vi/cwDqjmSmtMQ/hqdefault.jpg
            //中等清晰度封面图
            https://img.youtube.com/vi/cwDqjmSmtMQ/mqdefault.jpg
            //标准清晰度封面图(640x480)
            https://img.youtube.com/vi/cwDqjmSmtMQ/sddefault.jpg
            //最大清晰度封面图
            https://img.youtube.com/vi/cwDqjmSmtMQ/maxresdefault.jpg
             */

            ImageView imgVideoCover = helper.getView(R.id.img_video_cover);
            String coverUrl = String.format("https://img.youtube.com/vi/%s/sddefault.jpg", item.videoId);
            SLog.info("__coverUrl[%s]", coverUrl);
            Glide.with(mContext).load(coverUrl).centerCrop().into(imgVideoCover);

            LinearLayout llGoodsContainer = helper.getView(R.id.ll_goods_container);
            llGoodsContainer.removeAllViews();
            VideoGoodsListAdapter videoGoodsListAdapter = new VideoGoodsListAdapter(mContext, llGoodsContainer, R.layout.video_goods_item);
            videoGoodsListAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                @Override
                public void onClick(ViewGroupAdapter adapter, View view, int position) {
                    int commonId = item.goodsList.get(position).id;
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                }
            });
            videoGoodsListAdapter.setData(item.goodsList);

            String playCount = "播放:" + item.playCount;
            helper.setText(R.id.tv_play_count, playCount)
                    .setText(R.id.tv_update_time, item.updateTime)
                    .addOnClickListener(R.id.btn_play);
        }
    }
}
