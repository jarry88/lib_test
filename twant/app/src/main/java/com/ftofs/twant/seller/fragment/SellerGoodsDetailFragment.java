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
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 【賣家】商品詳情頁
 * @author zwm
 */
public class SellerGoodsDetailFragment extends BaseFragment implements View.OnClickListener {
    public List<ListPopupItem> unitList = new ArrayList<>();
    public String unitName;
    public String detailVideo;

    List<ListPopupItem> spinnerLogoItems = new ArrayList<>();

    int commonId;
    String goodsImageUrl;
    int twBlack;

    TextView tvGoodsVideoUrl;
    String goodsVideoUrl;

    TextView btnViewGoodsDetail;
    EasyJSONArray mobileBodyVoList = EasyJSONArray.generate();

    LinearLayout llSpecContainer;
    LinearLayout btnEditBasicInfo;
    EasyJSONArray specJsonVoList;

    List<ListPopupItem> spinnerLogoCountryItems = new ArrayList<>();
    public EasyJSONObject goodsVo;
    LinearLayout btnEditTransactionInfo;
    private LinearLayout btnEditFreightInfo;
    private LinearLayout btnEditOtherInfo;
    public int allowTariff;
    public int joinBigSale;
    public double goodsFreight;
    public double freightWeight;
    public double freightVolume;
    public int freightTemplateId;
    public String storeLabelNames;
    private TextView tvGoodsDetailVideoUrl;
    private String goodsDetailVideoUrl;
    public String formatTopName;
    public String formatBottomName;
    public int isVirtual;
    public  int tariffEnable;
    public int goodsState;
    public int limitBuy;
    public  int goodsModal;//銷售模式 銷售模式 0零售 1跨城購 2虛擬
    private String freightTemplateName;
    public int businessType;//經營模式 1資訊模式(只允許發佈資訊類商品) 2交易模式(可發佈所有類型商品)



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
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        btnViewGoodsDetail = view.findViewById(R.id.btn_view_goods_detail);
        btnEditBasicInfo =view.findViewById(R.id.btn_edit_basic_info);
        btnEditTransactionInfo =view.findViewById(R.id.btn_edit_transaction_info);
        btnEditFreightInfo =view.findViewById(R.id.btn_seller_goods_freight_edit);
        btnEditOtherInfo =view.findViewById(R.id.btn_seller_goods_other_edit);
        twBlack = _mActivity.getColor(R.color.tw_black);
        tvGoodsVideoUrl = view.findViewById(R.id.tv_goods_video_url);
        tvGoodsDetailVideoUrl = view.findViewById(R.id.tv_introduction_video_url);

        Util.setOnClickListener(view, R.id.btn_edit_basic_info, this);
        Util.setOnClickListener(view, R.id.btn_edit_transaction_info, this);
        Util.setOnClickListener(view, R.id.btn_edit_spec, this);
        Util.setOnClickListener(view, R.id.btn_seller_goods_detail_edit, this);
        Util.setOnClickListener(view, R.id.btn_seller_goods_freight_edit, this);
        Util.setOnClickListener(view, R.id.btn_seller_goods_other_edit, this);
        llSpecContainer = view.findViewById(R.id.ll_spec_container);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadData();
        loadStoreInfo();
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
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        loadingPopup.show();
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    updateDataFromJson(responseObj);


