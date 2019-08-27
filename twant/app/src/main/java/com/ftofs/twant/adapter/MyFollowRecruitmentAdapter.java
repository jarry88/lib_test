package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.WantedPostItem;
import com.ftofs.twant.widget.ScaledButton;

import java.util.List;

public class MyFollowRecruitmentAdapter extends BaseQuickAdapter<WantedPostItem, BaseViewHolder> {
    public MyFollowRecruitmentAdapter(int layoutResId, @Nullable List<WantedPostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WantedPostItem item) {
        helper.addOnClickListener(R.id.btn_expand);

        ScaledButton btnExpand = helper.getView(R.id.btn_expand);
        if (item.isJobDescExpanded) {
            btnExpand.setIconResource(R.drawable.btn_expanded_black);
        } else {
            btnExpand.setIconResource(R.drawable.expand_button_big);
        }


        helper.setText(R.id.tv_job_title, item.postType + " | " + item.postTitle)
        .setText(R.id.tv_salary_range, item.salaryRange)
                .setText(R.id.tv_salary_unit, item.currency + "/" + item.salaryType)
                .setText(R.id.tv_post_area, item.postArea)
                .setText(R.id.tv_mail_box, item.mailbox)
                .setText(R.id.tv_job_desc, item.postDescription);

        helper.setGone(R.id.ll_job_desc_container, item.isJobDescExpanded);
    }
}

