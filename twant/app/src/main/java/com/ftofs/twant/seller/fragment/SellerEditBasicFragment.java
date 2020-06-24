package com.ftofs.twant.seller.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 *
 * 需求
 * @author gzp
 */
public class SellerEditBasicFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvTitle;
    private int goodsModal=-1;//銷售模式 銷售模式 0零售 1跨城購 2虛擬 【必填】
    private EasyJSONArray unitList;
    private EasyJSONObject publishGoodsInfo= new EasyJSONObject();
    private EasyJSONArray specJsonVoList;
    private String unitName;
    private int commonId;
    SellerGoodsDetailFragment parent;

    public static SellerEditBasicFragment newInstance(SellerGoodsDetailFragment parent) {
        SellerEditBasicFragment fragment= new SellerEditBasicFragment();
        fragment.parent = parent;
        fragment.unitList = parent.unitList;
        fragment.specJsonVoList = parent.specJsonVoList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_add_good_basic_layout, container, false);
        Util.setOnClickListener(view,R.id.btn_back,this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        View view = getView();
        tvTitle = view.findViewById(R.id.tv_title);

        tvTitle.setText("基本信息");
        view.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);
        view.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);
        Util.setOnClickListener(view, R.id.tv_add_good_unit, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        TextView btnGoodCategory =view.findViewById(R.id.btn_select_category_id);
//        Util.setOnClickListener(view,R.id.sp_add_good_logo,this);
        EditText etName =view.findViewById(R.id.et_add_good_name);
        etName.setOnClickListener(v -> showSoftInput(view));
        btnGoodCategory.setOnClickListener(v -> {
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new StoreLabelPopup(_mActivity, PopupType.STORE_LABEL,this))
                    .show();
        });
        loadGoodsCountry(view);
    }

    private void loadGoodsCountry(View view) {
        if (!parent.spinnerLogoCountryItems.isEmpty()) {
            return;
        }
        EasyJSONObject params =EasyJSONObject.generate("token", User.getToken());
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_SELLER_QUERY_COUNTRY_ALL, params, new UICallback() {
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
                    EasyJSONArray countryList = responseObj.getArray("datas.countryList");
                    if (countryList != null) {
                        parent.spinnerLogoCountryItems.clear();
                        for (Object o : countryList) {
                            AdminCountry item = AdminCountry.parase(o);
                            parent.spinnerLogoCountryItems.add(new ListPopupItem(item.getCountryId(),item.getCountryCn(),item));
                        }
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_DETAIL + "/" + parent.commonId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );
        SLog.info("url[%s], params[%s]", url, params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        loadingPopup.show();
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
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

                    int tariffEnable = goodsVo.getInt("tariffEnable");
                    SLog.info("tariffEnable__[%d]", tariffEnable);
                    contentView.findViewById(R.id.cross_border_indicator).setVisibility(tariffEnable == Constant.TRUE_INT ? View.VISIBLE : View.GONE);

//                    String priceRange = String.format("%s MOP - %s MOP", StringUtil.formatFloat(goodsVo.getDouble("appPriceMin")), StringUtil.formatFloat(goodsVo.getDouble("batchPrice0")));
//                    ((TextView) contentView.findViewById(R.id.tv_price_range)).setText(priceRange);
//
//                    ((TextView) contentView.findViewById(R.id.tv_total_stock)).setText("總庫存: " + goodsVo.getInt("totalStorage"));
//                    ((TextView) contentView.findViewById(R.id.tv_total_sale)).setText("銷量: " + goodsVo.getInt("goodsSaleNum"));
//                    ((TextView) contentView.findViewById(R.id.tv_create_time)).setText(goodsVo.getSafeString("createTime"));
//
//                    ((TextView) contentView.findViewById(R.id.tv_goods_category)).setText(goodsVo.getSafeString("categoryNames"));
//                    ((TextView) contentView.findViewById(R.id.tv_goods_jingle)).setText(goodsVo.getSafeString("jingle"));
//                    ((TextView) contentView.findViewById(R.id.tv_brand)).setText(goodsVo.getSafeString("brandName"));
//                    ((TextView) contentView.findViewById(R.id.tv_brand_location)).setText(goodsVo.getSafeString("goodsCountryName"));
//                    ((TextView) contentView.findViewById(R.id.tv_unit)).setText(goodsVo.getSafeString("unitName"));
//
//                    int isVirtual = goodsVo.getInt("isVirtual");
//                    ((TextView) contentView.findViewById(R.id.tv_sale_way)).setText(isVirtual == Constant.TRUE_INT ? "虛擬商品" : "零售商品");
//
//                    double freightWeight = goodsVo.getDouble("freightWeight");
//                    double freightVolume = goodsVo.getDouble("freightVolume");
//                    ((TextView) contentView.findViewById(R.id.tv_goods_weight)).setText("重量：" + StringUtil.formatFloat(freightWeight) + "kg");
//                    ((TextView) contentView.findViewById(R.id.tv_goods_weight)).setText("體積：" + StringUtil.formatFloat(freightVolume) + "m3");
//
//                    specJsonVoList = goodsVo.getSafeArray("specJsonVoList");

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkBasicInfo() {
        if (StringUtil.isEmpty(unitName)) {
            ToastUtil.error(_mActivity,"請選擇計量單位");
            return false;
        }
        if (goodsModal < 0) {
            ToastUtil.error(_mActivity,"請選擇銷售模式");
            return false;
        }
        try{
            publishGoodsInfo.set("unitName", unitName);
            publishGoodsInfo.set("goodsModal", goodsModal);
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return true;
    }
    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
        if (id == R.id.btn_ok) {
            if (checkBasicInfo()) {
                parent.saveGoodsInfo(publishGoodsInfo);
            }
                
        }
        if (id == R.id.tv_add_good_unit) {
            SLog.info("添加單位");
        }

    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {

    }
}


