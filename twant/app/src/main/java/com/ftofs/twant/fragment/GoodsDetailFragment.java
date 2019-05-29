package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecPair;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.goods.GoodsMobileBodyVo;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商品詳情頁面
 * @author zwm
 */
public class GoodsDetailFragment extends BaseFragment implements View.OnClickListener {
    // 商品Id
    int commonId;

    // 店鋪Id
    int storeId;

    ImageView goodsImage;
    TextView tvGoodsPrice;
    TextView tvGoodsName;
    TextView tvGoodsJingle;

    ImageView imgGoodsNationalFlag;
    TextView tvGoodsCountryName;
    TextView tvShipTo;
    TextView tvFreightAmount;
    TextView tvFansCount;
    TextView tvGoodsSale;

    ImageView iconFollow;
    TextView tvFollow;
    int isFavorite;  // 是否關注

    ImageView btnGoodsThumb;
    int isLike; // 是否點贊

    List<Spec> specList = new ArrayList<>();
    // 從逗號連接的specValueId定位出goodsId的Map
    Map<String, Integer> specValueIdMap = new HashMap<>();

    List<SpecPair> specPairList = new ArrayList<>();

    LinearLayout llGoodsDetailImageContainer;

    TextView tvCurrentSpecs;
    // 當前選中的SpecValueId列表
    List<Integer> selSpecValueIdList = new ArrayList<>();

    public static GoodsDetailFragment newInstance(int commonId) {
        Bundle args = new Bundle();

        args.putInt("commonId", commonId);
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        commonId = args.getInt("commonId");
        SLog.info("commonId[%d]", commonId);

        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsJingle = view.findViewById(R.id.tv_goods_jingle);

        tvFreightAmount = view.findViewById(R.id.tv_freight_amount);

        tvCurrentSpecs = view.findViewById(R.id.tv_current_specs);

        imgGoodsNationalFlag = view.findViewById(R.id.img_goods_national_flag);
        tvGoodsCountryName = view.findViewById(R.id.tv_goods_country_name);
        tvShipTo = view.findViewById(R.id.tv_ship_to);

        iconFollow = view.findViewById(R.id.icon_follow);
        tvFollow = view.findViewById(R.id.tv_follow);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvGoodsSale = view.findViewById(R.id.tv_goods_sale);

        llGoodsDetailImageContainer = view.findViewById(R.id.ll_goods_detail_image_container);
        btnGoodsThumb = view.findViewById(R.id.btn_goods_thumb);
        btnGoodsThumb.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back_round, this);
        Util.setOnClickListener(view, R.id.btn_add_to_cart, this);
        Util.setOnClickListener(view, R.id.btn_buy, this);
        Util.setOnClickListener(view, R.id.btn_select_spec, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_follow, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_shop, this);

