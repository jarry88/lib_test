package com.ftofs.twant.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;

import java.util.List;

public class ImageListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;
    public ImageListAdapter(Context context, int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String imagePath) {
        helper.addOnClickListener(R.id.big_image_view);

        // 如果是本地路徑，還要添加上file://協議前綴
        if (imagePath.startsWith("/")) {
            imagePath = "file://" + imagePath;
        }
        BigImageView bigImageView = helper.getView(R.id.big_image_view);
        bigImageView.setProgressIndicator(new ProgressPieIndicator());
        bigImageView.showImage(Uri.parse(imagePath));
    }
}
