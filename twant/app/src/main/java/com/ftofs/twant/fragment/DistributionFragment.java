package com.ftofs.twant.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.DistributionMemberAdapter;
import com.ftofs.twant.adapter.DistributionOrderAdapter;
import com.ftofs.twant.adapter.DistributionPromotionGoodsAdapter;
import com.ftofs.twant.adapter.DistributionWithdrawRecordAdapter;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.entity.DistributionOrderItem;
import com.ftofs.twant.entity.DistributionPromotionGoods;
import com.ftofs.twant.entity.DistributionWithdrawRecord;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.WithdrawPopup;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

        titleList.add("");
        fragmentList.add(MyTeamFragment.newInstance(MyTeamFragment.REQUEST_TYPE_ALL));
        titleList.add("");
        fragmentList.add(MyTeamFragment.newInstance(MyTeamFragment.REQUEST_TYPE_FIRST_LEVEL));
        titleList.add("");
        fragmentList.add(MyTeamFragment.newInstance(MyTeamFragment.REQUEST_TYPE_SECOND_LEVEL));




        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_withdraw, this);
        Util.setOnClickListener(view, R.id.btn_show_qr_code, this);

        for (int i = 0; i < toolButtonIdArr.length; i++) {
            int id = toolButtonIdArr[i];
            Util.setOnClickListener(view, id, this);
            tvToolBtnTextArr[i] = view.findViewById(toolBtnTextIdArr[i]);
            imgToolBtnIconArr[i] = view.findViewById(toolBtnIconIdArr[i]);
        }

        SimpleTabManager tabManager = new SimpleTabManager(0) {
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
                } else if (id == R.id.stb_first_level) { // 一級同伴
                    SLog.info("一級同伴");
                } else if (id == R.id.stb_second_level) { // 二級同伴
                    SLog.info("二級同伴");
                }
            }
        };
        tabManager.add(view.findViewById(R.id.stb_all));
        tabManager.add(view.findViewById(R.id.stb_first_level));
        tabManager.add(view.findViewById(R.id.stb_second_level));
        tabManager.onSelect(view.findViewById(R.id.stb_all));  // 默認選中第1個

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
                } else if (id == R.id.stb_promoting_order_ongoing) { // 進行中
                    SLog.info("進行中");
                } else if (id == R.id.stb_promoting_order_finished) { // 已完成
                    SLog.info("已完成");
                } else if (id == R.id.stb_promoting_order_canceled) { // 已取消
                    SLog.info("已取消");
                }
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
                } else if (id == R.id.stb_withdraw_unprocessed) { // 未處理
                    SLog.info("未處理");
                } else if (id == R.id.stb_withdraw_success) { // 提現成功
                    SLog.info("提現成功");
                } else if (id == R.id.stb_withdraw_fail) { // 提現失敗
                    SLog.info("提現失敗");
                }
            }
        };
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_all));
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_unprocessed));
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_success));
        withdrawTabManager.add(view.findViewById(R.id.stb_withdraw_fail));
        withdrawTabManager.onSelect(view.findViewById(R.id.stb_withdraw_all));  // 默認選中第1個


        SimpleTabManager goodsTabManager = new SimpleTabManager(0) {
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
                } else if (id == R.id.stb_withdraw_unprocessed) { // 未處理
                    SLog.info("未處理");
                } else if (id == R.id.stb_withdraw_success) { // 提現成功
                    SLog.info("提現成功");
                } else if (id == R.id.stb_withdraw_fail) { // 提現失敗
                    SLog.info("提現失敗");
                }
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
        } else if (currSelectedBtnIndex == TAB_INDEX_PROMOTING_ORDER) { // 推介訂單
            promotingOrderTabContainer.setVisibility(View.VISIBLE);
        } else if (currSelectedBtnIndex == TAB_INDEX_WITHDRAW_RECORD) { // 提現記錄
            withdrawRecordTabContainer.setVisibility(View.VISIBLE);
        } else if (currSelectedBtnIndex == TAB_INDEX_PROMOTION_GOODS) { // 推介商品
            promotionGoodsTabContainer.setVisibility(View.VISIBLE);
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
            SLog.info("class[%s]", fragment.getClass().getName());
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


