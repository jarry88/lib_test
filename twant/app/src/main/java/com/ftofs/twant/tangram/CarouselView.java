package com.ftofs.twant.tangram;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.RoundedDataImageView;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class CarouselView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;
    private ImageView mImageView;

    public static final int GOODS_IMAGE_COUNT = 3;
    ImageView imgDesktop;
    RoundedDataImageView[] goodsImageArr = new RoundedDataImageView[GOODS_IMAGE_COUNT];
    TextView[] goodsPriceArr = new TextView[GOODS_IMAGE_COUNT];


    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        View contentView = LayoutInflater.from(context).inflate(R.layout.tangram_layout_home_carousel_view, this, false);

        mImageView = contentView.findViewById(R.id.img_banner);

        imgDesktop = contentView.findViewById(R.id.img_goods_desktop);
        goodsImageArr[0] = contentView.findViewById(R.id.goods_image1);
        goodsImageArr[1] = contentView.findViewById(R.id.goods_image2);
        goodsImageArr[2] = contentView.findViewById(R.id.goods_image3);

        goodsPriceArr[0] = contentView.findViewById(R.id.tv_goods_price1);
        goodsPriceArr[1] = contentView.findViewById(R.id.tv_goods_price2);
        goodsPriceArr[2] = contentView.findViewById(R.id.tv_goods_price3);


        for (int i = 0; i < GOODS_IMAGE_COUNT; i++) {
            goodsImageArr[i].setOnClickListener(v -> {
                int commonId = (int) ((RoundedDataImageView) v).getCustomData();
                SLog.info("commonId[%d]", commonId);
                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            });
        }

        addView(contentView);
    }

    public void setGoodsImageVisibility(int visibility,int count) {
        for (int i = 0; i < count; ++i) {
            goodsImageArr[i].setVisibility(visibility);
            goodsPriceArr[i].setVisibility(visibility);
        }
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @Override
    public void postBindView(BaseCell cell) {
        // SLog.info("CarouselView::postBindView");
        Object data = cell.optParam("data");
        if (data != null) {
            WebSliderItem webSliderItem = (WebSliderItem) data;

            // 数据绑定
            String imageUrl = StringUtil.normalizeImageUrl(webSliderItem.image);
            Glide.with(context).load(imageUrl).centerCrop().into(mImageView);
            //SLog.info("webSliderItem.linkType，[%s]",webSliderItem.linkType);

            imgDesktop.setVisibility(View.GONE);
            setGoodsImageVisibility(View.GONE,GOODS_IMAGE_COUNT);
            if(!webSliderItem.goodsCommons.equals("[]")) {
                String goodsCommons = webSliderItem.goodsCommons;
                EasyJSONArray goodsArray = (EasyJSONArray) EasyJSONArray.parse(goodsCommons);
                //SLog.info("goodsArray%d",goodsArray.length());
                for (int i = 0; i < goodsArray.length(); i++) {
                    EasyJSONObject goods;
                    try {
                        goods = goodsArray.getObject(i);
                        int commonId = goods.getInt("commonId");
                        float price = (float) goods.getDouble("goodsPrice0");
                        String goodsImage = StringUtil.normalizeImageUrl(goods.getSafeString("goodsImage"));
                        if (StringUtil.isEmpty(goodsImage)) {
                            goodsImageArr[i].setVisibility(View.GONE);
                        }
                        Glide.with(context).load(goodsImage).centerCrop().into(goodsImageArr[i]);
                        goodsImageArr[i].setCustomData(commonId);
                        goodsPriceArr[i].setText(StringUtil.formatPrice(context, price, 0, false));
                        goodsPriceArr[i].setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }

                    if (goodsArray.length() > 0) {
                        imgDesktop.setVisibility(View.VISIBLE);
                        setGoodsImageVisibility(View.VISIBLE, goodsArray.length());
                    }
                }
            }
        }
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
