package com.ftofs.twant.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.loader.ImageLoader;
import com.github.piasy.biv.view.BigImageView;

import java.io.File;
import java.util.List;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ORIENTATION_USE_EXIF;

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
        // 修复BigImageView图片旋转角度不正确的问题 参考：https://github.com/Piasy/BigImageViewer/issues/154
        bigImageView.setImageLoaderCallback(new ImageLoader.Callback() {
            @Override
            public void onCacheHit(int imageType, File image) {
                SubsamplingScaleImageView subsamplingScaleImageView = bigImageView.getSSIV();
                if (subsamplingScaleImageView != null) {
                    subsamplingScaleImageView.setOrientation(ORIENTATION_USE_EXIF);
                }
            }

            @Override
            public void onCacheMiss(int imageType, File image) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(File image) {

            }

            @Override
            public void onFail(Exception error) {

            }
        });
    }
}
