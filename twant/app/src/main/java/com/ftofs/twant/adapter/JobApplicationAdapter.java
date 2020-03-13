package com.ftofs.twant.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.JobInfoItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class JobApplicationAdapter extends BaseMultiItemQuickAdapter<JobInfoItem, BaseViewHolder> {
    public JobApplicationAdapter( List<JobInfoItem> data){
        super(data);
        addItemType(JobInfoItem.TYPE_POST,R.layout.application_item);
        addItemType(JobInfoItem.TYPE_DISPLAY,R.layout.hot_job);
    }

    @Override
    protected void convert(BaseViewHolder helper, JobInfoItem item) {
        if(item.itemType==JobInfoItem.TYPE_POST){
            helper.setText(R.id.tv_position_name,item.jobName)
                    .setText(R.id.tv_position_category,item.positionType)
                    .setText(R.id.tv_salary_range,item.salaryRange+item.salaryType)
                    .setText(R.id.tv_store_name,item.storeName)
                    .setText(R.id.tv_area,item.workArea);
            Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.storeAvatar)).into((ImageView)helper.getView(R.id.img_shop_avatar));
        }
    }
}
