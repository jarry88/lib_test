package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 【賣家】商品詳情頁
 * @author zwm
 */
public class SellerGoodsDetailFragment extends BaseFragment implements View.OnClickListener {
    int commonId;
    String goodsImageUrl;
    int twBlack;

    TextView tvGoodsVideoUrl;
    String goodsVideoUrl;

    public static SellerGoodsDetailFragment newInstance(int commonId, String goodsImageUrl) {
        Bundle args = new Bundle();

        SellerGoodsDetailFragment fragment = new SellerGoodsDetailFragment();
        fragment.setArguments(args);
        fragment.commonId = commonId;
        fragment.goodsImageUrl = goodsImageUrl;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        twBlack = _mActivity.getColor(R.color.tw_black);
        tvGoodsVideoUrl = view.findViewById(R.id.tv_goods_video_url);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_DETAIL + "/" + commonId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONObject goodsVo = responseObj.getSafeObject("datas.GoodsVo");
                    ((TextView) contentView.findViewById(R.id.tv_spu_id)).setText(String.valueOf(commonId));
                    ((TextView) contentView.findViewById(R.id.tv_goods_name)).setText(goodsVo.getSafeString("goodsName"));
                    ImageView goodsImage = contentView.findViewById(R.id.goods_image);
                    if (!StringUtil.isEmpty(goodsImageUrl)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImageUrl)).centerCrop().into(goodsImage);
                    }

                    int tariffEnable = goodsVo.getInt("tariffEnable");
                    SLog.info("tariffEnable__[%d]", tariffEnable);
                    contentView.findViewById(R.id.cross_border_indicator).setVisibility(tariffEnable == Constant.TRUE_INT ? View.VISIBLE : View.GONE);

                    String priceRange = String.format("%s MOP - %s MOP", StringUtil.formatFloat(goodsVo.getDouble("appPriceMin")), StringUtil.formatFloat(goodsVo.getDouble("batchPrice0")));
                    ((TextView) contentView.findViewById(R.id.tv_price_range)).setText(priceRange);

                    ((TextView) contentView.findViewById(R.id.tv_total_stock)).setText("總庫存: " + goodsVo.getInt("totalStorage"));
                    ((TextView) contentView.findViewById(R.id.tv_total_sale)).setText("銷量: " + goodsVo.getInt("goodsSaleNum"));
                    ((TextView) contentView.findViewById(R.id.tv_create_time)).setText(goodsVo.getSafeString("createTime"));

                    ((TextView) contentView.findViewById(R.id.tv_goods_category)).setText(goodsVo.getSafeString("categoryNames"));
                    ((TextView) contentView.findViewById(R.id.tv_goods_jingle)).setText(goodsVo.getSafeString("jingle"));
                    ((TextView) contentView.findViewById(R.id.tv_brand)).setText(goodsVo.getSafeString("brandName"));
                    ((TextView) contentView.findViewById(R.id.tv_brand_location)).setText(goodsVo.getSafeString("goodsCountryName"));
                    ((TextView) contentView.findViewById(R.id.tv_unit)).setText(goodsVo.getSafeString("unitName"));

                    double freightWeight = goodsVo.getDouble("freightWeight");
                    double freightVolume = goodsVo.getDouble("freightVolume");
                    ((TextView) contentView.findViewById(R.id.tv_goods_weight)).setText("重量：" + StringUtil.formatFloat(freightWeight) + "kg");
                    ((TextView) contentView.findViewById(R.id.tv_goods_weight)).setText("體積：" + StringUtil.formatFloat(freightVolume) + "m3");


                    LinearLayout llSpecContainer = contentView.findViewById(R.id.ll_spec_container);
                    EasyJSONArray specJsonVoList = goodsVo.getSafeArray("specJsonVoList");
                    for (Object object : specJsonVoList) {
                        EasyJSONObject specJsonVo = (EasyJSONObject) object;
                        StringBuilder specInfo = new StringBuilder();
                        specInfo.append("【" + specJsonVo.getSafeString("specName") + "】");

                        EasyJSONArray specValueList = specJsonVo.getSafeArray("specValueList");
                        for (Object object2 : specValueList) {
                            EasyJSONObject specValue = (EasyJSONObject) object2;
                            specInfo.append("   " + specValue.getSafeString("specValueName"));
                        }

                        TextView tvSpecInfo = new TextView(_mActivity);
                        tvSpecInfo.setText(specInfo.toString());
                        tvSpecInfo.setTextSize(13);
                        tvSpecInfo.setTextColor(twBlack);
                        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.topMargin = Util.dip2px(_mActivity, 10);
                        llSpecContainer.addView(tvSpecInfo, layoutParams);
                    }

                    goodsVideoUrl = goodsVo.getSafeString("goodsVideo");
                    if (!StringUtil.isEmpty(goodsVideoUrl)) {
                        tvGoodsVideoUrl.setText(Html.fromHtml("<u>" + goodsVideoUrl + "</u>"));
                        tvGoodsVideoUrl.setOnClickListener(SellerGoodsDetailFragment.this);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.tv_goods_video_url) {
            String videoId = Util.getYoutubeVideoId(goodsVideoUrl);

            if (!StringUtil.isEmpty(videoId)) {
                Util.playYoutubeVideo(_mActivity, videoId);
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

