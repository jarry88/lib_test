package com.ftofs.twant.tangram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.SkuGalleryItem;
import com.ftofs.twant.fragment.ImageFragment;
import com.ftofs.twant.fragment.ImageViewerFragment;
import com.ftofs.twant.fragment.SkuImageFragment;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import java.util.ArrayList;
import java.util.List;

public class LogoView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;
    ImageView imgLogo;

    public LogoView(Context context) {
        this(context, null);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        imgLogo = new ImageView(context);
        imgLogo.setImageResource(R.drawable.icon_takewant);
        addView(imgLogo);

        imgLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.DEVELOPER_MODE) {
                    List<SkuGalleryItem> imageList = new ArrayList<>();
                    /*
                    imageList.add(new SkuGalleryItem("https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg", "", 0, ""));
                    imageList.add(new SkuGalleryItem("https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/9e/8b/9e8b38b29f88a9ca0e90640efdc6f60d.png", "", 0, ""));
                    imageList.add(new SkuGalleryItem("https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/d9/61/d961ac84a97b534e1f773f787e2e46d9.jpg", "", 0, ""));
                    imageList.add(new SkuGalleryItem("https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/2d/7b/2d7b37b49011ad629becc4217df67366.png", "", 0, ""));

                    Util.startFragment(SkuImageFragment.newInstance(null, imageList));
                     */
                }
            }
        });
    }

    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {

    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
