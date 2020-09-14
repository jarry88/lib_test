package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.PostDetailFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.fragment.ShoppingSessionFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 活動彈窗
 * @author zwm
 */
public class ActivityPopup extends CenterPopupView implements View.OnClickListener {
    ImageView imgContent;
    ImageView imgLoadingIndicator;
    ImageView btnClose;

    Context context;
    String appPopupAdImage;
    String appPopupAdLinkType;
    String appPopupAdLinkValue;


    public ActivityPopup(@NonNull Context context, String appPopupAdImage, String appPopupAdLinkType, String appPopupAdLinkValue) {
        super(context);

        this.context = context;
        this.appPopupAdImage = StringUtil.normalizeImageUrl(appPopupAdImage);
        this.appPopupAdLinkType = appPopupAdLinkType;
        this.appPopupAdLinkValue = appPopupAdLinkValue;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        imgContent = findViewById(R.id.img_content);
        imgContent.setOnClickListener(this);
        imgLoadingIndicator = findViewById(R.id.img_loading_indicator);
        Glide.with(context).load("file:///android_asset/loading.gif").into(imgLoadingIndicator);

        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        SLog.info("appPopupAdImage[%s]", appPopupAdImage);
        Glide.with(context).load(appPopupAdImage).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                btnClose.setVisibility(VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                SLog.info("onResourceReady__");
                imgLoadingIndicator.setVisibility(GONE);
                btnClose.setVisibility(VISIBLE);
                return false;
            }
        }).into(imgContent);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        try {
            if (id == R.id.img_content) {
                SLog.info("appPopupAdLinkType[%s], appPopupAdLinkValue[%s]", appPopupAdLinkType, appPopupAdLinkValue);
                if ("activity".equals(appPopupAdLinkType) || "url".equals(appPopupAdLinkType)) { // 打開活動主頁
                    Util.startFragment(H5GameFragment.newInstance(this.appPopupAdLinkValue, ""));
                } else if ("promotion".equals(appPopupAdLinkType)) { // 打開購物專場
                    SLog.info("跳轉到購物專場");
                    Util.startFragment(ShoppingSessionFragment.newInstance());
                } else if("shoppingZone".equals(appPopupAdLinkType)) {
                    int zoneId = Integer.parseInt(appPopupAdLinkValue);
                    Util.startFragment(NewShoppingSpecialFragment.newInstance(zoneId));
                }else if ("none".equals(appPopupAdLinkType)) { // 無操作
                    SLog.info("無操作");
                    return;
                } else if ("goods".equals(appPopupAdLinkType)) {
                    int commonId = Integer.parseInt(appPopupAdLinkValue);
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                } else if ("store".equals(appPopupAdLinkType)) {
                    int storeId = Integer.parseInt(appPopupAdLinkValue);
                    Util.startFragment(ShopMainFragment.newInstance(storeId));
                } else if ("category".equals(appPopupAdLinkType)) { // 商品分類
                    EasyJSONObject params = EasyJSONObject.generate(
                            "cat", appPopupAdLinkValue);
                    Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.toString(), params.toString()));
                } else if ("wantPost".equals(appPopupAdLinkType)) { // 想要圈
                    MainFragment mainFragment = MainFragment.getInstance();
                    if (mainFragment == null) {
                        ToastUtil.error(TwantApplication.getInstance(), "MainFragment為空");
                        return;
                    }
                    mainFragment.showHideFragment(MainFragment.CIRCLE_FRAGMENT);
                } else if ("postId".equals(appPopupAdLinkType)) {
                    int postId = Integer.parseInt(appPopupAdLinkValue);
                    Util.startFragment(PostDetailFragment.newInstance(postId));
                }
                dismiss();
            } else if (id == R.id.btn_close) {
                dismiss();
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }
}