        String token = User.getToken();
        if (!StringUtil.isEmpty(token)) {
            loadGoodsDetail(commonId, token);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back_round:
                pop();
                break;
            case R.id.btn_add_to_cart:
                showSpecSelectPopup(Constant.ACTION_ADD_TO_CART);
                break;
            case R.id.btn_buy:
                showSpecSelectPopup(Constant.ACTION_BUY);
                break;
            case R.id.btn_select_spec:
                showSpecSelectPopup(Constant.ACTION_SELECT_SPEC);
                break;
            case R.id.btn_bottom_bar_follow:
                switchFavState();
                break;
            case R.id.btn_goods_thumb:
                switchThumbState();
                break;
            case R.id.btn_bottom_bar_shop:
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(ShopMainFragment.newInstance(storeId));
                break;
            default:
                break;
        }
    }

    /**
     * 商品關注/取消關注
     */
    private void switchFavState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId,
                "state", 1 - isFavorite,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);


        Api.postUI(Api.PATH_GOODS_FAVORITE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    isFavorite = 1 - isFavorite;
                    updateFavView();

                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 商品點贊/取消點贊
     */
    private void switchThumbState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId,
                "state", 1 - isLike,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);


        Api.postUI(Api.PATH_GOODS_LIKE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    isLike = 1 - isLike;
                    updateThumbView();

                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 更新是否關注的顯示
     */
    private void updateFavView() {
        if (isFavorite == Constant.ONE) {
            iconFollow.setImageResource(R.drawable.icon_follow_red);
            tvFollow.setText(R.string.text_followed);
        } else {
            iconFollow.setImageResource(R.drawable.icon_follow);
            tvFollow.setText(R.string.text_follow);
        }
    }

    private void updateThumbView() {
        if (isLike == Constant.ONE) {
            btnGoodsThumb.setImageResource(R.drawable.icon_goods_thumb_red);
        } else {
            btnGoodsThumb.setImageResource(R.drawable.icon_goods_thumb_grey);
        }
    }

    private void showSpecSelectPopup(int action) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, action, specList, specValueIdMap))
                .show();
    }

    private void loadGoodsDetail(int commonId, String token) {
        String path = Api.PATH_GOODS_DETAIL + "/" + commonId;
        SLog.info("path[%s]", path);
        EasyJSONObject params = EasyJSONObject.generate("token", token);

        Api.postUI(path, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONObject goodsDetail = responseObj.getObject("datas.goodsDetail");
                    SLog.info("goodsDetail[%s]", goodsDetail);

                    String goodsImageUrl = goodsDetail.getString("imageSrc");
                    Glide.with(GoodsDetailFragment.this).load(goodsImageUrl).centerCrop().into(goodsImage);

                    tvGoodsName.setText(goodsDetail.getString("goodsName"));
                    tvGoodsJingle.setText(goodsDetail.getString("jingle"));

                    String goodsNationalFlagUrl = Config.OSS_BASE_URL + "/" + responseObj.getString("datas.goodsCountry.nationalFlag");
                    Glide.with(GoodsDetailFragment.this).load(goodsNationalFlagUrl).into(imgGoodsNationalFlag);

                    tvGoodsCountryName.setText(responseObj.getString("datas.goodsCountry.countryCn"));

                    String areaInfo = responseObj.getString("datas.address.areaInfo");
                    float freightAmount = (float) responseObj.getDouble("datas.freight.freightAmount");
                    tvShipTo.setText(areaInfo);

                    tvFreightAmount.setText(getString(R.string.text_freight) + String.format("%.2f", freightAmount));

                    float goodsPrice = Util.getGoodsPrice(goodsDetail);
                    tvGoodsPrice.setText(String.format("%.2f", goodsPrice));

                    // 是否点赞
                    isLike = goodsDetail.getInt("isLike");
                    updateThumbView();

                    // 是否關注
                    isFavorite = goodsDetail.getInt("isFavorite");
                    updateFavView();

                    // 月銷量
                    int goodsSaleNum = goodsDetail.getInt("goodsSaleNum");
                    // 粉絲數
                    int goodsFavorite = goodsDetail.getInt("goodsFavorite");
                    storeId = responseObj.getInt("datas.storeInfo.storeId");

                    tvFansCount.setText(getString(R.string.text_fans) + goodsFavorite);
                    tvGoodsSale.setText(getString(R.string.text_monthly_sale) + goodsSaleNum + getString(R.string.text_monthly_sale_unit));

                    // 下面開始組裝規格數據列表
                    EasyJSONArray specJson = goodsDetail.getArray("specJson");
                    SLog.info("specJson[%s]", specJson);
                    for (Object object : specJson) {
                        EasyJSONObject specObj = (EasyJSONObject) object;

                        Spec specItem = new Spec();

                        specItem.specId = specObj.getInt("specId");
                        specItem.specName = specObj.getString("specName");

                        EasyJSONArray specValueList = specObj.getArray("specValueList");

                        boolean first = true;
                        for (Object object2 : specValueList) {
                            EasyJSONObject specValue = (EasyJSONObject) object2;
                            int specValueId = specValue.getInt("specValueId");
                            String specValueName = specValue.getString("specValueName");
                            String imageSrc = specValue.getString("imageSrc");

                            SpecValue specValueItem = new SpecValue(specValueId, specValueName, imageSrc);
                            specItem.specValueList.add(specValueItem);

                            if (first) {
                                specPairList.add(new SpecPair(specItem.specName, specValueName));
                                first = false;
                            }
                        }
                        specList.add(specItem);
                    }

                    SLog.info("fullSpecs[%s]", Util.formatSpecString(specPairList));
                    tvCurrentSpecs.setText(Util.formatSpecString(specPairList));

                    String goodsSpecValues = goodsDetail.getString("goodsSpecValues");
                    EasyJSONArray goodsSpecValueArr = (EasyJSONArray) EasyJSONArray.parse(goodsSpecValues);
                    for (Object object : goodsSpecValueArr) {
                        EasyJSONObject mapItem = (EasyJSONObject) object;
                        specValueIdMap.put(mapItem.getString("specValueIds"), mapItem.getInt("goodsId"));
                    }

                    SLog.info("specList.size[%d]", specList.size());
                    for (Spec spec : specList) {
                        SLog.info("SPEC_DUMP[%s]", spec);
                    }

                    // 商品詳情圖片
                    EasyJSONArray easyJSONArray = responseObj.getArray("datas.goodsMobileBodyVoList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        GoodsMobileBodyVo goodsMobileBodyVo = new GoodsMobileBodyVo();
                        goodsMobileBodyVo.setType(easyJSONObject.getString("type"));
                        goodsMobileBodyVo.setValue(easyJSONObject.getString("value"));
                        goodsMobileBodyVo.setWidth(easyJSONObject.getInt("width"));
                        goodsMobileBodyVo.setHeight(easyJSONObject.getInt("height"));

                        ImageView imageView = new ImageView(_mActivity);
                        imageView.setAdjustViewBounds(true);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(llGoodsDetailImageContainer).load(easyJSONObject.getString("value")).into(imageView);
                        llGoodsDetailImageContainer.addView(imageView);
                    }
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_SELECT_SPECS) {
            // 選擇規則完成
            SLog.info("data[%s]", message.data);
            try {
                EasyJSONObject easyJSONObject = (EasyJSONObject) EasyJSONObject.parse(message.data);
                int goodsId = easyJSONObject.getInt("goodsId");
                String imageSrc = easyJSONObject.getString("imageSrc");

                selSpecValueIdList.clear();
                for (Object object : easyJSONObject.getArray("selSpecValueIdArr")) {
                    selSpecValueIdList.add((Integer) object);
                }

                String fullSpecs = getFullSpecs();
                tvCurrentSpecs.setText(fullSpecs);
                if (!StringUtil.isEmpty(imageSrc)) {
                    Glide.with(_mActivity).load(imageSrc).centerCrop().into(goodsImage);
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 根據當前選中的specValueId列表，生成規格字符串
     * @return
     */
    public String getFullSpecs() {
        int index = 0;
        List<SpecPair> specPairList = new ArrayList<>();
        for (Integer specValueId : selSpecValueIdList) {
            Spec spec = specList.get(index);

            String specValueName = "";
            for (SpecValue specValue : spec.specValueList) {
                if (specValueId == specValue.specValueId) {
                    specValueName = specValue.specValueName;
                    break;
                }
            }
            specPairList.add(new SpecPair(spec.specName, specValueName));
            ++index;
        }
        return Util.formatSpecString(specPairList);
    }
}
