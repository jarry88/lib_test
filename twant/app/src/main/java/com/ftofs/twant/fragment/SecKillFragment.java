package com.ftofs.twant.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;
import com.ftofs.twant.widget.SecKillLabel;

import org.greenrobot.eventbus.EventBus;

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
    LinearLayout llScheduleLabelContainer;

    TextView tvCountDownHour;
    TextView tvCountDownMinute;
    TextView tvCountDownSecond;

    CountDownTimer countDownTimer;
    int currentIndex = -1; // 当前选中的Index

    List<Long> endTimeList = new ArrayList<>();  // 活動結束的時間戳

    SecKillLabel prevLabel; // 上一次選中的Label
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

        llScheduleLabelContainer = view.findViewById(R.id.ll_schedule_label_container);

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

    /**
     * 选择秒殺活動場次
     * @param index
     */
    private void selectSecKill(int index) {
        if (index == currentIndex) {
            return;
        }

        currentIndex = index;

        if (countDownTimer != null) {
            countDownTimer.cancel(); // 取消上一次的倒計時
            countDownTimer = null;
        }

        long now = System.currentTimeMillis();
        long endTime = endTimeList.get(index);
        long remainTime = endTime - now;

        if (prevLabel != null) {
            prevLabel.setLabelSelected(false);
        }
        SecKillLabel currLabel = (SecKillLabel) llScheduleLabelContainer.getChildAt(index);
        currLabel.setLabelSelected(true);
        prevLabel = currLabel;
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
        // String url = Api.PATH_SEC_KILL_GOODS_LIST;
        String url = "https://test.weshare.team/tmp/test.json";
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

                    int secKillCount = 0;
                    EasyJSONArray scheduleList = responseObj.getSafeArray("datas.seckillScheduleList");
                    for (Object object : scheduleList) {
                        EasyJSONObject schedule = (EasyJSONObject) object;

                        int scheduleId = schedule.optInt("scheduleId");
                        String startTime = schedule.optString("startTime").substring(11, 16);
                        String scheduleStateText = schedule.optString("scheduleStateText");
                        String endTime = schedule.optString("endTime");
                        Jarbon jarbon = Jarbon.parse(endTime);
                        endTimeList.add(jarbon.getTimestampMillis());

                        SecKillLabel label = new SecKillLabel(_mActivity);
                        label.setTag(R.id.data_index, secKillCount);
                        label.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int index = (int) v.getTag(R.id.data_index);
                                SLog.info("index[%d]", index);
                                selectSecKill(index);
                            }
                        });

                        label.setData(scheduleId, startTime, scheduleStateText);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.weight = 1;
                        llScheduleLabelContainer.addView(label, layoutParams);

                        titleList.add(schedule.optString("scheduleName"));
                        fragmentList.add(SecKillGoodsListFragment.newInstance(schedule.optInt("scheduleId")));
                        secKillCount++;
                    }

                    // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
                    // ViewPager中Fragment不回調onCreateView的問題
                    CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
                    viewPager.setAdapter(adapter);

                    if (secKillCount > 0) {
                        selectSecKill(0);
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



