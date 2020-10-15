package com.ftofs.twant.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.DistributionPromotionGoods;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.WithdrawPopup;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 我的團隊頁面
 * @author zwm
 */
public class DistributionFragment extends BaseFragment implements View.OnClickListener {
    static final int TOOL_BTN_COUNT = 4;

    /*
    ic_distribution_my_team, R.id.ic_distribution_promoting_order,
            R.id.ic_distribution_withdraw_record, R.id.ic_distribution_promotion_goods
     */
    public static final int TAB_INDEX_MY_TEAM = 0;
    public static final int TAB_INDEX_PROMOTING_ORDER = 1;
    public static final int TAB_INDEX_WITHDRAW_RECORD = 2;
    public static final int TAB_INDEX_PROMOTION_GOODS = 3;

    // 一級Tab的切換的ViewPager偏移量
    public static final int VP_OFFSET_MY_TEAM = 0;
    public static final int VP_OFFSET_PROMOTING_ORDER = 3;
    public static final int VP_OFFSET_WITHDRAW_RECORD = 7;
    public static final int VP_OFFSET_PROMOTION_GOODS = 11;

    // 二級Tab切換的ViewPager內部偏移量
    int vpInternalOffsetMyTeam = 0;
    int vpInternalOffsetPromotingOrder = 0;
    int vpInternalOffsetWithdrawRecord = 0;
    int vpInternalOffsetPromotionGoods = 0;


    int currSelectedBtnIndex = 0;  // 當前選中的工具欄按鈕的索引
    int[] toolButtonIdArr = new int[] {R.id.btn_my_team, R.id.btn_promoting_order,
            R.id.btn_withdraw_record, R.id.btn_promotion_goods, };
    int[] toolBtnTextIdArr = new int[] {R.id.tv_my_team, R.id.tv_promoting_order,
            R.id.tv_withdraw_record, R.id.tv_promotion_goods, };
    int[] toolBtnIconIdArr = new int[] {R.id.ic_distribution_my_team, R.id.ic_distribution_promoting_order,
            R.id.ic_distribution_withdraw_record, R.id.ic_distribution_promotion_goods, };

    TextView[] tvToolBtnTextArr = new TextView[TOOL_BTN_COUNT];
    ImageView[] imgToolBtnIconArr = new ImageView[TOOL_BTN_COUNT];

    ViewPager viewPager;

    View myTeamTabContainer;
    View promotingOrderTabContainer;
    View withdrawRecordTabContainer;
    View promotionGoodsTabContainer;

    NestedScrollView containerView;
    int containerViewHeight = 0;
    LinearLayout llMyTeamToolbarContainer;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    TextView tvTotalCommissionAmount;
    TextView tvUnpaidCommissionAmount;

    public static DistributionFragment newInstance() {
        DistributionFragment fragment = new DistributionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distribution, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.view_pager);
        containerView = view.findViewById(R.id.container_view);
        llMyTeamToolbarContainer = view.findViewById(R.id.ll_my_team_toolbar_container);

        myTeamTabContainer = view.findViewById(R.id.my_team_tab_container);
        promotingOrderTabContainer = view.findViewById(R.id.promoting_order_tab_container);
        withdrawRecordTabContainer = view.findViewById(R.id.withdraw_record_tab_container);
        promotionGoodsTabContainer = view.findViewById(R.id.promotion_goods_tab_container);

        tvTotalCommissionAmount = view.findViewById(R.id.tv_total_comission_amount);
        tvUnpaidCommissionAmount = view.findViewById(R.id.tv_unpaid_commission_amount);

        titleList.add("");
        fragmentList.add(DistributionMemberFragment.newInstance(DistributionMemberFragment.REQUEST_TYPE_ALL));
        titleList.add("");
        fragmentList.add(DistributionMemberFragment.newInstance(DistributionMemberFragment.REQUEST_TYPE_FIRST_LEVEL));
        titleList.add("");
        fragmentList.add(DistributionMemberFragment.newInstance(DistributionMemberFragment.REQUEST_TYPE_SECOND_LEVEL));


        titleList.add("");
        fragmentList.add(DistributionOrderFragment.newInstance(DistributionOrderFragment.REQUEST_TYPE_ALL));
        titleList.add("");
        fragmentList.add(DistributionOrderFragment.newInstance(DistributionOrderFragment.REQUEST_TYPE_ONGOING));
        titleList.add("");
        fragmentList.add(DistributionOrderFragment.newInstance(DistributionOrderFragment.REQUEST_TYPE_FINISHED));
        titleList.add("");
        fragmentList.add(DistributionOrderFragment.newInstance(DistributionOrderFragment.REQUEST_TYPE_CANCELED));


