package com.ftofs.twant.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.fragment.StoreAboutFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static com.ftofs.twant.fragment.ShopHomeFragment.INTRODUCTION_FRAGMENT;

class StoreCardPopup extends CenterPopupView implements View.OnClickListener{
    private final Context context;
    private final int storeId;

    public StoreCardPopup(@NonNull Context context,int storeId) {
        super(context);
        this.context = context;
        this.storeId = storeId;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.store_instruction_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_ok).setOnClickListener(this);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
        String path = Api.STORE_CARD_DETAIL + "/" + storeId;
        LinearLayout llWhite = findViewById(R.id.ll_bottom_container);
        View llTop = findViewById(R.id.ll_top_view);
        View llBottom = findViewById(R.id.ll_bottom_container);
        LinearLayout llBlue = findViewById(R.id.top_container);
        changeViewHeightAnimatorStart(llTop,llTop.getHeight(),Util.dip2px(context,150));
        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responeStr[%s]",responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }
                try {
                    EasyJSONObject storeVo = responseObj.getObject("datas.storeVo");
                    ImageView avatar = findViewById(R.id.img_avatar);
                    TextView storeName = findViewById(R.id.tv_store_name);
                    storeName.setText(storeVo.getSafeString("storeName"));
//                    isLike = storeVo.getInt("isLike");
//                    isFavorite = storeVo.getInt("isFavorite");
                    // 營業時間
                    setWorkTime(storeVo.getSafeString("weekDayStart"), storeVo.getSafeString("weekDayStartTime"), storeVo.getSafeString("weekDayEnd"), storeVo.getSafeString("weekDayEndTime"),
                            storeVo.getSafeString("restDayStart"), storeVo.getSafeString("restDayStartTime"), storeVo.getSafeString("restDayEnd"), storeVo.getSafeString("restDayEndTime"));
                    // 商店電話
                    String storePhone = storeVo.getSafeString("chainPhone");
                    // 商店地址
                    String storeAddress = storeVo.getSafeString("chainAreaInfo") + storeVo.getSafeString("chainAddress");
                    // 商家介紹
//                    merchantIntroduction = storeVo.getSafeString("storeIntroduce");
//                    ((StoreAboutFragment) fragments.get(INTRODUCTION_FRAGMENT)).setIntroduction(merchantIntroduction); // 关于我们

                    setStoreContact(storePhone, storeAddress);
                    TextView tvFavoriteCount = findViewById(R.id.tv_popularity);
                    TextView tvLikeCount = findViewById(R.id.tv_fans_count);
                    TextView tvGoodsCount = findViewById(R.id.tv_goods_count);
                    int favoriteCount = storeVo.getInt("collectCount");
                    int likeCount = storeVo.getInt("likeCount");
                    int goodsCommonCount = storeVo.getInt("goodsCommonCount");
                    tvLikeCount.setText(String.valueOf(likeCount));
                    tvGoodsCount.setText(String.valueOf(goodsCommonCount));
                    tvFavoriteCount.setText(String.valueOf(favoriteCount));
                    String imgUrl =storeVo.getSafeString("storeAvatar");
                    Glide.with(context).load(StringUtil.normalizeImageUrl(imgUrl)).centerCrop().into(avatar);
                } catch (EasyJSONException e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

                }

            }
        });
    }
    public void setWorkTime(String weekDayStart, String weekDayStartTime, String weekDayEnd, String weekDayEndTime,
                            String restDayStart, String restDayStartTime, String restDayEnd, String restDayEndTime) {
        SLog.info("weekDayStart[%s], weekDayStartTime[%s], weekDayEnd[%s], weekDayEndTime[%s], restDayStart[%s], restDayStartTime[%s], restDayEnd[%s], restDayEndTime[%s]",
                weekDayStart, weekDayStartTime, weekDayEnd, weekDayEndTime, restDayStart, restDayStartTime, restDayEnd, restDayEndTime);

        String weekDayRange = weekDayStart + "至" + weekDayEnd;
        String weekDayRangeTime = weekDayStartTime + "-" + weekDayEndTime;
        String restDayRange = restDayStart + "至" + restDayEnd;
        String restDayRangeTime = restDayStartTime + "-" + restDayEndTime;

        TextView tvBusinessTimeWeekend=findViewById(R.id.tv_business_time_weekend);
        TextView tvBusinessTimeWorkingDay=findViewById(R.id.tv_business_time_working_day);
        if (StringUtil.isEmpty(weekDayStart)) {
            tvBusinessTimeWeekend.setVisibility(View.GONE);
            tvBusinessTimeWorkingDay.setText(weekDayRangeTime);
        } else {
            tvBusinessTimeWorkingDay.setText(weekDayRange + "   " + weekDayRangeTime);
        }

        if (StringUtil.isEmpty(restDayStart)) {
            tvBusinessTimeWeekend.setVisibility(View.GONE);
        } else {
            tvBusinessTimeWeekend.setText(restDayRange + "   " + restDayRangeTime);
        }
    }

    public void setStoreContact(String storePhone, String storeAddress) {
        TextView tvPhoneNumber = findViewById(R.id.tv_phone_number);
        TextView tvShopAddress = findViewById(R.id.tv_shop_address);
        tvPhoneNumber.setText(storePhone);
        tvShopAddress.setText(storeAddress);
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * .8f);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok || id == R.id.btn_close) {
            Util.startFragment(ShopMainFragment.newInstance(storeId));
            dismiss();
        }
    }
    public static void changeViewHeightAnimatorStart(final View view, final int startHeight, final int endHeight){

        if(view!=null&&startHeight>=0&&endHeight>=0){

            ValueAnimator animator=ValueAnimator.ofInt(startHeight,endHeight);

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override

                public void onAnimationUpdate(ValueAnimator animation) {

                    ViewGroup.LayoutParams params=view.getLayoutParams();

                    params.height= (int) animation.getAnimatedValue();

                    view.setLayoutParams(params);

                }

            });
            animator.setDuration(800);
            animator.start();

        }

    }
}
