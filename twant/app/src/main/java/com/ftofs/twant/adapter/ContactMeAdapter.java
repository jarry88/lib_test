package com.ftofs.twant.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.JobInfoItem;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import javax.annotation.Nullable;

public class ContactMeAdapter extends BaseQuickAdapter<JobInfoItem, BaseViewHolder> {
    public ContactMeAdapter(int layoutResId,@Nullable List<JobInfoItem> data){
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JobInfoItem item) {
        helper.setText(R.id.tv_store_name,item.storeName)
        .setText(R.id.tv_contact_title,String.format("[%s]的招聘者想要與您進行溝通，是否顯示您的基礎信息給對方",item.storeName));
        helper.addOnClickListener(R.id.tv_contact_title)
                .addOnClickListener(R.id.tv_store_name)
                .addOnClickListener(R.id.sb_show_contact_info);
        helper.getView(R.id.sb_show_contact_info).setVisibility(item.isLook==0? View.VISIBLE:View.GONE);
        ((SwitchButton) helper.getView(R.id.sb_show_contact_info)).setChecked(item.isLook!=0);
        helper.getView(R.id.tv_show).setVisibility(item.isLook==1? View.VISIBLE:View.GONE);
    }
}
