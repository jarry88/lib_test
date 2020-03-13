package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.MyFollowItem;
import com.ftofs.twant.widget.ScaledButton;

import java.util.List;

public abstract class MyFollowAdapter<T extends MyFollowItem, K extends BaseViewHolder> extends BaseQuickAdapter <T, K>{



    boolean allCheck;
    int mode = Constant.MODE_VIEW;

    public MyFollowAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        this.notifyDataSetChanged();
    }

    public void setAllCheck(boolean allCheck) {
        this.allCheck = allCheck;

    }



    public void switchMode(BaseViewHolder helper,MyFollowItem item) {
        ScaledButton btnCheckStore = helper.getView(R.id.btn_select);

        if (mode == Constant.MODE_VIEW) {
            btnCheckStore.setVisibility(View.GONE);
        }else{
            btnCheckStore.setVisibility(View.VISIBLE);
            btnCheckStore.setChecked(item.check);
        }
    }

}
