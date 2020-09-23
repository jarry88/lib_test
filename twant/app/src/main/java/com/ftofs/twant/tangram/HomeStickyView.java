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
import com.ftofs.twant.BlankFragment;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.entity.StickyCellData;
import com.ftofs.twant.fragment.CategoryFragment;
import com.ftofs.twant.fragment.CircleFragment;
import com.ftofs.twant.fragment.ExplorerFragment;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.PostDetailFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.fragment.ShoppingSessionFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

import cn.snailpad.easyjson.EasyJSONObject;

public class HomeStickyView extends LinearLayout implements ITangramViewLifeCycle, View.OnClickListener {
    Context context;

    TextView tvStoreCount;
    TextView tvGoodsCount;
    TextView tvPostCount;
    TextView tvFriendCount;  // 要諮詢
    TextView tvRandomFriendCount; // 城友

    View iconTakewant;
    ImageView iconActivityEntrance;
    ImageView gifSearch;
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
        contentView.findViewById(R.id.btn_category_friend).setOnClickListener(this);
        contentView.findViewById(R.id.btn_random_friend).setOnClickListener(this);
        contentView.findViewById(R.id.icon_takewant).setOnClickListener(this);

        tvStoreCount = contentView.findViewById(R.id.tv_store_count);
        tvGoodsCount = contentView.findViewById(R.id.tv_goods_count);
        tvPostCount = contentView.findViewById(R.id.tv_post_count);
        tvFriendCount = contentView.findViewById(R.id.tv_friend_count);
        tvRandomFriendCount = contentView.findViewById(R.id.tv_random_friend_count);

        iconActivityEntrance = contentView.findViewById(R.id.icon_activity_entrance);
        vwActivityEntrancePlaceholder = contentView.findViewById(R.id.vw_activity_entrance_placeholder);
        btnGotoActivity = contentView.findViewById(R.id.btn_goto_activity);
        iconTakewant = contentView.findViewById(R.id.icon_takewant);
        gifSearch = contentView.findViewById(R.id.btn_search);
        Glide.with(contentView).load(R.drawable.home_search).into(gifSearch);

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
            //首頁城有數量顯示
            StickyCellData stickyCellData = (StickyCellData) data;
            String randomFriendCount = String.valueOf(stickyCellData.memberCount);
//            tvRandomFriendCount.setText(randomFriendCount);
            if (Config.USE_DEVELOPER_TEST_DATA) {
                randomFriendCount = "1234567";
            }
            tvRandomFriendCount.setText(randomFriendCount);