                    ((TextView) contentView.findViewById(R.id.tv_spu_id)).setText(String.valueOf(commonId));
                    ((TextView) contentView.findViewById(R.id.tv_goods_name)).setText(goodsVo.getSafeString("goodsName"));
                    ImageView goodsImage = contentView.findViewById(R.id.goods_image);
                    if (!StringUtil.isEmpty(goodsImageUrl)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImageUrl)).centerCrop().into(goodsImage);
                    }

                    SLog.info("tariffEnable__[%d]", tariffEnable);
                    contentView.findViewById(R.id.cross_border_indicator).setVisibility(tariffEnable == Constant.TRUE_INT ? View.VISIBLE : View.GONE);
                    String priceRange;
                    if (StringUtil.safeModel(goodsVo) == Constant.GOODS_TYPE_CONSULT) {
                        priceRange = "問價";
                    } else {
                     priceRange= String.format("%s MOP - %s MOP", StringUtil.formatFloat(goodsVo.getDouble("appPriceMin")), StringUtil.formatFloat(goodsVo.getDouble("batchPrice0")));
                    }
                    ((TextView) contentView.findViewById(R.id.tv_price_range)).setText(priceRange);

                    ((TextView) contentView.findViewById(R.id.tv_total_stock)).setText("總庫存: " + goodsVo.getInt("totalStorage"));
                    ((TextView) contentView.findViewById(R.id.tv_total_sale)).setText("銷量: " + goodsVo.getInt("goodsSaleNum"));
                    ((TextView) contentView.findViewById(R.id.tv_create_time)).setText(goodsVo.getSafeString("createTime"));

                    ((TextView) contentView.findViewById(R.id.tv_goods_category)).setText(goodsVo.getSafeString("categoryNames"));
                    ((TextView) contentView.findViewById(R.id.tv_goods_jingle)).setText(goodsVo.getSafeString("jingle"));
                    ((TextView) contentView.findViewById(R.id.tv_brand)).setText(goodsVo.getSafeString("brandName"));
                    ((TextView) contentView.findViewById(R.id.tv_brand_location)).setText(goodsVo.getSafeString("goodsCountryName"));
                    ((TextView) contentView.findViewById(R.id.tv_top_name)).setText(formatTopName);
                    ((TextView) contentView.findViewById(R.id.tv_bottom_name)).setText(formatBottomName);
                    ((TextView) contentView.findViewById(R.id.tv_goods_category_in_store)).setText(storeLabelNames);
                    ((TextView) contentView.findViewById(R.id.tv_unit)).setText(unitName);