        titleList.add("");
        fragmentList.add(DistributionWithdrawRecordFragment.newInstance(DistributionWithdrawRecordFragment.REQUEST_TYPE_ALL));
        titleList.add("");
        fragmentList.add(DistributionWithdrawRecordFragment.newInstance(DistributionWithdrawRecordFragment.REQUEST_TYPE_UNPROCESSED));
        titleList.add("");
        fragmentList.add(DistributionWithdrawRecordFragment.newInstance(DistributionWithdrawRecordFragment.REQUEST_TYPE_SUCCESS));
        titleList.add("");
        fragmentList.add(DistributionWithdrawRecordFragment.newInstance(DistributionWithdrawRecordFragment.REQUEST_TYPE_FAIL));


        titleList.add("");
        fragmentList.add(DistributionPromotionGoodsFragment.newInstance());



        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_withdraw, this);
        Util.setOnClickListener(view, R.id.btn_show_qr_code, this);

        for (int i = 0; i < toolButtonIdArr.length; i++) {
            int id = toolButtonIdArr[i];
            Util.setOnClickListener(view, id, this);
            tvToolBtnTextArr[i] = view.findViewById(toolBtnTextIdArr[i]);
            imgToolBtnIconArr[i] = view.findViewById(toolBtnIconIdArr[i]);
        }

        SimpleTabManager myTeamTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.stb_all) { // 全部
                    SLog.info("全部");
                    vpInternalOffsetMyTeam = 0;
                } else if (id == R.id.stb_first_level) { // 一級同伴
                    SLog.info("一級同伴");
                    vpInternalOffsetMyTeam = 1;
                } else if (id == R.id.stb_second_level) { // 二級同伴
                    SLog.info("二級同伴");
                    vpInternalOffsetMyTeam = 2;
                }
                viewPager.setCurrentItem(VP_OFFSET_MY_TEAM + vpInternalOffsetMyTeam);
            }
        };
        myTeamTabManager.add(view.findViewById(R.id.stb_all));
        myTeamTabManager.add(view.findViewById(R.id.stb_first_level));
        myTeamTabManager.add(view.findViewById(R.id.stb_second_level));
        myTeamTabManager.onSelect(view.findViewById(R.id.stb_all));  // 默認選中第1個

        SimpleTabManager orderTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.stb_promoting_order_all) { // 全部
                    SLog.info("全部");
                    vpInternalOffsetPromotingOrder = 0;
                } else if (id == R.id.stb_promoting_order_ongoing) { // 進行中
                    SLog.info("進行中");
                    vpInternalOffsetPromotingOrder = 1;
                } else if (id == R.id.stb_promoting_order_finished) { // 已完成
                    SLog.info("已完成");
                    vpInternalOffsetPromotingOrder = 2;
                } else if (id == R.id.stb_promoting_order_canceled) { // 已取消
                    SLog.info("已取消");
                    vpInternalOffsetPromotingOrder = 3;
                }
                viewPager.setCurrentItem(VP_OFFSET_PROMOTING_ORDER + vpInternalOffsetPromotingOrder);
            }
        };
        orderTabManager.add(view.findViewById(R.id.stb_promoting_order_all));
        orderTabManager.add(view.findViewById(R.id.stb_promoting_order_ongoing));
        orderTabManager.add(view.findViewById(R.id.stb_promoting_order_finished));
        orderTabManager.add(view.findViewById(R.id.stb_promoting_order_canceled));
        orderTabManager.onSelect(view.findViewById(R.id.stb_promoting_order_all));  // 默認選中第1個


        SimpleTabManager withdrawTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.stb_withdraw_all) { // 全部
                    SLog.info("全部");
                    vpInternalOffsetWithdrawRecord = 0;
                } else if (id == R.id.stb_withdraw_unprocessed) { // 未處理
                    SLog.info("未處理");
                    vpInternalOffsetWithdrawRecord = 1;
                } else if (id == R.id.stb_withdraw_success) { // 提現成功
                    SLog.info("提現成功");
                    vpInternalOffsetWithdrawRecord = 2;
                } else if (id == R.id.stb_withdraw_fail) { // 提現失敗
                    SLog.info("提現失敗");
                    vpInternalOffsetWithdrawRecord = 3;
                }
                viewPager.setCurrentItem(VP_OFFSET_WITHDRAW_RECORD + vpInternalOffsetWithdrawRecord);
            }
        };
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_all));
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_unprocessed));
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_success));
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_fail));
        withdrawTabManager.onSelect(view.findViewById(R.id.stb_withdraw_all));  // 默認選中第1個


        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);


        containerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int llMyTeamToolbarContainerY = Util.getYOnScreen(llMyTeamToolbarContainer);
                int containerViewY = Util.getYOnScreen(containerView);

