package com.ftofs.twant.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.DistributionMemberAdapter;
import com.ftofs.twant.adapter.DistributionOrderAdapter;
import com.ftofs.twant.adapter.DistributionProfitDetailAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.DistributionMember;
import com.ftofs.twant.entity.DistributionOrderItem;
import com.ftofs.twant.entity.DistributionProfitDetail;
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
    static final int TOOL_BTN_COUNT = 5;

    RecyclerView rvList;
    DistributionMemberAdapter memberAdapter;
    List<DistributionMember> distributionMemberList = new ArrayList<>();

    DistributionOrderAdapter orderAdapter;
    List<DistributionOrderItem> distributionOrderList = new ArrayList<>();

    DistributionProfitDetailAdapter profitDetailAdapter;
    List<DistributionProfitDetail> distributionProfitDetailList = new ArrayList<>();


    int currSelectedBtnIndex = 0;  // 當前選中的工具欄按鈕的索引
    int[] toolButtonIdArr = new int[] {R.id.btn_my_team, R.id.btn_promoting_order, R.id.btn_profit_detail,
            R.id.btn_withdraw_record, R.id.btn_promotion_goods, };
    int[] toolBtnTextIdArr = new int[] {R.id.tv_my_team, R.id.tv_promoting_order, R.id.tv_profit_detail,
            R.id.tv_withdraw_record, R.id.tv_promotion_goods, };
    int[] toolBtnIconIdArr = new int[] {R.id.ic_distribution_my_team, R.id.ic_distribution_promoting_order, R.id.ic_distribution_profit_detail,
            R.id.ic_distribution_withdraw_record, R.id.ic_distribution_promotion_goods, };

    TextView[] tvToolBtnTextArr = new TextView[TOOL_BTN_COUNT];
    ImageView[] imgToolBtnIconArr = new ImageView[TOOL_BTN_COUNT];

    View vwSeparator;

    View myTeamTabContainer;
    View promotingOrderTabContainer;
    View withdrawRecordTabContainer;
    View promotionGoodsTabContainer;

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

        for (int i = 0; i < 10; i++) {
            distributionMemberList.add(new DistributionMember());
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        memberAdapter = new DistributionMemberAdapter(R.layout.distribution_member_item, distributionMemberList);
        rvList.setAdapter(memberAdapter);


        for (int i = 0; i < 10; i++) {
            distributionOrderList.add(new DistributionOrderItem());
        }
        orderAdapter = new DistributionOrderAdapter(R.layout.distribution_order_item, distributionOrderList);

        distributionProfitDetailList.add(new DistributionProfitDetail(Constant.ITEM_TYPE_HEADER));
        for (int i = 0; i < 10; i++) {
            distributionProfitDetailList.add(new DistributionProfitDetail(Constant.ITEM_TYPE_NORMAL));
        }
        profitDetailAdapter = new DistributionProfitDetailAdapter(distributionProfitDetailList);
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
        if (currSelectedBtnIndex == 0) { // 我的團隊
            vwSeparator.setVisibility(View.VISIBLE);
            myTeamTabContainer.setVisibility(View.VISIBLE);
            rvList.setAdapter(memberAdapter);
        } else if (currSelectedBtnIndex == 1) { // 推介訂單
            vwSeparator.setVisibility(View.VISIBLE);
            promotingOrderTabContainer.setVisibility(View.VISIBLE);
            rvList.setAdapter(orderAdapter);
        } else if (currSelectedBtnIndex == 2) { // 收益明細
            rvList.setAdapter(profitDetailAdapter);
        } else if (currSelectedBtnIndex == 3) { // 提現記錄
            vwSeparator.setVisibility(View.VISIBLE);
            withdrawRecordTabContainer.setVisibility(View.VISIBLE);
        } else if (currSelectedBtnIndex == 4) { // 推介商品
            vwSeparator.setVisibility(View.VISIBLE);
            promotionGoodsTabContainer.setVisibility(View.VISIBLE);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_show_qr_code) {

        } else if (id == R.id.btn_withdraw) {

        }

        if (handleToolbarClick(id)) {
            return;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