//                    ((TextView) contentView.findViewById(R.id.tv_sale_way)).setText(allowTariff == Constant.TRUE_INT ?
//                            (tariffEnable== Constant.TRUE_INT?"跨城購商品":(isVirtual== Constant.TRUE_INT?"虛擬商品" : "零售商品")):
//                            (isVirtual== Constant.TRUE_INT?"虛擬商品" : "零售商品"));
                    ((TextView) contentView.findViewById(R.id.tv_sale_way)).setText(goodsModal==5?"咨詢商品":goodsModal==2 ?"跨城購商品":goodsModal== 1?"虛擬商品" : "零售商品");

                    if (freightTemplateId > 0) {
                        ((TextView) contentView.findViewById(R.id.tv_freight)).setText("物流規則：");
                        ((TextView) contentView.findViewById(R.id.tv_goods_freight)).setText(freightTemplateName);


                    } else {
                        ((TextView) contentView.findViewById(R.id.tv_freight)).setText("固定運費：");
                        ((TextView) contentView.findViewById(R.id.tv_goods_freight)).setText("$ "+StringUtil.formatFloat(goodsFreight));

                    }
                    ((TextView) contentView.findViewById(R.id.tv_goods_weight)).setText("重量：" + StringUtil.formatFloat(freightWeight) + "kg");
                    ((TextView) contentView.findViewById(R.id.tv_goods_volume)).setText("體積：" + StringUtil.formatFloat(freightVolume) + "m3");
                    ((TextView) contentView.findViewById(R.id.tv_goods_participate_bargain)).setText(joinBigSale==1?"是":"否");
                    ((TextView) contentView.findViewById(R.id.tv_goods_publish_way)).setText(goodsState==1?"立即發佈":"放入倉庫");
                    int limitBuy = responseObj.getInt("datas.GoodsVo.limitBuy");
                    ((TextView) contentView.findViewById(R.id.tv_limit_buy)).setText(String.valueOf(limitBuy));

                    updateGoodsSpecView();

                    if (!StringUtil.isEmpty(goodsVideoUrl)) {
                        tvGoodsVideoUrl.setText(Html.fromHtml("<u>" + goodsVideoUrl + "</u>"));
                        tvGoodsVideoUrl.setOnClickListener(SellerGoodsDetailFragment.this);
                    }if (!StringUtil.isEmpty(goodsDetailVideoUrl)) {
                        tvGoodsDetailVideoUrl.setText(Html.fromHtml("<u>" + goodsDetailVideoUrl + "</u>"));
                        tvGoodsDetailVideoUrl.setOnClickListener(SellerGoodsDetailFragment.this);
                    }

                    btnViewGoodsDetail.setOnClickListener(SellerGoodsDetailFragment.this);

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    private void loadStoreInfo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_PUBLISH_PAGE ;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "isPublish",0
        );
        SLog.info("url[%s], params[%s]", url, params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        loadingPopup.show();
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);


                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    EasyJSONObject data = responseObj.getObject("datas");
                    businessType = data.getInt("businessType");

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    public void updateDataFromJson(EasyJSONObject responseObj) throws Exception{

        goodsVo = responseObj.getSafeObject("datas.GoodsVo");
        allowTariff = responseObj.getInt("datas.allowTariff");
        tariffEnable = goodsVo.getInt("tariffEnable");

        detailVideo=goodsVo.getSafeString("detailVideo");
        formatTopName=goodsVo.getSafeString("formatTopName");
        formatBottomName=goodsVo.getSafeString("formatBottomName");
        storeLabelNames = goodsVo.getSafeString("storeLabelNames");
        unitName = goodsVo.getSafeString("unitName");
        isVirtual = goodsVo.getInt("isVirtual");
        goodsModal = goodsVo.getInt("goodsModal");
        joinBigSale = goodsVo.getInt("joinBigSale");
        specJsonVoList = goodsVo.getSafeArray("specJsonVoList");

        goodsVideoUrl = goodsVo.getSafeString("goodsVideo");
        goodsDetailVideoUrl = goodsVo.getSafeString("detailVideo");
        mobileBodyVoList = goodsVo.getArray("mobileBodyVoList");
        explainFreight();
        goodsState = goodsVo.getInt("goodsState");
        limitBuy = goodsVo.getInt("limitBuy");

    }

    public void explainFreight() throws Exception{
        freightWeight = goodsVo.getDouble("freightWeight");
        freightVolume = goodsVo.getDouble("freightVolume");
        goodsFreight = goodsVo.getDouble("goodsFreight");
        freightTemplateId = goodsVo.getInt("freightTemplateId");
        freightTemplateName = goodsVo.getSafeString("freightTemplateName");

    }

    private void updateGoodsSpecView() {
        try {
            llSpecContainer.removeAllViews();

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
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
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
        } else if (id == R.id.btn_view_goods_detail) {
            Util.startFragment(SellerGoodsDetailViewerFragment.newInstance(mobileBodyVoList));
        }  else if (id == R.id.btn_edit_basic_info) {
            Util.startFragment(SellerEditBasicFragment.newInstance(this));
        }  else if (id == R.id.btn_edit_transaction_info) {
            Util.startFragment(SellerEditTransactionFragment.newInstance(this));
        } else if (id == R.id.btn_edit_spec) {
            start(SellerEditGoodsSpecFragment.newInstance(commonId, specJsonVoList));
        }  else if (id == R.id.btn_seller_goods_detail_edit) {
            Util.startFragment(SellerEditGoodsDetailFragment.newInstance(this));
        }   else if (id == R.id.btn_seller_goods_freight_edit) {
            Util.startFragment(SellerEditFreightFragment.newInstance(this));
        }  else if (id == R.id.btn_seller_goods_other_edit) {
            Util.startFragment(SellerEditOtherFragment.newInstance(this));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    public void saveGoodsInfo(EasyJSONObject publishGoodsInfo, SimpleCallback ui) {
        String path = Api.SELLER_GOODS_EDIT + "?token=" + User.getToken();
          SLog.info("path[%s]", path);
          SLog.info("paramas[%s]", publishGoodsInfo.toString());
          Api.postJsonUi(path, publishGoodsInfo.toString(), new UICallback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 LogUtil.uploadAppLog(path, publishGoodsInfo.toString(), "", e.getMessage());
                 ToastUtil.showNetworkError(_mActivity, e);
             }
         
             @Override
             public void onResponse(Call call, String responseStr) throws IOException {
                 try {
                     SLog.info("responseStr[%s]", responseStr);
         
                     EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                     if (ToastUtil.checkError(_mActivity, responseObj)) {
                         LogUtil.uploadAppLog(path, publishGoodsInfo.toString(), responseStr, "");
                         return;
                     }
//                     ToastUtil.success(_mActivity,responseObj.getString("datas.success"));
                     ui.onSimpleCall(responseObj.getString("datas.success"));
                 } catch (Exception e) {
                     SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                 }
             }
          });
    }
}

