package com.ftofs.twant.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.BargainItem;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.widget.CountDownTimerViewHolder;

import java.util.List;

public class BargainListAdapter extends BaseMultiItemQuickAdapter<BargainItem, CountDownTimerViewHolder> {
    Context context;

    // 參考：
    // Android 用 RecyclerView 实现倒计时列表功能  https://juejin.im/entry/58d36be044d9040069239acd
    private SparseArray<CountDownTimer> countDownMap;  // 用于退出activity,避免countdown，造成资源浪费。

    public static final int MAX_AVATAR_COUNT = 3;

    // 三个头像控件的Id
    int[] avatarIdArr = new int[] {R.id.img_avatar_1, R.id.img_avatar_2, R.id.img_avatar_3};

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BargainListAdapter(Context context, List<BargainItem> data) {
        super(data);

        countDownMap = new SparseArray<>();
        this.context = context;

        addItemType(Constant.ITEM_TYPE_BANNER, R.layout.bargain_list_banner_item);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.bargain_list_normal_item);
    }

    @Override
    protected void convert(CountDownTimerViewHolder helper, BargainItem item) {
        int itemType = item.itemType;

        //将前一个缓存清除
        if (helper.countDownTimer != null) {
            helper.countDownTimer.cancel();
        }

        if (itemType == Constant.ITEM_TYPE_BANNER) {  // 頂部的Banner
            ImageView imgBanner = helper.getView(R.id.img_banner);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.bannerUrl)).centerCrop().into(imgBanner);
        } else if (itemType == Constant.ITEM_TYPE_NORMAL) {
            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);

            helper.setText(R.id.tv_goods_name, item.goodsName)
                    .setText(R.id.tv_goods_jingle, item.jingle);
            // 砍價榜
            if (item.bargainOpenList.size() < 1) {
                // 如果砍價榜沒內容，則不顯示
                helper.setVisible(R.id.ll_bargain_rank_list, false);
            } else {
                helper.setVisible(R.id.ll_bargain_rank_list, true);


                int count = item.bargainOpenList.size();
                // 显示前3个
                if (count > MAX_AVATAR_COUNT) {
                    count = MAX_AVATAR_COUNT;
                }

                int i = 0;
                for (; i < count; i++) {
                    String avatarUrl = item.bargainOpenList.get(i);
                    ImageView imgAvatar = helper.getView(avatarIdArr[i]);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
                    imgAvatar.setVisibility(View.VISIBLE);
                }

                // 如果不足3個，隱藏後面的ImageView
                for (; i < MAX_AVATAR_COUNT; i++) {
                    ImageView imgAvatar = helper.getView(avatarIdArr[i]);
                    imgAvatar.setVisibility(View.GONE);
                }
            }

            helper.setText(R.id.tv_bottom_price, StringUtil.formatPrice(context, item.bottomPrice, 0));

            if (item.bargainState == Constant.BARGAIN_STATE_NOT_STARTED) {  // 活動尚未開始
                helper.setGone(R.id.ll_count_down_container, true)
                    .setGone(R.id.btn_versatile, false);

                TextView tvCountDownDay = helper.getView(R.id.tv_count_down_day);
                TextView tvCountDownHour = helper.getView(R.id.tv_count_down_hour);
                TextView tvCountDownMinute = helper.getView(R.id.tv_count_down_minute);
                TextView tvCountDownSecond = helper.getView(R.id.tv_count_down_second);
                long now = System.currentTimeMillis();
                long remainTime = item.startTime - now;
                SLog.info("___startTime[%s], now[%s], remainTime[%s]", item.startTime, now, remainTime);
                if (remainTime > 0) {
                    helper.countDownTimer = new CountDownTimer(remainTime, 100) {
                        public void onTick(long millisUntilFinished) {
                            TimeInfo timeInfo = Time.diff((int) (System.currentTimeMillis() / 1000), (int) (item.startTime / 1000));
                            if (timeInfo != null) {
                                tvCountDownDay.setText(timeInfo.day + "天");
                                tvCountDownHour.setText(String.format("%d", timeInfo.hour));
                                tvCountDownMinute.setText(String.format("%02d", timeInfo.minute));
                                tvCountDownSecond.setText(String.format("%02d", timeInfo.second));
                            }
                        }
                        public void onFinish() {
                            // 倒計時結束
                        }
                    }.start();

                    countDownMap.put(tvCountDownHour.hashCode(), helper.countDownTimer);
                }
            } else {
                helper.setGone(R.id.ll_count_down_container, false)
                        .setGone(R.id.btn_versatile, true);
            }
        }
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }


        SLog.info("size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
