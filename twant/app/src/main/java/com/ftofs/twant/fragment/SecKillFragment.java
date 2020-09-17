package com.ftofs.twant.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.SecKillZoneListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.SecKillZoneItem;
import com.ftofs.twant.entity.TimeInfo;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SecKillFragment extends BaseFragment implements View.OnClickListener {
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    boolean isDataLoaded = false;
    RecyclerView rvScheduleLabelList;
    SecKillZoneListAdapter secKillZoneListAdapter;
    List<SecKillZoneItem> secKillZoneItemList = new ArrayList<>();

    TextView tvCountDownDesc;
    TextView tvCountDownHour;
    TextView tvCountDownMinute;
    TextView tvCountDownSecond;

    CountDownTimer countDownTimer;
    int currentIndex = -1; // 当前选中的Index

    List<Long> endTimeList = new ArrayList<>();  // 活動結束的時間戳

    ViewPager viewPager;

    public static SecKillFragment newInstance() {
        Bundle args = new Bundle();

        SecKillFragment fragment = new SecKillFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sec_kill, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_category, this);
        Util.setOnClickListener(view, R.id.btn_more, this);

        rvScheduleLabelList = view.findViewById(R.id.rv_schedule_label_list);
        rvScheduleLabelList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        secKillZoneListAdapter = new SecKillZoneListAdapter(_mActivity, R.layout.sec_kill_label, secKillZoneItemList);
        secKillZoneListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("position[%d], currIndex[%d]", position, currentIndex);

                secKillZoneListAdapter.setSelectedIndex(position);
                if (currentIndex >= 0) { // 取消上一項的選中狀態
                    secKillZoneListAdapter.notifyItemChanged(currentIndex);
                }
                secKillZoneListAdapter.notifyItemChanged(position);

                selectSecKill(position);
            }
        });
        rvScheduleLabelList.setAdapter(secKillZoneListAdapter);

        tvCountDownDesc = view.findViewById(R.id.tv_count_down_desc);

        int twYellow = getResources().getColor(R.color.tw_yellow, null);
        Drawable countDownDrawable = BackgroundDrawable.create(twYellow, Util.dip2px(_mActivity, 2));
        tvCountDownHour = view.findViewById(R.id.tv_count_down_hour);
        tvCountDownHour.setBackground(countDownDrawable);
        tvCountDownMinute = view.findViewById(R.id.tv_count_down_minute);
        tvCountDownMinute.setBackground(countDownDrawable);
        tvCountDownSecond = view.findViewById(R.id.tv_count_down_second);
        tvCountDownSecond.setBackground(countDownDrawable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (!isDataLoaded) {
            loadData();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    private void updateCountDownDesc(int scheduleState) {
        if (scheduleState == 0) {
            tvCountDownDesc.setText("距活動開始");
        } else {
            tvCountDownDesc.setText("距活動結束");
        }
    }

    /**
     * 选择秒殺活動場次
     * @param index
     */
    private void selectSecKill(int index) {
        if (index == currentIndex) {
            return;
        }

        SecKillZoneItem item = secKillZoneItemList.get(index);
        updateCountDownDesc(item.scheduleState);

        currentIndex = index;

        if (countDownTimer != null) {
            countDownTimer.cancel(); // 取消上一次的倒計時
            countDownTimer = null;
        }

        long now = System.currentTimeMillis();
        long endTime = endTimeList.get(index);
        long remainTime = endTime - now;

        viewPager.setCurrentItem(index);

        if (remainTime <= 0) {  // 如果已經開始，則不需要倒計時
            tvCountDownHour.setText("00");
            tvCountDownMinute.setText("00");
            tvCountDownSecond.setText("00");
            return;
        }

        countDownTimer = new CountDownTimer(remainTime, 500) {
            public void onTick(long millisUntilFinished) {
                TimeInfo timeInfo = Time.countDownDiff(System.currentTimeMillis(), endTime);
                if (timeInfo == null) {
                    return;
                }

                tvCountDownHour.setText(String.format("%02d", timeInfo.hour));
                tvCountDownMinute.setText(String.format("%02d", timeInfo.minute));
                tvCountDownSecond.setText(String.format("%02d", timeInfo.second));
            }
            public void onFinish() {
                tvCountDownHour.setText("00");
                tvCountDownMinute.setText("00");
                tvCountDownSecond.setText("00");
            }
        }.start();
    }

    private void loadData() {
        String url = Api.PATH_SEC_KILL;
        // String url = "https://test.weshare.team/tmp/test.json";
        Api.postUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, "", "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(url, "", responseStr, "");
                    return;
                }

                try {
                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    viewPager = contentView.findViewById(R.id.vp_goods_list);


                    int defaultScheduleId = responseObj.optInt("datas.seckillSchedule.scheduleId"); // 【搶購中】狀態的場次Id
                    int defaultIndex = -1;  // 默認選中【搶購中】狀態的秒殺專場的位置索引
                    int secKillCount = 0;
                    EasyJSONArray scheduleList = responseObj.getSafeArray("datas.seckillScheduleList");
                    for (Object object : scheduleList) {
                        EasyJSONObject schedule = (EasyJSONObject) object;

                        int scheduleId = schedule.optInt("scheduleId");
                        int scheduleState = schedule.optInt("scheduleState");
                        String scheduleStateText = schedule.optString("scheduleStateText");
                        String startTime = schedule.optString("startTime");
                        String endTime = schedule.optString("endTime");

                        // 默認選中【搶購中】狀態的秒殺專場的位置索引
                        if (scheduleId == defaultScheduleId) {
                            defaultIndex = secKillCount;
                        }

                        Jarbon jarbon;
                        if (scheduleState == 0) { // 如果是【即將開場】，用開始時間作倒計時
                            jarbon = Jarbon.parse(startTime);
                        } else {
                            jarbon = Jarbon.parse(endTime);
                        }
                        endTimeList.add(jarbon.getTimestampMillis());

                        SecKillZoneItem item = new SecKillZoneItem();
                        item.scheduleId = scheduleId;
                        item.startTime = startTime.substring(11, 16);
                        item.statusText = scheduleStateText;
                        item.scheduleState = scheduleState;

                        secKillZoneItemList.add(item);

                        titleList.add(schedule.optString("scheduleName"));
                        fragmentList.add(SecKillGoodsListFragment.newInstance(scheduleId));
                        secKillCount++;
                    }
                    SLog.info("defaultIndex[%d]", defaultIndex);
                    if (defaultIndex >= 0) {
                        secKillZoneListAdapter.setSelectedIndex(defaultIndex);
                    }
                    secKillZoneListAdapter.setNewData(secKillZoneItemList);

                    // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
                    // ViewPager中Fragment不回調onCreateView的問題
                    CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
                    viewPager.setAdapter(adapter);

                    if (defaultIndex >= 0) {
                        selectSecKill(defaultIndex);

                        // 滾動到指定位置，【搶購中】狀態的置於中間
                        int targetPosition = defaultIndex - 2;
                        if (targetPosition < 0) {
                            targetPosition = 0;
                        }
                        while (secKillCount - targetPosition < 5 && targetPosition > 0) {
                            targetPosition--;
                        }
                        if (targetPosition > 0) {
                            rvScheduleLabelList.scrollToPosition(targetPosition);
                        }
                    }

                    isDataLoaded = true;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_category) {
            SLog.info("btn_category");
        } else if (id == R.id.btn_more) {
            SLog.info("btn_more");
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}




