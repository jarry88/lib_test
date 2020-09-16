package com.ftofs.twant.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.GroupListItem;
import com.ftofs.twant.entity.TimeInfo;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.widget.CountDownTimerViewHolder;

import java.util.List;

/**
 * 團購列表Adapter
 * @author zwm
 */
public class GroupListAdapter extends BaseQuickAdapter<GroupListItem, CountDownTimerViewHolder> {
    // 參考：
    // Android 用 RecyclerView 实现倒计时列表功能  https://juejin.im/entry/58d36be044d9040069239acd
    private SparseArray<CountDownTimer> countDownMap;  // 用于退出activity,避免countdown，造成资源浪费。

    Context context;
    public GroupListAdapter(Context context, int layoutResId, @Nullable List<GroupListItem> data) {
        super(layoutResId, data);

        countDownMap = new SparseArray<>();
        this.context = context;
    }


    @Override
    protected void convert(CountDownTimerViewHolder helper, GroupListItem item) {
        helper.addOnClickListener(R.id.btn_join_group);

        ImageView imgMemberAvatar = helper.getView(R.id.img_member_avatar);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.memberAvatar)).centerCrop().into(imgMemberAvatar);

        // 尚需成团人数倒数
        int countDownMemberCount = item.requireNum - item.joinedNum;
        helper.setText(R.id.tv_count_down_member, countDownMemberCount + "人");

        //将前一个缓存清除
        if (helper.countDownTimer != null) {
            helper.countDownTimer.cancel();
        }

        TextView tvCountDownTime = helper.getView(R.id.tv_count_down_time);
        long remainTime = item.endTime - System.currentTimeMillis();
        if (remainTime > 0) {
            helper.setGone(R.id.btn_join_group, true);
            helper.countDownTimer = new CountDownTimer(remainTime, 100) {
                public void onTick(long millisUntilFinished) {
                    TimeInfo timeInfo = Time.groupTimeDiff(System.currentTimeMillis(), item.endTime);
                    tvCountDownTime.setText(String.format("%d:%02d:%02d.%d", timeInfo.hour, timeInfo.minute, timeInfo.second, timeInfo.milliSecond / 100));
                }
                public void onFinish() {
                    tvCountDownTime.setText("結束");
                    helper.setGone(R.id.btn_join_group, false);
                }
            }.start();

            countDownMap.put(tvCountDownTime.hashCode(), helper.countDownTimer);
        } else {
            tvCountDownTime.setText("結束");
            helper.setGone(R.id.btn_join_group, false);
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


