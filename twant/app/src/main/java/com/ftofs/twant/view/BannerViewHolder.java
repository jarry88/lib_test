package com.ftofs.twant.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.RoundedDataImageView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class BannerViewHolder implements MZViewHolder<WebSliderItem> {
    private ImageView mImageView;

    public static final int GOODS_IMAGE_COUNT = 3;
    ImageView imgDesktop;
    RoundedDataImageView[] goodsImageArr = new RoundedDataImageView[GOODS_IMAGE_COUNT];
    TextView[] goodsPriceArr = new TextView[GOODS_IMAGE_COUNT];

    List<WebSliderItem> webSliderItemList;

    public BannerViewHolder(List<WebSliderItem> webSliderItemList) {
        this.webSliderItemList = webSliderItemList;
    }

    public void setGoodsImageVisibility(int visibility,int count) {
        for (int i = 0; i < count; ++i) {
            goodsImageArr[i].setVisibility(visibility);
            goodsPriceArr[i].setVisibility(visibility);
        }
    }

    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_banner_item,null);
        mImageView = view.findViewById(R.id.img_banner);

        imgDesktop = view.findViewById(R.id.img_goods_desktop);
        goodsImageArr[0] = view.findViewById(R.id.goods_image_left);
        goodsImageArr[1] = view.findViewById(R.id.goods_image_middle);
        goodsImageArr[2] = view.findViewById(R.id.goods_image_right);

        goodsPriceArr[0] = view.findViewById(R.id.tv_goods_price_left);
        goodsPriceArr[1] = view.findViewById(R.id.tv_goods_price_middle);
        goodsPriceArr[2] = view.findViewById(R.id.tv_goods_price_right);


        for (int i = 0; i < GOODS_IMAGE_COUNT; i++) {
            goodsImageArr[i].setOnClickListener(v -> {
                int commonId = (int) ((RoundedDataImageView) v).getCustomData();
                SLog.info("commonId[%d]", commonId);
                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            });
        }
        return view;
    }

    @Override
    public void onBind(Context context, int position, WebSliderItem webSliderItem) {
        // 数据绑定
        String imageUrl = StringUtil.normalizeImageUrl(webSliderItem.image);
        Glide.with(context).load(imageUrl).centerCrop().into(mImageView);
        //SLog.info("webSliderItem.linkType，[%s]",webSliderItem.linkType);

        imgDesktop.setVisibility(View.GONE);
        setGoodsImageVisibility(View.GONE,GOODS_IMAGE_COUNT);
        if(!webSliderItem.goodsCommons.equals("[]")){
            String goodsCommons = webSliderItem.goodsCommons;
            EasyJSONArray goodsArray = (EasyJSONArray) EasyJSONArray.parse(goodsCommons);
            //SLog.info("goodsArray%d",goodsArray.length());
            for (int i=0;i<goodsArray.length();i++) {
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

                    boolean noPrice = StringUtil.safeModel(goods)== Constant.GOODS_TYPE_CONSULT;
                    if (noPrice) {
                        UiUtil.toConsultUI(goodsPriceArr[i]);
                    } else {
                        goodsPriceArr[i].setText(StringUtil.formatPrice(context,price,0,false ));
                    }
                    goodsPriceArr[i].setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

                if (goodsArray.length() > 0) {
                    imgDesktop.setVisibility(View.VISIBLE);
                    setGoodsImageVisibility(View.VISIBLE,goodsArray.length());
                }
            }

        }
    }

}