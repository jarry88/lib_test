package com.ftofs.twant.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.GoodsConformItem;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecPair;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.goods.GoodsMobileBodyVo;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.ftofs.twant.widget.StoreGiftPopup;
import com.ftofs.twant.widget.StoreVoucherPopup;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 商品詳情頁面
 * @author zwm
 */
public class GoodsDetailFragment extends BaseFragment implements View.OnClickListener {
    // 商品Id
    int commonId;
    // 當前選中的goodsId
    int currGoodsId;

    // 店鋪Id
    int storeId;
    // 購買數量
    int buyNum = 1;

    /**
     * 限時折扣倒計時是否正在倒數
     */
    boolean isCountingDown;
    int discountEndTime;
    Timer timer;

    ImageView goodsImage;
    TextView tvGoodsPrice;
    TextView tvGoodsPriceFinal;
    TextView tvGoodsPriceOriginal;
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

    List<CustomerServiceStaff> staffList = new ArrayList<>();

    List<Spec> specList = new ArrayList<>();
    // 從逗號連接的specValueId定位出goodsId的Map
    Map<String, Integer> specValueIdMap = new HashMap<>();

    List<SpecPair> specPairList = new ArrayList<>();

    LinearLayout llGoodsDetailImageContainer;

    TextView tvCurrentSpecs;
    // 當前選中的SpecValueId列表
    List<Integer> selSpecValueIdList = new ArrayList<>();

    RelativeLayout btnShowVoucher;
    TextView tvVoucherText;

    RelativeLayout btnShowConform;
    TextView tvConformText;

    RelativeLayout btnShowGift;
    TextView tvGiftText;

    TextView tvCountDownDay;
    TextView tvCountDownHour;
    TextView tvCountDownMinute;
    TextView tvCountDownSecond;

    RelativeLayout rlDiscountInfoContainer;
    RelativeLayout rlPriceTag;

    // 商品評論條數
    int commentCount = 0;
    TextView tvCommentCount;
    LinearLayout btnViewAllComment;
    LinearLayout llFirstCommentContainer;
    ImageView imgCommenterAvatar;
    TextView tvCommenterNickname;
    TextView tvComment;

    /**
     * goodsId與贈品列表的映射表
     */
    Map<Integer, List<GiftItem>> giftMap = new HashMap<>();
    List<GoodsConformItem> goodsConformItemList = new ArrayList<>();
    Map<Integer, GoodsInfo> goodsInfoMap = new HashMap<>();

    static class CountDownHandler extends Handler {
        WeakReference<GoodsDetailFragment> weakReference;

        public CountDownHandler(GoodsDetailFragment goodsDetailFragment) {
            weakReference = new WeakReference<>(goodsDetailFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TimeInfo timeInfo = (TimeInfo) msg.obj;
            GoodsDetailFragment goodsDetailFragment = weakReference.get();
            goodsDetailFragment.tvCountDownDay.setText("距結束 " + timeInfo.day + " 天");
            goodsDetailFragment.tvCountDownHour.setText(String.format("%02d", timeInfo.hour));
            goodsDetailFragment.tvCountDownMinute.setText(String.format("%02d", timeInfo.minute));
            goodsDetailFragment.tvCountDownSecond.setText(String.format("%02d", timeInfo.second));
        }
    }


    CountDownHandler countDownHandler;

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
        SLog.info("onViewCreated");
        countDownHandler = new CountDownHandler(this);
        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        commonId = args.getInt("commonId");
        SLog.info("commonId[%d]", commonId);

        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsJingle = view.findViewById(R.id.tv_goods_jingle);

        rlDiscountInfoContainer = view.findViewById(R.id.rl_discount_info_container);
        rlPriceTag = view.findViewById(R.id.rl_price_tag);

