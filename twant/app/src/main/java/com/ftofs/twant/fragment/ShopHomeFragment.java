package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreGoodsListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.entity.StoreGoodsItem;
import com.ftofs.twant.entity.StoreGoodsPair;
import com.ftofs.twant.entity.StoreMapInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AmapPopup;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.MerchantIntroductionPopup;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.StoreAnnouncementPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * 店鋪首頁Fragment
 * @author zwm
 */
public class ShopHomeFragment extends BaseFragment implements View.OnClickListener, AutoVerticalScrollTextViewUtil.OnMyClickListener {
    ShopMainFragment parentFragment;

    ImageView imgShopAvatar;
    TextView tvShopSignature;
    TextView tvShopOpenDay;
    ImageView imgShopFigure;

    TextView tvPhoneNumber;
    TextView tvBusinessTime;
    TextView tvShopAddress;

    LinearLayout llSnsContainer;
    LinearLayout llPayWayContainer;


    TextView tvCommentSummary;
    ImageView imgAuthorAvatar;
    TextView tvAuthorNickname;
    TextView tvCommentContent;

    TextView tvLikeCount;
    TextView tvFavoriteCount;

    LinearLayout llFirstCommentContainer;
    LinearLayout llShopAnnouncementContainer;


    int storeId;
    double storeDistance;  // 我與店鋪的距離
    String storeName;
    String storePhone;
    String storeAddress;
    double storeLongitude;
    double storeLatitude;

    int isFavorite;
    ImageView btnStoreFavorite;

    int isLike;
    ImageView btnStoreThumb;

    List<StoreGoodsPair> storeHotItemList = new ArrayList<>();
    List<StoreGoodsPair> storeNewInItemList = new ArrayList<>();
    LinearLayout llHotItemList;
    LinearLayout llNewInItemList;

    List<StoreAnnouncement> storeAnnouncementList = new ArrayList<>();
    private ArrayList<CharSequence> announcementTextList = new ArrayList<>();
    private AutoVerticalScrollTextViewUtil verticalScrollUtil;
    AutoVerticalScrollTextView tvVerticalScroll;

    Bundle savedInstanceState;
    String merchantIntroduction;

    public static ShopHomeFragment newInstance() {
        Bundle args = new Bundle();

        ShopHomeFragment fragment = new ShopHomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

        imgShopAvatar = view.findViewById(R.id.img_shop_avatar);
        imgShopAvatar.setOnClickListener(this);
        tvShopSignature = view.findViewById(R.id.tv_shop_signature);
        tvShopOpenDay = view.findViewById(R.id.tv_shop_open_day);
        imgShopFigure = view.findViewById(R.id.img_shop_figure);

        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        tvBusinessTime = view.findViewById(R.id.tv_business_time);
        tvShopAddress = view.findViewById(R.id.tv_shop_address);

        llSnsContainer = view.findViewById(R.id.ll_sns_container);
        llPayWayContainer = view.findViewById(R.id.ll_pay_way_container);
        tvCommentSummary = view.findViewById(R.id.tv_comment_summary);

        imgAuthorAvatar = view.findViewById(R.id.img_author_avatar);
        tvAuthorNickname = view.findViewById(R.id.tv_author_nickname);
        tvCommentContent = view.findViewById(R.id.tv_comment_content);

        tvLikeCount = view.findViewById(R.id.tv_like_count);
        btnStoreThumb = view.findViewById(R.id.btn_store_thumb);
        btnStoreThumb.setOnClickListener(this);

        tvFavoriteCount = view.findViewById(R.id.tv_favorite_count);
        btnStoreFavorite = view.findViewById(R.id.btn_store_favorite);
        btnStoreFavorite.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.ll_uo_share_container, this);

        llFirstCommentContainer = view.findViewById(R.id.ll_first_comment_container);
        llHotItemList = view.findViewById(R.id.ll_hot_item_list);
        llNewInItemList = view.findViewById(R.id.ll_new_in_item_list);
        llShopAnnouncementContainer = view.findViewById(R.id.ll_shop_announcement_container);

        tvVerticalScroll = view.findViewById(R.id.tv_vertical_scroll);

        Util.setOnClickListener(view, R.id.btn_shop_map, this);
        Util.setOnClickListener(view, R.id.btn_view_all_comment, this);

        this.savedInstanceState = savedInstanceState;

