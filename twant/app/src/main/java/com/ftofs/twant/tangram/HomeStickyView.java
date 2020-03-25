package com.ftofs.twant.tangram;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.entity.StickyCellData;
import com.ftofs.twant.fragment.CategoryFragment;
import com.ftofs.twant.fragment.CircleFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShoppingSessionFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import cn.snailpad.easyjson.EasyJSONObject;

public class HomeStickyView extends LinearLayout implements ITangramViewLifeCycle, View.OnClickListener {
    Context context;

    TextView tvStoreCount;
    TextView tvGoodsCount;
    TextView tvPostCount;

    ImageView iconActivityEntrance;
    View vwActivityEntrancePlaceholder;
    View btnGotoActivity;

    BaseCell cell;

    public HomeStickyView(Context context) {
        this(context, null);
    }

    public HomeStickyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeStickyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        View contentView = LayoutInflater.from(context).inflate(R.layout.tangram_layout_home_sticky_view, this, false);
        contentView.findViewById(R.id.ll_search_box).setOnClickListener(this);
        contentView.findViewById(R.id.btn_category_store).setOnClickListener(this);
        contentView.findViewById(R.id.btn_category_goods).setOnClickListener(this);
        contentView.findViewById(R.id.btn_category_brand).setOnClickListener(this);

        tvStoreCount = contentView.findViewById(R.id.tv_store_count);
        tvGoodsCount = contentView.findViewById(R.id.tv_goods_count);
        tvPostCount = contentView.findViewById(R.id.tv_post_count);

        iconActivityEntrance = contentView.findViewById(R.id.icon_activity_entrance);
        vwActivityEntrancePlaceholder = contentView.findViewById(R.id.vw_activity_entrance_placeholder);
        btnGotoActivity = contentView.findViewById(R.id.btn_goto_activity);

        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {
        this.cell = cell;
    }

    @Override
    public void postBindView(BaseCell cell) {
        SLog.info("HomeStickyView::postBindView");
        Object data = cell.optParam("data");
        if (data != null) {
            StickyCellData stickyCellData = (StickyCellData) data;
            tvStoreCount.setText(formatCount(stickyCellData.storeCount));
            tvStoreCount.setVisibility(VISIBLE);
            tvGoodsCount.setText(formatCount(stickyCellData.goodsCommonCount));
            tvGoodsCount.setVisibility(VISIBLE);
            tvPostCount.setText(formatCount(stickyCellData.wantPostCount));
            tvPostCount.setVisibility(VISIBLE);

            vwActivityEntrancePlaceholder.setVisibility(stickyCellData.activityEnable ? View.VISIBLE : View.GONE);
            btnGotoActivity.setVisibility(stickyCellData.activityEnable ? View.VISIBLE : View.GONE);
            if (stickyCellData.activityEnable) {
                Glide.with(getContext()).load(StringUtil.normalizeImageUrl(stickyCellData.appIndexNavigationImage))
                        .into(iconActivityEntrance);
                btnGotoActivity.setOnClickListener(this);
            }
        }
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }

    private String formatCount(int count) {
        /*
        0~9999： 直接顯示具體數量；
        9999~11000： 11K；
        11001~12000：12K；

        以此類推進行顯示；
        數量超過1萬後 以千位單位進行向上取整，單位為大寫 K；
         */
        return String.valueOf(count);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_goto_activity) {
            gotoActivity();
        } else if (id == R.id.ll_search_box) {
            Util.startFragment(CategoryFragment.newInstance(SearchType.STORE, null));
        } else if (id == R.id.btn_category_store) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.STORE.name(),
                    EasyJSONObject.generate("keyword", "").toString()));
        } else if (id == R.id.btn_category_goods) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                    EasyJSONObject.generate("keyword", "").toString()));
        } else if (id == R.id.btn_category_brand) {
            SearchPostParams searchPostParams = new SearchPostParams();
            searchPostParams.keyword = "";
            Util.startFragment(CircleFragment.newInstance(true, searchPostParams));
        }
    }

    /**
     * 點擊導航欄活動按鈕
     */
    private void gotoActivity() {
        Object data = cell.optParam("data");
        if (data == null) {
            return;
        }

        StickyCellData stickyCellData = (StickyCellData) data;

        SLog.info("appIndexNavigationLinkType[%s]", stickyCellData.appIndexNavigationLinkType);
        if ("activity".equals(stickyCellData.appIndexNavigationLinkType)) { // 活動主頁
            Util.startFragment(H5GameFragment.newInstance(stickyCellData.appIndexNavigationLinkValue, ""));
        } else if ("promotion".equals(stickyCellData.appIndexNavigationLinkType)) { // 購物專場
            SLog.info("跳轉到購物專場");
            Util.startFragment(ShoppingSessionFragment.newInstance());
        }
    }
}
