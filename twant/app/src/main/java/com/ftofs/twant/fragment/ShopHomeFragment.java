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
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.lxj.xpopup.XPopup;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 店鋪首頁Fragment
 * @author zwm
 */
public class ShopHomeFragment extends BaseFragment implements View.OnClickListener {
    // 店鋪Id
    int shopId;

    TextView tvShopTitle;

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

    LinearLayout llFirstCommentContainer;

    public static ShopHomeFragment newInstance(int shopId) {
        Bundle args = new Bundle();

        args.putInt("shopId", shopId);
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

        Util.setOnClickListener(view, R.id.btn_menu, this);

        tvShopTitle = view.findViewById(R.id.tv_shop_title);

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

        llFirstCommentContainer = view.findViewById(R.id.ll_first_comment_container);

        try {
            Bundle args = getArguments();
            shopId = args.getInt("shopId");
            SLog.info("shopId[%d]", shopId);

            // 獲取店鋪首頁信息
            String path = Api.PATH_SHOP_HOME + "/" + shopId;
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate();
            if (StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseStr = response.body().string();
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        String shopName = responseObj.getString("datas.storeInfo.storeName");
                        tvShopTitle.setText(shopName);
                        String shopAvatarUrl = responseObj.getString("datas.storeInfo.storeAvatar");
                        // 店鋪頭像
                        Glide.with(ShopHomeFragment.this).load(shopAvatarUrl).into(imgShopAvatar);

                        // 店鋪簽名
                        tvShopSignature.setText(responseObj.getString("datas.storeInfo.storeSignature"));

                        // 開店天數
                        tvShopOpenDay.setText(getResources().getText(R.string.text_shop_open_day_prefix) +
                                responseObj.getString("datas.storeInfo.shopDay"));

                        // 店鋪形象圖
                        String shopFigureUrl = Config.OSS_BASE_URL + "/" + responseObj.getString("datas.storeInfo.storeFigureImage");
                        Glide.with(ShopHomeFragment.this).load(shopFigureUrl).into(imgShopFigure);

                        // 店鋪電話
                        tvPhoneNumber.setText(responseObj.getString("datas.storeInfo.chainPhone"));
                        // 營業時間
                        String businessTimeTemplate = getResources().getString(R.string.business_time_template);
                        String businessTime = String.format(businessTimeTemplate,
                                responseObj.getString("datas.storeInfo.weekDayStart").substring(0, 5),
                                responseObj.getString("datas.storeInfo.weekDayEnd").substring(0, 5),
                                responseObj.getString("datas.storeInfo.restDayStart").substring(0, 5),
                                responseObj.getString("datas.storeInfo.restDayEnd").substring(0, 5));
                        tvBusinessTime.setText(businessTime);

                        // 店鋪地址
                        String shopAddress = responseObj.getString("datas.storeInfo.chainAreaInfo") + responseObj.getString("datas.storeInfo.chainAddress");
                        tvShopAddress.setText(shopAddress);

                        // 社交分享
                        EasyJSONArray snsArray = responseObj.getArray("datas.socialList");
                        for (Object object : snsArray) {
                            EasyJSONObject snsObject = (EasyJSONObject) object;
                            String snsImageUrl = Config.OSS_BASE_URL + "/" + snsObject.getString("socialLogoChecked");
                            SLog.info("snsImageUrl[%s]", snsImageUrl);
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
                            SLog.info("payWayImageUrl[%s]", payWayImageUrl);
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
        if (id == R.id.btn_menu) {
            SLog.info("here");
            new XPopup.Builder(getContext())
                    .offsetX(-50)
                    .offsetY(-20)
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity))
                    .show();
        }
    }
}