        tvGoodsPriceOriginal = view.findViewById(R.id.tv_goods_price_original);
        // 原價顯示刪除線
        tvGoodsPriceOriginal.setPaintFlags(tvGoodsPriceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tvGoodsPriceFinal = view.findViewById(R.id.tv_goods_price_final);

        tvCountDownDay = view.findViewById(R.id.tv_count_down_day);
        tvCountDownHour = view.findViewById(R.id.tv_count_down_hour);
        tvCountDownMinute = view.findViewById(R.id.tv_count_down_minute);
        tvCountDownSecond = view.findViewById(R.id.tv_count_down_second);

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

        Util.setOnClickListener(view, R.id.btn_goods_share, this);

        btnShowVoucher = view.findViewById(R.id.btn_show_voucher);
        btnShowVoucher.setOnClickListener(this);
        tvVoucherText = view.findViewById(R.id.tv_voucher_text);

        btnShowConform = view.findViewById(R.id.btn_show_conform);
        btnShowConform.setOnClickListener(this);
        tvConformText = view.findViewById(R.id.tv_conform_text);

        btnShowGift = view.findViewById(R.id.btn_show_gift);
        btnShowGift.setOnClickListener(this);
        tvGiftText = view.findViewById(R.id.tv_gift_text);

        tvCommentCount = view.findViewById(R.id.tv_comment_count);
        btnViewAllComment = view.findViewById(R.id.btn_view_all_comment);
        btnViewAllComment.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_goods_comment, this);

        llFirstCommentContainer = view.findViewById(R.id.ll_first_comment_container);
        imgCommenterAvatar = view.findViewById(R.id.img_commenter_avatar);
        tvCommenterNickname = view.findViewById(R.id.tv_commenter_nickname);
        tvComment = view.findViewById(R.id.tv_comment);

        Util.setOnClickListener(view, R.id.btn_back_round, this);
        Util.setOnClickListener(view, R.id.btn_add_to_cart, this);
        Util.setOnClickListener(view, R.id.btn_buy, this);
        Util.setOnClickListener(view, R.id.btn_select_spec, this);
        Util.setOnClickListener(view, R.id.tv_ship_to, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_follow, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_shop, this);
        Util.setOnClickListener(view, R.id.btn_goto_cart, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_customer_service, this);

        String token = User.getToken();
        loadGoodsDetail(commonId, token);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        int userId = User.getUserId();

        switch (id) {
            case R.id.btn_back_round:
                pop();
                break;
            case R.id.btn_add_to_cart:
                if (userId > 0) {
                    showSpecSelectPopup(Constant.ACTION_ADD_TO_CART);
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_goto_cart:
                if (userId > 0) {
                    Util.startFragment(CartFragment.newInstance(true));
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_buy:
                if (userId > 0) {
                    showSpecSelectPopup(Constant.ACTION_BUY);
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_select_spec:
                showSpecSelectPopup(Constant.ACTION_SELECT_SPEC);
                break;
            case R.id.tv_ship_to:
                if (userId > 0) {
                    startForResult(AddrManageFragment.newInstance(), RequestCode.CHANGE_ADDRESS.ordinal());
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_bottom_bar_follow:
                switchFavoriteState();
                break;
            case R.id.btn_goods_thumb:
                switchThumbState();
                break;
            case R.id.btn_bottom_bar_shop:
                Util.startFragment(ShopMainFragment.newInstance(storeId));
                break;
            case R.id.btn_show_voucher:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreVoucherPopup(_mActivity, storeId))
                        .show();
                break;
            case R.id.btn_show_conform:
            case R.id.btn_show_gift:
                int tabId = StoreGiftPopup.TAB_ID_CONFORM;
                if (id == R.id.btn_show_gift) {
                    tabId = StoreGiftPopup.TAB_ID_GIFT;
                }
                List<GiftItem> giftItemList = giftMap.get(currGoodsId);
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreGiftPopup(_mActivity, tabId, giftItemList, goodsConformItemList))
                        .show();
                break;
            case R.id.btn_goods_share:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new SharePopup(_mActivity, storeId))
                        .show();
                break;
            case R.id.btn_view_all_comment:
            case R.id.btn_goods_comment:
                Util.startFragment(CommentListFragment.newInstance(commonId, Constant.COMMENT_CHANNEL_GOODS));
                break;
            case R.id.btn_bottom_bar_customer_service:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreCustomerServicePopup(_mActivity, storeId, staffList))
                        .show();
                break;
            default:
                break;
        }
    }