        loadStoreData();
    }


    private void loadStoreData() {
        final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_loading))
                .show();

        try {
            // 獲取店鋪首頁信息
            String path = Api.PATH_SHOP_HOME + "/" + parentFragment.getStoreId();
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            SLog.info("path[%s], params[%s]", path, params.toString());
            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();

                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONObject storeInfo = responseObj.getObject("datas.storeInfo");

                        storeId = storeInfo.getInt("storeId");
                        storeName = storeInfo.getString("storeName");
                        parentFragment.setShopName(storeName);

                        String shopAvatarUrl = Config.OSS_BASE_URL + "/" + storeInfo.getString("storeAvatar");
                        SLog.info("storeAvatar__[%s]", shopAvatarUrl);
                        // 店鋪頭像
                        Glide.with(ShopHomeFragment.this).load(shopAvatarUrl).into(imgShopAvatar);
                        // 將店鋪頭像設置到工具欄按鈕
                        parentFragment.setImgBottomBarShopAvatar(shopAvatarUrl);

                        // 店鋪簽名
                        tvShopSignature.setText(storeInfo.getString("storeSignature"));

                        // 商家介紹
                        merchantIntroduction = storeInfo.getString("storeIntroduce");

                        // 開店天數
                        tvShopOpenDay.setText(getString(R.string.text_shop_open_day_prefix) + storeInfo.getString("shopDay"));

                        // 店鋪形象圖
                        String shopFigureUrl = Config.OSS_BASE_URL + "/" + storeInfo.getString("storeFigureImage");
                        Glide.with(ShopHomeFragment.this).load(shopFigureUrl).into(imgShopFigure);

                        tvLikeCount.setText(String.valueOf(storeInfo.getInt("likeCount")));
                        tvFavoriteCount.setText(String.valueOf(storeInfo.getInt("collectCount")));
                        isLike = storeInfo.getInt("isLike");
                        isFavorite = storeInfo.getInt("isFavorite");
                        SLog.info("isLike[%d], isFavorite[%d]", isLike, isFavorite);

                        updateThumbView();
                        updateFavoriteView();

                        // 店鋪電話
                        storePhone = storeInfo.getString("chainPhone");
                        tvPhoneNumber.setText(storePhone);
                        // 營業時間
                        String businessTimeTemplate = getResources().getString(R.string.business_time_template);

                        String weekDayStart = getStoreBusinessTime(storeInfo, "weekDayStart");
                        String weekDayEnd = getStoreBusinessTime(storeInfo, "weekDayEnd");
                        String restDayStart = getStoreBusinessTime(storeInfo, "restDayStart");
                        String restDayEnd = getStoreBusinessTime(storeInfo, "restDayEnd");

                        String businessTime = String.format(businessTimeTemplate,
                                weekDayStart, weekDayEnd, restDayStart, restDayEnd);
                        tvBusinessTime.setText(businessTime);

                        // 店鋪地址
                        storeAddress = storeInfo.getString("chainAreaInfo") + storeInfo.getString("chainAddress");
                        tvShopAddress.setText(storeAddress);

                        storeLongitude = storeInfo.getDouble("lng");
                        storeLatitude = storeInfo.getDouble("lat");

                        String storeDistanceStr = storeInfo.getString("distance");
                        storeDistance = Double.valueOf(storeDistanceStr);


                        // 社交分享
                        EasyJSONArray snsArray = responseObj.getArray("datas.socialList");
                        for (Object object : snsArray) {
                            EasyJSONObject snsObject = (EasyJSONObject) object;
                            String snsImageUrl = Config.OSS_BASE_URL + "/" + snsObject.getString("socialLogoChecked");
                            // SLog.info("snsImageUrl[%s]", snsImageUrl);
                            ImageView snsImage = new ImageView(_mActivity);

                            LinearLayout.LayoutParams layoutParams =
                                    new LinearLayout.LayoutParams(Util.dip2px(_mActivity, 34), Util.dip2px(_mActivity, 34));
                            layoutParams.setMarginEnd(Util.dip2px(_mActivity, 15));
                            Glide.with(ShopHomeFragment.this).load(snsImageUrl).into(snsImage);
                            llSnsContainer.addView(snsImage, layoutParams);
                        }

                        // 支付方式
                        EasyJSONArray paymentArray = responseObj.getArray("datas.storePaymentList");
                        for (Object object : paymentArray) {
                            EasyJSONObject paymentObject = (EasyJSONObject) object;
                            String payWayImageUrl = Config.OSS_BASE_URL + "/" + paymentObject.getString("paymentLogo");
                            // SLog.info("payWayImageUrl[%s]", payWayImageUrl);
                            ImageView payWayImage = new ImageView(_mActivity);

                            LinearLayout.LayoutParams layoutParams =
                                    new LinearLayout.LayoutParams(Util.dip2px(_mActivity, 30), Util.dip2px(_mActivity, 21));
                            layoutParams.setMarginEnd(Util.dip2px(_mActivity, 15));
                            Glide.with(ShopHomeFragment.this).load(payWayImageUrl).into(payWayImage);
                            llPayWayContainer.addView(payWayImage, layoutParams);
                        }

                        // 評論條數
                        int commentCount = responseObj.getInt("datas.wantCommentVoInfoCount");
                        String commentSummary = getResources().getString(R.string.text_shop_comment) + "(" + commentCount + ")";
                        tvCommentSummary.setText(commentSummary);

                        if (commentCount > 0) { // 如果有評論，顯示第1條評論
                            // 取第1條評論
                            EasyJSONObject firstComment = responseObj.getObject("datas.wantCommentVoInfoList[0]");
                            SLog.info("firstComment[%s]", firstComment);

                            // 評論者的頭像
                            String authorAvatarUrl = Config.OSS_BASE_URL + "/" + firstComment.getString("memberVo.avatar");
                            SLog.info("authorAvatarUrl[%s]", authorAvatarUrl);
                            // 評論者的昵稱
                            String authorNickname = firstComment.getString("memberVo.nickName");
                            // 評論內容
                            String content = firstComment.getString("content");

                            Glide.with(ShopHomeFragment.this).load(authorAvatarUrl).into(imgAuthorAvatar);
                            tvAuthorNickname.setText(authorNickname);
                            tvCommentContent.setText(content);
                        } else { // 如果沒有評論，則隱藏相應的控件
                            llFirstCommentContainer.setVisibility(View.GONE);
                        }

                        // 最新商品
                        EasyJSONArray newGoodsVoList = responseObj.getArray("datas.newGoodsVoList");
                        int index = 0;
                        StoreGoodsPair storeGoodsPair = null;
                        for (Object object : newGoodsVoList) {
                            EasyJSONObject easyJSONObject = (EasyJSONObject) object;

                            StoreGoodsItem storeGoodsItem = new StoreGoodsItem();
                            storeGoodsItem.commonId = easyJSONObject.getInt("commonId");
                            storeGoodsItem.imageSrc = easyJSONObject.getString("imageSrc");
                            storeGoodsItem.goodsName = easyJSONObject.getString("goodsName");
                            storeGoodsItem.jingle = easyJSONObject.getString("jingle");
                            storeGoodsItem.price = Util.getGoodsPrice(easyJSONObject);

                            if (index % 2 == 0) {
                                storeGoodsPair = new StoreGoodsPair();
                                storeNewInItemList.add(storeGoodsPair);

                                storeGoodsPair.leftItem = storeGoodsItem;
                            } else {
                                storeGoodsPair.rightItem = storeGoodsItem;
                            }

                            ++index;
                        }

                        StoreGoodsListAdapter newInGoodsAdapter = new StoreGoodsListAdapter(_mActivity, llNewInItemList, R.layout.store_goods_list_item);
                        newInGoodsAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                                SLog.info("onClick");
                                StoreGoodsPair pair = storeNewInItemList.get(position);
                                int commonId;
                                int id = view.getId();
                                if (id == R.id.ll_left_item_container) {
                                    if (pair.leftItem == null) {
                                        return;
                                    }
                                    commonId = pair.leftItem.commonId;
                                } else {
                                    if (pair.rightItem == null) {
                                        return;
                                    }
                                    commonId = pair.rightItem.commonId;
                                }

                                Util.startFragment(GoodsDetailFragment.newInstance(commonId));
                            }
                        });
                        newInGoodsAdapter.setData(storeNewInItemList);


                        // 店鋪熱賣
                        EasyJSONArray hotGoodsVoList = responseObj.getArray("datas.commendGoodsVoList");
                        index = 0;
                        storeGoodsPair = null;
                        for (Object object : hotGoodsVoList) {
                            EasyJSONObject easyJSONObject = (EasyJSONObject) object;

                            StoreGoodsItem storeGoodsItem = new StoreGoodsItem();
                            storeGoodsItem.commonId = easyJSONObject.getInt("commonId");
                            storeGoodsItem.imageSrc = easyJSONObject.getString("imageSrc");
                            storeGoodsItem.goodsName = easyJSONObject.getString("goodsName");
                            storeGoodsItem.jingle = easyJSONObject.getString("jingle");
                            storeGoodsItem.price = Util.getGoodsPrice(easyJSONObject);

                            if (index % 2 == 0) {
                                storeGoodsPair = new StoreGoodsPair();
                                storeHotItemList.add(storeGoodsPair);

                                storeGoodsPair.leftItem = storeGoodsItem;
                            } else {
                                storeGoodsPair.rightItem = storeGoodsItem;
                            }

                            ++index;
                        }


                        StoreGoodsListAdapter hotGoodsAdapter = new StoreGoodsListAdapter(_mActivity, llHotItemList, R.layout.store_goods_list_item);
                        hotGoodsAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                                SLog.info("onClick");
                                StoreGoodsPair pair = storeHotItemList.get(position);
                                int commonId;
                                int id = view.getId();
                                if (id == R.id.ll_left_item_container) {
                                    if (pair.leftItem == null) {
                                        return;
                                    }
                                    commonId = pair.leftItem.commonId;
                                } else {
                                    if (pair.rightItem == null) {
                                        return;
                                    }
                                    commonId = pair.rightItem.commonId;
                                }

                                Util.startFragment(GoodsDetailFragment.newInstance(commonId));
                            }
                        });
                        hotGoodsAdapter.setData(storeHotItemList);

                        // 店鋪公告
                        EasyJSONArray announcements = responseObj.getArray("datas.announcements");
                        for (Object object : announcements) {
                            EasyJSONObject announcement = (EasyJSONObject) object;
                            String title = announcement.getString("announcementsTitle");
                            StoreAnnouncement storeAnnouncement = new StoreAnnouncement(
                                    announcement.getInt("id"), title);
                            storeAnnouncementList.add(storeAnnouncement);
                            announcementTextList.add(Html.fromHtml("<font color='#FFFFFF'>" + title + "</font>"));
                        }

                        // 初始化
                        verticalScrollUtil = new AutoVerticalScrollTextViewUtil(tvVerticalScroll, announcementTextList);
                        verticalScrollUtil.setDuration(3000)// 设置上下滚动時間间隔
                                .start();   // 如果只有一條，是否可以不調用start ?
                        // 点击事件监听
                        verticalScrollUtil.setOnMyClickListener(ShopHomeFragment.this);

                        // 如果沒有公告，則隱藏
                        if (storeAnnouncementList.size() < 1) {
                            llShopAnnouncementContainer.setVisibility(View.GONE);
                        }
                    } catch (EasyJSONException e) {
                        SLog.info("Error!%s", e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_store_thumb:
                switchThumbState();
                break;
            case R.id.btn_store_favorite:
                switchFavoriteState();
                break;
            case R.id.btn_shop_map:
                StoreMapInfo storeMapInfo = new StoreMapInfo(storeLongitude, storeLatitude, storeDistance, 0, 0,
                        storeName, storeAddress, storePhone);
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new AmapPopup(_mActivity, storeMapInfo, savedInstanceState))
                        .show();
                break;
            case R.id.img_shop_avatar:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new MerchantIntroductionPopup(_mActivity, merchantIntroduction))
                        .show();
                break;
            case R.id.ll_uo_share_container:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new SharePopup(_mActivity, storeId))
                        .show();
                break;
            case R.id.btn_view_all_comment:
                Util.startFragment(CommentListFragment.newInstance(storeId, Constant.COMMENT_CHANNEL_STORE));
                break;
            default:
                break;
        }
    }

    private void switchThumbState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId,
                "isLike", 1 - isLike,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);


        Api.postUI(Api.PATH_STORE_LIKE, params, new UICallback() {
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

                    int likeCount = responseObj.getInt("datas.likeCount");
                    tvLikeCount.setText(String.valueOf(likeCount));
                } catch (Exception e) {

                }
            }
        });
    }

    private void switchFavoriteState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);

        String path;
        if (isFavorite == Constant.ONE) {
            path = Api.PATH_STORE_FAVORITE_DELETE;
        } else {
            path = Api.PATH_STORE_FAVORITE_ADD;
        }

        SLog.info("path[%s], params[%s]", path, params);
        Api.postUI(path, params, new UICallback() {
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

    private void updateThumbView() {
        if (isLike == Constant.ONE) {
            btnStoreThumb.setImageResource(R.drawable.icon_store_thumb_red);
        } else {
            btnStoreThumb.setImageResource(R.drawable.icon_store_thumb_grey);
        }
    }

    private void updateFavoriteView() {
        if (isFavorite == Constant.ONE) {
            btnStoreFavorite.setImageResource(R.drawable.icon_store_favorite_red);
        } else {
            btnStoreFavorite.setImageResource(R.drawable.icon_store_favorite_grey);
        }
    }


    /**
     * 獲取店鋪的營業時間
     * @param responseObj
     * @param path
     * @return
     */
    private String getStoreBusinessTime(EasyJSONObject responseObj, String path) throws EasyJSONException {
        String timeStr = responseObj.getString(path);
        if (StringUtil.isEmpty(timeStr)) {
            return "";
        } else {
            return timeStr.substring(0, 5);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(verticalScrollUtil != null) {
            verticalScrollUtil.stop();
        }
    }

    @Override
    public void onMyClickListener(int i, CharSequence charSequence) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new StoreAnnouncementPopup(_mActivity, storeAnnouncementList))
                .show();
    }
}
