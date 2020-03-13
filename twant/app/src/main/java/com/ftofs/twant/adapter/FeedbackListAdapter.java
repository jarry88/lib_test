package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.FeedbackItem;
import com.ftofs.twant.fragment.ImageViewerFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

import java.util.List;

public class FeedbackListAdapter extends BaseQuickAdapter<FeedbackItem, BaseViewHolder> {
    public FeedbackListAdapter(int layoutResId, @Nullable List<FeedbackItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedbackItem item) {
        Jarbon jarbon = new Jarbon(item.createTime);
        String createTime = jarbon.toDateTimeString();

        helper.setText(R.id.tv_create_time, createTime)
                .setText(R.id.tv_content, item.suggestContent);

        SquareGridLayout sglImageContainer = helper.getView(R.id.sgl_image_container);
        SLog.info("item.imageList.size[%d]", item.imageList.size());
        if (item.imageList.size() > 0) {
            for (String imageUrl : item.imageList) {
                final View imageWidget = LayoutInflater.from(mContext).inflate(R.layout.feedback_image_widget, sglImageContainer, false);
                ImageView imageView = imageWidget.findViewById(R.id.feedback_image);
                // 隱藏刪除按鈕
                imageWidget.findViewById(R.id.btn_remove_image).setVisibility(View.GONE);

                String absoluteImageUrl = StringUtil.normalizeImageUrl(imageUrl);
                SLog.info("absoluteImageUrl[%s]", absoluteImageUrl);
                Glide.with(mContext).load(absoluteImageUrl).centerCrop().into(imageView);
                imageWidget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.startFragment(ImageViewerFragment.newInstance(absoluteImageUrl));
                    }
                });

                sglImageContainer.addView(imageWidget);
            }
        } else {
            // 如果沒有圖片，則隱藏圖片容器
            sglImageContainer.setVisibility(View.GONE);
        }

        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }
}