    /**
     * 商品關注/取消關注
     */
    private void switchFavoriteState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
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
                ToastUtil.showNetworkError(_mActivity, e);
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
                    updateFavoriteView();

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
            Util.showLoginFragment();
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
                ToastUtil.showNetworkError(_mActivity, e);
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
    private void updateFavoriteView() {
        if (isFavorite == Constant.ONE) {
            iconFollow.setImageResource(R.drawable.icon_follow_red);
            tvFollow.setText(R.string.text_followed);
        } else {
            iconFollow.setImageResource(R.drawable.icon_goods_follow_grey);
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
                .asCustom(new SpecSelectPopup(_mActivity, action, specList, specValueIdMap, selSpecValueIdList, buyNum, goodsInfoMap))
                .show();
    }

    private void loadGoodsDetail(final int commonId, String token) {
        final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_loading))
                .show();
        String path = Api.PATH_GOODS_DETAIL + "/" + commonId;
        SLog.info("path[%s]", path);
        EasyJSONObject params = EasyJSONObject.generate("token", token);

        Api.postUI(path, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

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
                    updateFavoriteView();

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
                        SLog.info("kkkkey[%s], vvvalue[%s]", mapItem.getString("specValueIds"), mapItem.getInt("goodsId"));
                        // 有些沒有規格的商品，只有一個goodsId，且specValueIds字符串為空串
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

                        String imageUrl = StringUtil.normalizeImageUrl(easyJSONObject.getString("value"));

                        ImageView imageView = new ImageView(_mActivity);
                        imageView.setAdjustViewBounds(true);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SLog.info("imageUrl[%s]", imageUrl);
                                start(ImageViewerFragment.newInstance(imageUrl));
                            }
                        });
                        Glide.with(llGoodsDetailImageContainer).load(imageUrl).into(imageView);
                        llGoodsDetailImageContainer.addView(imageView);
                    }


                    boolean first;
                    // 【領券】優惠
                    EasyJSONArray voucherList = responseObj.getArray("datas.goodsDetail.goodsDetailCouponVoList");
                    if (voucherList.length() > 0) {
                        first = true;
                        StringBuilder voucherText = new StringBuilder();
                        for (Object object : voucherList) {
                            if (!first) {
                                voucherText.append(" / ");
                            }
                            EasyJSONObject voucher = (EasyJSONObject) object;

                            voucherText.append(String.format("滿%d減%d", voucher.getInt("limitAmount"), voucher.getInt("couponPrice")));
                            first = false;
                        }

                        tvVoucherText.setText(voucherText);
                        btnShowVoucher.setVisibility(VISIBLE);
                    }

                    // 限時折扣
                    EasyJSONObject discount = goodsDetail.getObject("discount");
                    if (discount != null) {
                        // 表明有限時折扣活動
                        String endTime = discount.getString("endTime");
                        SLog.info("endTime[%s]", endTime);
                        Date date = DateTimeUtils.formatDate(endTime);
                        discountEndTime = (int) (date.getTime() / 1000);
                        SLog.info("discountEndTime[%d]", discountEndTime);
                    }

                    // 【贈品】優惠
                    first = true;
                    EasyJSONArray goodsInfoVoList = responseObj.getArray("datas.goodsDetail.goodsInfoVoList");
                    for (Object object : goodsInfoVoList) {
                        GoodsInfo goodsInfo = new GoodsInfo();

                        EasyJSONObject goodsInfoVo = (EasyJSONObject) object;
                        int goodsId = goodsInfoVo.getInt("goodsId");
                        EasyJSONArray giftVoList = goodsInfoVo.getArray("giftVoList");

                        List<GiftItem> giftItemList = new ArrayList<>();
                        for (Object object2 : giftVoList) {
                            EasyJSONObject giftVo = (EasyJSONObject) object2;

                            GiftItem giftItem = (GiftItem) EasyJSONBase.jsonDecode(GiftItem.class, giftVo.toString());
                            giftItemList.add(giftItem);
                        }
                        giftMap.put(goodsId, giftItemList);
                        // 默認選中第一項sku
                        if (first) {
                            currGoodsId = goodsId;
                            SLog.info("默認選中第一項sku, goodsId[%d]", goodsId);
                        }

                        goodsInfo.goodsId = goodsId;
                        goodsInfo.commonId = commonId;
                        goodsInfo.goodsFullSpecs = goodsInfoVo.getString("goodsFullSpecs");
                        goodsInfo.specValueIds = goodsInfoVo.getString("specValueIds");
                        goodsInfo.goodsPrice0 = (float) goodsInfoVo.getDouble("goodsPrice0");
                        goodsInfo.price = Util.getGoodsPrice(goodsInfoVo);
                        goodsInfo.imageSrc = goodsInfoVo.getString("imageSrc");
                        goodsInfo.goodsStorage = goodsInfoVo.getInt("goodsStorage");
                        goodsInfo.limitAmount = goodsInfoVo.getInt("limitAmount");
                        goodsInfo.unitName = goodsInfoVo.getString("unitName");

                        goodsInfoMap.put(goodsId, goodsInfo);

                        first = false;
                    }
                    // 初始化默認選擇
                    SLog.info("currGoodsId[%d]", currGoodsId);
                    selectSku(currGoodsId);


                    // 【滿減】優惠
                    EasyJSONArray conformList = responseObj.getArray("datas.goodsDetail.conformList");
                    if (conformList.length() > 0) {
                        first = true;
                        StringBuilder conformText = new StringBuilder();
                        for (Object object : conformList) {
                            if (!first) {
                                conformText.append(" / ");
                            }

                            EasyJSONObject conform = (EasyJSONObject) object;
                            conformText.append(conform.getString("shortRule"));

                            GoodsConformItem goodsConformItem = new GoodsConformItem();
                            goodsConformItem.conformId = conform.getInt("conformId");
                            goodsConformItem.startTime = conform.getString("startTime");
                            goodsConformItem.endTime = conform.getString("endTime");
                            goodsConformItem.limitAmount = conform.getInt("limitAmount");
                            goodsConformItem.conformPrice = conform.getInt("conformPrice");
                            goodsConformItem.isFreeFreight = conform.getInt("isFreeFreight");
                            goodsConformItem.templateId = conform.getInt("templateId");
                            goodsConformItem.templatePrice = conform.getInt("templatePrice");

                            goodsConformItemList.add(goodsConformItem);
                            first = false;
                        }

                        tvConformText.setText(conformText);
                        btnShowConform.setVisibility(VISIBLE);
                    }


                    commentCount = responseObj.getInt("datas.wantCommentVoInfoCount");
                    tvCommentCount.setText(String.format(getString(R.string.text_comment) + "(%d)", commentCount));

                    SLog.info("commentCount[%d]", commentCount);
                    if (commentCount > 0) {
                        // 如果有評論，顯示首條評論
                        EasyJSONObject wantCommentVoInfo = responseObj.getObject("datas.wantCommentVoInfoList[0]");

                        String commenterAvatarUrl = Config.OSS_BASE_URL + "/" + wantCommentVoInfo.getString("memberVo.avatar");
                        Glide.with(_mActivity).load(commenterAvatarUrl).centerCrop().into(imgCommenterAvatar);
                        tvCommenterNickname.setText(wantCommentVoInfo.getString("memberVo.nickName"));
                        tvComment.setText(wantCommentVoInfo.getString("content"));
                    } else {
                        // 如果沒有評論，隱藏相應的控件
                        btnViewAllComment.setVisibility(GONE);
                        llFirstCommentContainer.setVisibility(GONE);
                    }

                    // 獲取店鋪客服人員數據
                    EasyJSONArray storeServiceStaffList = responseObj.getArray("datas.storeServiceStaffList");
                    for (Object object : storeServiceStaffList) {
                        EasyJSONObject storeServiceStaff = (EasyJSONObject) object;

                        CustomerServiceStaff staff = new CustomerServiceStaff();
                        Util.packStaffInfo(staff, storeServiceStaff);

                        staffList.add(staff);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    private void showGiftHint(List<GiftItem> giftItemList) {
        StringBuilder giftText = new StringBuilder("買就送");
        for (GiftItem giftItem : giftItemList) {
            giftText.append(giftItem.goodsName);
            giftText.append(" ");
        }
        tvGiftText.setText(giftText);
        btnShowGift.setVisibility(VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SLog.info("onDestroyView");
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
                EasyJSONObject easyJSONObject = (EasyJSONObject) EasyJSONObject.parse((String) message.data);
                int goodsId = easyJSONObject.getInt("goodsId");
                buyNum = easyJSONObject.getInt("quantity");
                selectSku(goodsId);
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

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        // 從哪個Fragment返回
        String from = data.getString("from");
        SLog.info("requestCode[%d], resultCode[%d], from[%s]", requestCode, resultCode, from);
        if (AddrManageFragment.class.getName().equals(from) || AddAddressFragment.class.getName().equals(from)) {
            // 從地址管理Fragment返回 或 從地址添加Fragment返回
            boolean isNoAddress = data.getBoolean("isNoAddress", false); // 標記是否刪除了所有地址
            if (isNoAddress) {
                // tvShipTo.setText("");
                return;
            }

            // 上一級Fragment返回的地址項
            AddrItem addrItem = data.getParcelable("addrItem");
            if (addrItem == null) {
                // tvShipTo.setText("");
                return;
            }
            SLog.info("addrItem: %s", addrItem);
            tvShipTo.setText(addrItem.areaInfo);

            updateFreight(addrItem);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (discountEndTime > 0) {
            startCountDown();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("onSupportInvisible");
        stopCountDown();
    }

    private void startCountDown() {
        if (timer == null) {
            timer = new Timer();
        }

        if (isCountingDown) {
            return;
        }
        // 定时服务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long threadId = Thread.currentThread().getId();
                // SLog.info("threadId[%d]", threadId);

                TimeInfo timeInfo = Time.diff((int) (System.currentTimeMillis() / 1000), discountEndTime);
                if (timeInfo == null) {
                    return;
                }


                Message message = new Message();
                message.obj = timeInfo;

                if (countDownHandler != null) {
                    countDownHandler.sendMessage(message);
                }
            }
        }, 500, 1000);  // 0.5秒后启动，每隔1秒运行一次
    }

    private void stopCountDown() {
        isCountingDown = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 選擇當前Sku
     * @param goodsId
     */
    private void selectSku(int goodsId) {
        currGoodsId = goodsId;

        GoodsInfo goodsInfo = goodsInfoMap.get(goodsId);
        if (goodsInfo == null) {
            return;
        }

        List<GiftItem> giftItemList = giftMap.get(currGoodsId);
        if (giftItemList == null || giftItemList.size() == 0) { // 如果當前規格沒有贈品，則隱藏贈品優惠提示
            btnShowGift.setVisibility(GONE);
        } else {
            showGiftHint(giftItemList);
        }

        SLog.info("goodsInfo.specValueIds[%s]", goodsInfo.specValueIds);
        selSpecValueIdList = StringUtil.specValueIdsToList(goodsInfo.specValueIds);

        String fullSpecs = goodsInfo.goodsFullSpecs;
        tvCurrentSpecs.setText(fullSpecs);

        String imageSrc = goodsInfo.imageSrc;
        if (!StringUtil.isEmpty(imageSrc)) {
            Glide.with(_mActivity).load(imageSrc).centerCrop().into(goodsImage);
        }

        if (discountEndTime > 0 &&
                Math.abs(goodsInfo.goodsPrice0 - goodsInfo.price) > Constant.STORE_DISTANCE_THRESHOLD) {  // 原價與最終價格有差值才算是折扣活動
            showPriceTag(false);

            tvGoodsPriceFinal.setText(StringUtil.formatPrice(_mActivity, goodsInfo.price, 0));
            tvGoodsPriceOriginal.setText(StringUtil.formatPrice(_mActivity, goodsInfo.goodsPrice0, 0));
        } else {
            showPriceTag(true);

            tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, goodsInfo.price, 0));
        }
    }

    /**
     * 是顯示價格標簽還是顯示折扣活動信息
     * @param show
     */
    private void showPriceTag(boolean show) {
        if (show) {
            rlPriceTag.setVisibility(VISIBLE);
            rlDiscountInfoContainer.setVisibility(GONE);
            stopCountDown();
        } else {
            rlPriceTag.setVisibility(GONE);
            rlDiscountInfoContainer.setVisibility(VISIBLE);
            startCountDown();
        }
    }

    /**
     * 用戶切換了送貨目的地
     * 更新運費的顯示
     */
    private void updateFreight(AddrItem addrItem) {
        int areaId2 = 0;

        if (addrItem.areaIdList.size() >= 2) {
            areaId2 = addrItem.areaIdList.get(1);
        }

        if (areaId2 == 0) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId,
                "buyNum", buyNum,
                "areaId2", areaId2);
        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_REFRESH_FREIGHT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    float freightAmount = (float) responseObj.getDouble("datas.freight.freightAmount");
                    tvFreightAmount.setText(getString(R.string.text_freight) + String.format("%.2f", freightAmount));
                } catch (Exception e) {

                }
            }
        });
    }
}
