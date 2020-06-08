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
import com.ftofs.twant.fragment.BlackTestFragment;
import com.ftofs.twant.fragment.SellerRefundFragment;
import com.ftofs.twant.fragment.TestFragment;
<<<<<<< HEAD
import com.ftofs.twant.seller.fragment.SellerFeaturesFragment;
=======
import com.ftofs.twant.seller.fragment.AddGoodsFragment;
>>>>>>> master
import com.ftofs.twant.seller.fragment.SellerGoodsListFragment;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

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
        /*
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        imgLogo = new ImageView(context);
        imgLogo.setImageResource(R.drawable.icon_takewant);
        addView(imgLogo);

        imgLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.DEVELOPER_MODE) {
//                    Util.startFragment(SellerRefundFragment.newInstance());
                    Util.startFragment(SellerFeaturesFragment.newInstance());
                }
            }
        });

         */
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
