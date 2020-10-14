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
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.DistributionMemberAdapter;
import com.ftofs.twant.adapter.DistributionOrderAdapter;
import com.ftofs.twant.adapter.DistributionProfitDetailAdapter;
import com.ftofs.twant.adapter.DistributionPromotionGoodsAdapter;
import com.ftofs.twant.adapter.DistributionWithdrawRecordAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.entity.DistributionOrderItem;
import com.ftofs.twant.entity.DistributionProfitDetail;
import com.ftofs.twant.entity.DistributionPromotionGoods;
import com.ftofs.twant.entity.DistributionWithdrawRecord;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的團隊頁面
 * @author zwm
 */
public class MyTeamFragment extends BaseFragment implements View.OnClickListener {
    static final int TOOL_BTN_COUNT = 4;

    /*
    ic_distribution_my_team, R.id.ic_distribution_promoting_order,
            R.id.ic_distribution_withdraw_record, R.id.ic_distribution_promotion_goods
     */
    public static final int TAB_INDEX_MY_TEAM = 0;
    public static final int TAB_INDEX_PROMOTING_ORDER = 1;
    public static final int TAB_INDEX_WITHDRAW_RECORD = 2;
    public static final int TAB_INDEX_PROMOTION_GOODS = 3;

    RecyclerView rvList;
    DistributionMemberAdapter memberAdapter;
    List<DistributionMember> distributionMemberList = new ArrayList<>();

    DistributionOrderAdapter orderAdapter;
    List<DistributionOrderItem> distributionOrderList = new ArrayList<>();

    DistributionProfitDetailAdapter profitDetailAdapter;
    List<DistributionProfitDetail> distributionProfitDetailList = new ArrayList<>();

    DistributionWithdrawRecordAdapter withdrawRecordAdapter;
    List<DistributionWithdrawRecord> distributionWithdrawRecordList = new ArrayList<>();

    DistributionPromotionGoodsAdapter promotionGoodsAdapter;
    List<DistributionPromotionGoods> distributionPromotionGoodsList = new ArrayList<>();


    int currSelectedBtnIndex = 0;  // 當前選中的工具欄按鈕的索引
    int[] toolButtonIdArr = new int[] {R.id.btn_my_team, R.id.btn_promoting_order,
            R.id.btn_withdraw_record, R.id.btn_promotion_goods, };
    int[] toolBtnTextIdArr = new int[] {R.id.tv_my_team, R.id.tv_promoting_order,
            R.id.tv_withdraw_record, R.id.tv_promotion_goods, };
    int[] toolBtnIconIdArr = new int[] {R.id.ic_distribution_my_team, R.id.ic_distribution_promoting_order,
            R.id.ic_distribution_withdraw_record, R.id.ic_distribution_promotion_goods, };

    TextView[] tvToolBtnTextArr = new TextView[TOOL_BTN_COUNT];
    ImageView[] imgToolBtnIconArr = new ImageView[TOOL_BTN_COUNT];

    View vwSeparator;

    View myTeamTabContainer;
    View promotingOrderTabContainer;
    View withdrawRecordTabContainer;
    View promotionGoodsTabContainer;

    NestedScrollView containerView;
    int containerViewHeight = 0;
    LinearLayout llMyTeamToolbarContainer;

