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
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import cn.snailpad.easyjson.json.JSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 店鋪首頁Fragment
 * @author zwm
 */
public class ShopHomeFragment extends BaseFragment implements View.OnClickListener {
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

    int storeId;

    int isFavorite;
    ImageView btnStoreFavorite;

    int isLike;
    ImageView btnStoreThumb;

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

        llFirstCommentContainer = view.findViewById(R.id.ll_first_comment_container);

        loadStoreData();
    }


    private void loadStoreData() {
        final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                .asLoading("正在加載")
                .show();

        try {
            // 獲取店鋪首頁信息
            String path = Api.PATH_SHOP_HOME + "/" + parentFragment.getShopId();
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
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
                        String shopName = storeInfo.getString("storeName");
                        parentFragment.setShopName(shopName);

                        String shopAvatarUrl = storeInfo.getString("storeAvatar");
                        // 店鋪頭像
                        Glide.with(ShopHomeFragment.this).load(shopAvatarUrl).into(imgShopAvatar);
                        // 將店鋪頭像設置到工具欄按鈕
                        parentFragment.setImgBottomBarShopAvatar(shopAvatarUrl);

                        // 店鋪簽名
                        tvShopSignature.setText(storeInfo.getString("storeSignature"));

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
                        tvPhoneNumber.setText(storeInfo.getString("chainPhone"));
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
                        String shopAddress = storeInfo.getString("chainAreaInfo") + storeInfo.getString("chainAddress");
                        tvShopAddress.setText(shopAddress);

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
            default:
                break;
        }
    }

    private void switchThumbState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
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
}
