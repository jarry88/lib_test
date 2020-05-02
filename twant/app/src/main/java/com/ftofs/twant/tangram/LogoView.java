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
import com.ftofs.twant.fragment.ImageFragment;
import com.ftofs.twant.fragment.ImageViewerFragment;
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
                    List<String> imageList = new ArrayList<>();
                    imageList.add("https://gfile.oss-cn-hangzhou.aliyuncs.com/img/ABTD3D72991D32C1A7A940FA59B3CC367EF16F6E37A1890A058E9A42EB46DACEDD5.jpg");
                    imageList.add("https://gfile.oss-cn-hangzhou.aliyuncs.com/img/5f24336176dd4a80d94ba96d937992ca.png");
                    imageList.add("https://gfile.oss-cn-hangzhou.aliyuncs.com/img/0159525979bab9a8012193a329c12d.jpg");
                    Util.startFragment(ImageFragment.newInstance(0, imageList));
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