    public static MyTeamFragment newInstance() {
        MyTeamFragment fragment = new MyTeamFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_team, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        containerView = view.findViewById(R.id.container_view);
        llMyTeamToolbarContainer = view.findViewById(R.id.ll_my_team_toolbar_container);

        vwSeparator = view.findViewById(R.id.vw_separator);

        myTeamTabContainer = view.findViewById(R.id.my_team_tab_container);
        promotingOrderTabContainer = view.findViewById(R.id.promoting_order_tab_container);
        withdrawRecordTabContainer = view.findViewById(R.id.withdraw_record_tab_container);
        promotionGoodsTabContainer = view.findViewById(R.id.promotion_goods_tab_container);

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

        for (int i = 0; i < 20; i++) {
            distributionMemberList.add(new DistributionMember());
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setNestedScrollingEnabled(false);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        memberAdapter = new DistributionMemberAdapter(R.layout.distribution_member_item, distributionMemberList);
        rvList.setAdapter(memberAdapter);


        for (int i = 0; i < 20; i++) {
            distributionOrderList.add(new DistributionOrderItem());
        }
        orderAdapter = new DistributionOrderAdapter(R.layout.distribution_order_item, distributionOrderList);

        distributionProfitDetailList.add(new DistributionProfitDetail(Constant.ITEM_TYPE_HEADER));
        for (int i = 0; i < 20; i++) {
            distributionProfitDetailList.add(new DistributionProfitDetail(Constant.ITEM_TYPE_NORMAL));
        }
        profitDetailAdapter = new DistributionProfitDetailAdapter(distributionProfitDetailList);


        for (int i = 0; i < 20; i++) {
            distributionWithdrawRecordList.add(new DistributionWithdrawRecord());
        }
        withdrawRecordAdapter = new DistributionWithdrawRecordAdapter(R.layout.distribution_withdraw_record, distributionWithdrawRecordList);
        withdrawRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DistributionWithdrawRecord withdrawRecord = distributionWithdrawRecordList.get(position);
                if (!withdrawRecord.expanded) {
                    withdrawRecord.expanded = true;
                    withdrawRecordAdapter.notifyItemChanged(position);
                }
            }
        });


        for (int i = 0; i < 20; i++) {
            distributionPromotionGoodsList.add(new DistributionPromotionGoods());
        }
        promotionGoodsAdapter = new DistributionPromotionGoodsAdapter(R.layout.distribution_promotion_goods, distributionPromotionGoodsList);
        promotionGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DistributionPromotionGoods promotionGoods = distributionPromotionGoodsList.get(position);
                promotionGoods.selected = !promotionGoods.selected;
                promotionGoodsAdapter.notifyItemChanged(position);
            }
        });

        containerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int llMyTeamToolbarContainerY = Util.getYOnScreen(llMyTeamToolbarContainer);
                int containerViewY = Util.getYOnScreen(containerView);

//                SLog.info("rvPostListY[%s], containerViewY[%s]", rvPostListY, containerViewY);
                if (llMyTeamToolbarContainerY - Util.dip2px(_mContext, 11) <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
                    rvList.setNestedScrollingEnabled(true);
                } else {
                    rvList.setNestedScrollingEnabled(false);
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

        vwSeparator.setVisibility(View.GONE);
        myTeamTabContainer.setVisibility(View.GONE);
        promotingOrderTabContainer.setVisibility(View.GONE);
        withdrawRecordTabContainer.setVisibility(View.GONE);
        promotionGoodsTabContainer.setVisibility(View.GONE);


        // 設置當前的選中
        currSelectedBtnIndex = i;
        tvToolBtnTextArr[currSelectedBtnIndex].setTextColor(Util.getColor(R.color.tw_blue));
        imgToolBtnIconArr[currSelectedBtnIndex].setImageTintList(ColorStateList.valueOf(Util.getColor(R.color.tw_blue)));
        if (currSelectedBtnIndex == TAB_INDEX_MY_TEAM) { // 我的團隊
            vwSeparator.setVisibility(View.VISIBLE);
            myTeamTabContainer.setVisibility(View.VISIBLE);
            rvList.setBackgroundResource(R.drawable.white_r4dp_bg);
            rvList.setAdapter(memberAdapter);
        } else if (currSelectedBtnIndex == TAB_INDEX_PROMOTING_ORDER) { // 推介訂單
            vwSeparator.setVisibility(View.VISIBLE);
            promotingOrderTabContainer.setVisibility(View.VISIBLE);
            rvList.setBackground(null);
            rvList.setAdapter(orderAdapter);
        } else if (currSelectedBtnIndex == TAB_INDEX_WITHDRAW_RECORD) { // 提現記錄
            vwSeparator.setVisibility(View.VISIBLE);
            withdrawRecordTabContainer.setVisibility(View.VISIBLE);
            rvList.setBackground(null);
            rvList.setAdapter(withdrawRecordAdapter);
        } else if (currSelectedBtnIndex == TAB_INDEX_PROMOTION_GOODS) { // 推介商品
            vwSeparator.setVisibility(View.VISIBLE);
            promotionGoodsTabContainer.setVisibility(View.VISIBLE);
            rvList.setBackground(null);
            rvList.setAdapter(promotionGoodsAdapter);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_show_qr_code) { // 二維碼

        } else if (id == R.id.btn_withdraw) { // 提現

        }

        if (handleToolbarClick(id)) {
            return;
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (containerViewHeight == 0) {
            containerViewHeight = containerView.getHeight();
            SLog.info("containerViewHeight[%d]", containerViewHeight);

            ViewGroup.LayoutParams layoutParams = rvList.getLayoutParams();
            layoutParams.height = containerViewHeight - llMyTeamToolbarContainer.getHeight() - Util.dip2px(_mActivity, 11 + 8); // 11 和 8 分别是上下margin
            rvList.setLayoutParams(layoutParams);
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