//                SLog.info("rvPostListY[%s], containerViewY[%s]", rvPostListY, containerViewY);
                if (llMyTeamToolbarContainerY - Util.dip2px(_mContext, 11) <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
                    setNestedScrollingEnabled(true);
                } else {
                    setNestedScrollingEnabled(false);
                }
            }
        });

        loadData();
    }

    private void loadData() {
        String url = Api.PATH_MY_TEAM_HOME;

        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, "", "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, "", responseStr, "");
                        return;
                    }

                    double totalCommissionAmount = responseObj.optDouble("datas.marketingMember.commissionTotalAmount");
                    double unpaidCommissionAmount = responseObj.optDouble("datas.marketingMember.unpayCommission");
                    tvTotalCommissionAmount.setText("¥ " + StringUtil.formatFloat(totalCommissionAmount));
                    tvUnpaidCommissionAmount.setText("可提現金額：" + StringUtil.formatFloat(unpaidCommissionAmount) + "元");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 處理工具欄點擊
     * @param btnId
     * @return 如果已經處理，返回true；否則，返回false
     */
    private boolean handleToolbarClick(int btnId) {
        int i;
        for (i = 0; i < toolButtonIdArr.length; i++) {
            int id = toolButtonIdArr[i];
            if (id == btnId) {
                break;
            }
        }

        if (i == toolButtonIdArr.length) {
            return false;
        }

        // 取消之前的選中
        tvToolBtnTextArr[currSelectedBtnIndex].setTextColor(Util.getColor(R.color.tw_black));
        imgToolBtnIconArr[currSelectedBtnIndex].setImageTintList(ColorStateList.valueOf(Color.parseColor("#6B6B6B")));

        myTeamTabContainer.setVisibility(View.GONE);
        promotingOrderTabContainer.setVisibility(View.GONE);
        withdrawRecordTabContainer.setVisibility(View.GONE);
        promotionGoodsTabContainer.setVisibility(View.GONE);


        // 設置當前的選中
        currSelectedBtnIndex = i;
        tvToolBtnTextArr[currSelectedBtnIndex].setTextColor(Util.getColor(R.color.tw_blue));
        imgToolBtnIconArr[currSelectedBtnIndex].setImageTintList(ColorStateList.valueOf(Util.getColor(R.color.tw_blue)));
        if (currSelectedBtnIndex == TAB_INDEX_MY_TEAM) { // 我的團隊
            myTeamTabContainer.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(VP_OFFSET_MY_TEAM + vpInternalOffsetMyTeam);
        } else if (currSelectedBtnIndex == TAB_INDEX_PROMOTING_ORDER) { // 推介訂單
            promotingOrderTabContainer.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(VP_OFFSET_PROMOTING_ORDER + vpInternalOffsetPromotingOrder);
        } else if (currSelectedBtnIndex == TAB_INDEX_WITHDRAW_RECORD) { // 提現記錄
            withdrawRecordTabContainer.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(VP_OFFSET_WITHDRAW_RECORD + vpInternalOffsetWithdrawRecord);
        } else if (currSelectedBtnIndex == TAB_INDEX_PROMOTION_GOODS) { // 推介商品
            promotionGoodsTabContainer.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(VP_OFFSET_PROMOTION_GOODS);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_show_qr_code) { // 二維碼
            SLog.info("二維碼");
        } else if (id == R.id.btn_withdraw) { // 提現
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new WithdrawPopup(_mActivity, 2.56))
                    .show();
        }

        if (handleToolbarClick(id)) {
            return;
        }
    }


    private void setNestedScrollingEnabled(boolean enabled) {
        for (Fragment fragment : fragmentList) {
            // SLog.info("class[%s]", fragment.getClass().getName());
            ((NestedScrollingFragment) fragment).setNestedScrollingEnabled(enabled);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (containerViewHeight == 0) {
            containerViewHeight = containerView.getHeight();
            SLog.info("containerViewHeight[%d]", containerViewHeight);

            ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
            layoutParams.height = containerViewHeight - llMyTeamToolbarContainer.getHeight() - Util.dip2px(_mActivity, 11 + 8); // 11 和 8 分别是上下margin
            viewPager.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}