            tvStoreCount.setText(Util.formatCount(stickyCellData.storeCount));
            tvStoreCount.setVisibility(VISIBLE);
            tvGoodsCount.setText(Util.formatCount(stickyCellData.goodsCommonCount));
            tvGoodsCount.setVisibility(VISIBLE);
            tvFriendCount.setText(Util.formatCount(stickyCellData.imSessionCount));
            tvFriendCount.setVisibility(VISIBLE);
            tvPostCount.setText(Util.formatCount(stickyCellData.wantPostCount));
            tvPostCount.setVisibility(VISIBLE);




//            vwActivityEntrancePlaceholder.setVisibility(stickyCellData.activityEnable ? View.VISIBLE : View.GONE);
            //加入有成交后强制影藏
//            btnGotoActivity.setVisibility(stickyCellData.activityEnable ? View.VISIBLE : View.GONE);
//            if (stickyCellData.activityEnable) {
//                Glide.with(getContext()).load(StringUtil.normalizeImageUrl(stickyCellData.appIndexNavigationImage))
//                        .into(iconActivityEntrance);
//                btnGotoActivity.setOnClickListener(this);
//            }
        }
        iconTakewant.setOnClickListener(this);
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_goto_activity) {
//            gotoTestFragment();
            gotoActivity();
        } else if (id == R.id.ll_search_box) {
            Util.startFragment(CategoryFragment.newInstance(SearchType.GOODS, null));
        } else if (id == R.id.btn_category_store) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.STORE.name(),
                    EasyJSONObject.generate("keyword", "").toString()));
            // Util.startFragment(CategoryFragment.newInstance(SearchType.STORE, null));
        } else if (id == R.id.btn_category_goods) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                    EasyJSONObject.generate("keyword", "").toString()));
            // Util.startFragment(CategoryFragment.newInstance(SearchType.GOODS, null));
        } else if (id == R.id.btn_category_brand) {
            SearchPostParams searchPostParams = new SearchPostParams();
            searchPostParams.keyword = "";
            Util.startFragment(CircleFragment.newInstance(true, searchPostParams));
        }else if (id == R.id.btn_category_friend) {
            SearchPostParams searchPostParams = new SearchPostParams();
            searchPostParams.keyword = "";
            MainFragment mainFragment = MainFragment.getInstance();
            if (StringUtil.isEmpty(User.getToken())) {
                Util.showLoginFragment(context);
            } else {
                mainFragment.showHideFragment(MainFragment.MESSAGE_FRAGMENT);
            }

        }else if (id == R.id.btn_random_friend) {
            Util.startFragment(new RandomFriendListFragment());
//            Util.startFragment(new Test2Fragment());

        } else if (id == R.id.icon_takewant) {
            if (Config.PROD) {
                return;
            }
            SLog.info("进入测试页ImGoodsFragment ");
            Util.startFragment(new BlankFragment());
//            Util.startFragment( ImGoodsFragment.newInstance("u_007615414781"),null);
//             Util.startFragment(LabFragment.newInstance());
            // Util.startFragment(GoodsDetailFragment.newInstance(4195, 6957, 1));
//            Util.startFragment(LabFragment.newInstance());
//            Util.startFragment(GoodsDetailFragment.newInstance(4195, 6957, 1));
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

        SLog.info("data [%s],appIndexNavigationLinkType[%s]", data.toString(),stickyCellData.appIndexNavigationLinkType);
//        if ("activity".equals(stickyCellData.appIndexNavigationLinkType)) { // 活動主頁
//            Util.startFragment(H5GameFragment.newInstance(stickyCellData.appIndexNavigationLinkValue, ""));
//        } else if ("promotion".equals(stickyCellData.appIndexNavigationLinkType)) { // 購物專場
//            SLog.info("跳轉到購物專場");
//            Util.startFragment(ShoppingSessionFragment.newInstance());
//        }
        String linkType = stickyCellData.appIndexNavigationLinkType;
        switch (linkType) {
            case "none":
                // 无操作
                break;
            case "promotion":
                Util.startFragment(ShoppingSessionFragment.newInstance());
                break;
            case "url":
                // 外部鏈接
                String url = StringUtil.normalizeImageUrl(stickyCellData.appIndexNavigationLinkValue);
                Util.startFragment(ExplorerFragment.newInstance(url, true));
                break;
            case "keyword":
                // 关键字
                String keyword = stickyCellData.appIndexNavigationLinkValue;
                Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                        EasyJSONObject.generate("keyword", keyword).toString()));
                break;
            case "goods":
                // 產品
                int commonId = Integer.parseInt(stickyCellData.appIndexNavigationLinkValue);
                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                break;
            case "store":
                // 店铺
                int storeId = Integer.parseInt(stickyCellData.appIndexNavigationLinkValue);
                Util.startFragment(ShopMainFragment.newInstance(storeId));
                break;
            case "category":
                // 產品搜索结果页(分类)
                String cat = stickyCellData.appIndexNavigationLinkValue;
                Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                        EasyJSONObject.generate("cat", cat).toString()));
                break;
            case "brandList":
                // 品牌列表
                break;
            case "voucherCenter":
                // 领券中心
                break;
            case "activityUrl":
                String activityUrl = StringUtil.normalizeImageUrl(stickyCellData.appIndexNavigationLinkValue);
                Util.startFragment(H5GameFragment.newInstance(activityUrl, true));
                break;
            case "postId":
                int postId = Integer.parseInt(stickyCellData.appIndexNavigationLinkValue);
                Util.startFragment(PostDetailFragment.newInstance(postId));
                break;
            case "shopping":
                Util.startFragment(ShoppingSessionFragment.newInstance());
                break;
            case "shoppingZone":
                //購物新專場
                int zoneId = Integer.parseInt(stickyCellData.appIndexNavigationLinkValue);
//                if (Config.DEVELOPER_MODE) {
//                    zoneId=20;
//                }
                Util.startFragment(NewShoppingSpecialFragment.newInstance(zoneId));
                break;
            case "wantPost":
                MainFragment mainFragment = MainFragment.getInstance();
                if (mainFragment == null) {
                    ToastUtil.error(TwantApplication.Companion.get(), "MainFragment為空");
                    return;
                }
                mainFragment.showHideFragment(MainFragment.CIRCLE_FRAGMENT);
                break;
            default:
                break;
        }
    }
}
