package com.ftofs.twant.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.LinkageViewSecondaryAdapter;
import com.ftofs.twant.adapter.ShopGoodsListAdapter;
import com.ftofs.twant.adapter.StoreCategoryListAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.LinkageGoodsItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.LinkageView;
import com.ftofs.twant.widget.SlantedWidget;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 購物專場二級聯動子頁面
 *暂时页面内嵌套二级联动方案 后续研究
 * @author gzp
 */
public class ShoppingLinkageFragment extends BaseFragment implements View.OnClickListener {

    LinkageRecyclerView linkage;
    LinkageView linkageView;
    private RecyclerView rvSecondList;
    private RecyclerView rvPrimaryList;
    private ShoppingSpecialFragment parentFragment;
    private int twColor;
    private RecyclerView rvGoodsWithoutCategory;
    List<ElemeGroupedItem> items = new ArrayList<>();

    private ShopGoodsListAdapter shopGoodsListAdapter;
    private List<Goods> goodsList;
    private EasyJSONArray zoneGoodsVoList;
    private EasyJSONArray zoneGoodsCategoryVoList;
    private boolean linkageLoaded;
    private Typeface typeface;
    private RecyclerView rvLinkageSecondList;
    private RecyclerView rvLinkagePrimaryList;
    private List<Goods> linkageItems=new ArrayList<>();
    LinkageViewSecondaryAdapter secondaryAdapter;
    private StoreCategoryListAdapter primaryAdapter;
    private List<StoreLabel> primaryLabelList=new ArrayList<>();
    OnSelectedListener onSelectedListener;


    public static ShoppingLinkageFragment newInstance (ShoppingSpecialFragment shoppingSpecialFragment)  {
        ShoppingLinkageFragment fragment = new ShoppingLinkageFragment();
        fragment.parentFragment = shoppingSpecialFragment;
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shopping_linkage, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeface = AssetsUtil.getTypeface(_mActivity,"fonts/din_alternate_bold.ttf");

        linkageView = view.findViewById(R.id.linkage_view);

        rvLinkageSecondList = linkageView.findViewById(R.id.rv_secondary);
        rvLinkagePrimaryList = linkageView.findViewById(R.id.rv_primary);

        linkage = view.findViewById(R.id.linkage);
        rvSecondList = linkage.findViewById(R.id.rv_secondary);
        rvPrimaryList = linkage.findViewById(R.id.rv_primary);

        rvGoodsWithoutCategory = view.findViewById(R.id.rv_shopping_good_without_category_list);
        initAdapter();

//        initLinkage();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvSecondList.getLayoutParams();
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) rvPrimaryList.getLayoutParams();
        layoutParams.height = parentFragment.scrollView.getHeight() - 44;
//        layoutParams1.height =parentFragment.scrollView.getHeight();
        rvSecondList.setLayoutParams(layoutParams);
//        rvPrimaryList.setLayoutParams(layoutParams1);

        SLog.info("isNestedScrollingEnabled[%s]", rvSecondList.isNestedScrollingEnabled());

        rvSecondList.setNestedScrollingEnabled(false);
        rvSecondList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                SLog.info("__newState[%d]", newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    int linkageY_ = Util.getYOnScreen(linkage) + linkage.getHeight();
                    SLog.info("linkageY_[%s]", linkageY_);
//                    hideFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    showFloatButton();
                }
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");

        updateView();


    }

    private void updateView() {
        linkage.setVisibility(View.GONE);
        rvGoodsWithoutCategory.setVisibility(parentFragment.hasGoodsCategory==1?View.GONE:View.VISIBLE);
        if (parentFragment.hasGoodsCategory == 1) {
            parentFragment.viewPager.setVisibility(View.GONE);
            parentFragment.linkage.setVisibility(View.VISIBLE);

            if (zoneGoodsCategoryVoList != null && !linkageLoaded) {
//                updateLinkage();
            }
        } else {
            if (zoneGoodsVoList != null) {
                parentFragment.viewPager.setVisibility(View.VISIBLE);
                updateGoodVoList(zoneGoodsVoList);
            }
        }
    }


    private void initGoodsAdapter() {
        shopGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = goodsList.get(position);
                SLog.info("here");
                int commonId = goods.id;
                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            }
        });
        shopGoodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                Goods goods = goodsList.get(position);
                SLog.info("here");
                int commonId = goods.id;
                int userId = User.getUserId();
                if (id == R.id.iv_goods_add) {
                    if (userId > 0) {
                        parentFragment.showSpecSelectPopup(commonId);
                    } else {
                        Util.showLoginFragment();
                    }
                }
            }
        });
    }

    private void initAdapter() {
        goodsList = new ArrayList<>();
        shopGoodsListAdapter = new ShopGoodsListAdapter(_mActivity, goodsList,R.layout.adapter_shopping_zone_secondary_linear);
//        shopGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Goods goods = goodsList.get(position);
//                int commonId = goods.id;
//
//
//            }
//        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        LinearLayoutManager linkageSecondManager = new LinearLayoutManager(_mActivity);
        LinearLayoutManager linkagePrimaryManager = new LinearLayoutManager(_mActivity);
        initGoodsAdapter();

        rvGoodsWithoutCategory.setLayoutManager(linearLayoutManager);
        rvGoodsWithoutCategory.setAdapter(shopGoodsListAdapter);
        rvGoodsWithoutCategory.setNestedScrollingEnabled(false);


        secondaryAdapter = new LinkageViewSecondaryAdapter(_mActivity,linkageItems);
        rvLinkageSecondList.setLayoutManager( linkageSecondManager);
        rvLinkageSecondList.setAdapter(secondaryAdapter);
        rvLinkageSecondList.setNestedScrollingEnabled(false);

//        primaryAdapter = new StoreCategoryListAdapter(_mActivity,R.layout.store_category_list_item, primaryLabelList, this);
        rvLinkagePrimaryList.setLayoutManager( linkagePrimaryManager);
        rvLinkagePrimaryList.setAdapter(primaryAdapter);
        rvLinkagePrimaryList.setNestedScrollingEnabled(false);
////
//        shopGoodsListAdapter.setEnableLoadMore(true);
//        shopGoodsListAdapter.setOnLoadMoreListener(() -> {
//            SLog.info("onLoadMoreRequested");
//
////                if (!hasMore) {
////                    shopGoodsListAdapter.setEnableLoadMore(false);
////                    return;
////                }
////                loadStoreGoods(paramsOriginal, mExtra, currPage + 1);
//        }, rvGoodsWithoutCategory);
    }
    public void updateGoodVoList(EasyJSONArray zoneGoodsVoList) {
        try {
            for (Object object1 : zoneGoodsVoList) {
                EasyJSONObject goods = (EasyJSONObject) object1;
                Goods goodsInfo = Goods.parse(goods);
                goodsList.add(goodsInfo);

                rvGoodsWithoutCategory.setVisibility(View.VISIBLE);
            }
            shopGoodsListAdapter.setNewData(goodsList);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public void setGoodVoList(EasyJSONArray zoneGoodsVoList) {
        this.zoneGoodsVoList = zoneGoodsVoList;
        updateView();
    }

    @Override
    public void onClick(View v) {
        SLog.info("onClick%s",v.getId());
        int id = v.getId();
        if (id == R.id.btn_test) {
            SLog.info("here");
        }
    }

    public void setNestedScroll(boolean b) {
        rvGoodsWithoutCategory.setNestedScrollingEnabled(b);
    }


    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;
        private int backgroundColor;
        private Drawable default_drawbg;

        public ElemePrimaryAdapterConfig(Context context) {
            this.mContext = context;
        }

        public void setTwColor(int twColor) {
            this.twColor = twColor;
        }

        private int twColor = R.color.tw_black;

        @Override
        public void setContext(Context context) {
//            mContext = ;
        }

        public void setBackgroundColor(int color, Drawable bg) {
            backgroundColor = color;
            default_drawbg = bg;
        }


        @Override
        public int getLayoutId() {
            return R.layout.adapter_linkage_primary;
        }

        @Override
        public int getGroupTitleViewId() {
            return R.id.tv_group;
        }

        @Override
        public int getRootViewId() {
            return R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            TextView tvTitle = ((TextView) holder.mGroupTitle);
            tvTitle.setText(title);
            View blue = holder.mLayout.findViewById(R.id.view_border);
            blue.setVisibility(selected ? View.VISIBLE : View.GONE);
            if (selected) {
                tvTitle.setBackground(default_drawbg);
                holder.mLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
            } else {
                holder.mLayout.setBackgroundColor(Color.argb(26, 0, 0, 0));
                tvTitle.setBackgroundColor(Color.argb(0, 0, 0, 0));
            }
            tvTitle.setTextColor(ContextCompat.getColor(mContext,
                    selected ? R.color.tw_blue : R.color.tw_black));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? -1 : 0);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
            ToastUtil.error(mContext, title);
        }
    }
    //从asset 读取字体
    //得到AssetManager



    private static class ElemeSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private final Typeface typeFace;
        private Context mContext;

        public ElemeSecondaryAdapterConfig(Typeface typeface,Context context) {
            this.typeFace = typeface;
            this.mContext =context;
        }

        @Override
        public void setContext(Context context) {
//            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return 0;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_shopping_zone_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return R.layout.adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return R.layout.black_layout;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return 2;
        }

        @Override
        public void onBindViewHolder(LinkageSecondaryViewHolder holder,
                                     BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            try {
                ((TextView) holder.getView(R.id.tv_goods_name)).setText(item.info.getTitle());
                ((TextView) holder.getView(R.id.tv_goods_comment)).setText(item.info.getContent());
                TextView tvPrice=holder.getView(R.id.tv_goods_price);
                TextView tvOriginalPrice=holder.getView(R.id.tv_goods_original_price);
                tvPrice.setText(StringUtil.formatPrice(mContext, Double.valueOf(item.info.getCost().substring(1)), 0, true));
                if (item.info.getOriginal() > 0) {
                    tvOriginalPrice.setVisibility(View.VISIBLE);
                    tvOriginalPrice.setText(StringUtil.formatPrice(mContext, item.info.getOriginal(), 0, true));
                    tvPrice.setTypeface(typeFace);
                    tvOriginalPrice.setTypeface(typeFace);
                    // 原價顯示刪除線
                    tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                holder.getView(R.id.sw_price).setVisibility(item.info.show ? View.VISIBLE : View.GONE);
                ((SlantedWidget) holder.getView(R.id.sw_price)).setDiscountInfo(mContext, item.info.getDiscount(), item.info.getOriginal());
                ImageView imageView = holder.getView(R.id.img_goods_item);
                Glide.with(mContext).load(item.info.getImgUrl()).centerCrop().into(imageView);
                holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                    //TODO
                    Util.startFragment(GoodsDetailFragment.newInstance(item.info.commonId, 0));
                });

                holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                    //TODO
                });
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        }
    }

    private void initLinkage() {
//        if (items == null || items.size() == 0) {
//            return;
//        }

//        ElemePrimaryAdapterConfig primaryAdapterConfig = new ElemePrimaryAdapterConfig(getContext());
//        primaryAdapterConfig.setBackgroundColor(R.color.mask15_background_color, getResources().getDrawable(white_4dp_right_radius_bg));
//        SLog.info("twColor%s,%d", twColor, items.size());
//        primaryAdapterConfig.setTwColor(twColor);
        primaryAdapter.setNewData(primaryLabelList);
        secondaryAdapter.setNewData(linkageItems);
//        linkage.init(items, primaryAdapterConfig, secondaryAdapterConfig);
        linkageLoaded = true;
    }

    public void setLinkageData(EasyJSONArray zoneGoodsCategoryVoList) {
        this.zoneGoodsCategoryVoList = zoneGoodsCategoryVoList;
        updateView();
    }

    private void updateLinkage() {
        try {
            SLog.info("設置二級聯動列表數據");
            items.clear();
            primaryLabelList.clear();
            for (Object object : zoneGoodsCategoryVoList) {
                EasyJSONObject categoryData = (EasyJSONObject) object;

                String groupName = categoryData.getSafeString("categoryName");
                EasyJSONArray goodsList = categoryData.getSafeArray("zoneGoodsVoList");
                ElemeGroupedItem category = new ElemeGroupedItem(true, groupName);
                if (goodsList == null) {
                    continue;
                }
                items.add(category);
                StoreLabel storeLabel = new StoreLabel();
                storeLabel.setStoreLabelName(groupName);
                primaryLabelList.add(storeLabel);
                for (Object object1 : goodsList) {
                    EasyJSONObject goods = (EasyJSONObject) object1;
                    String goodsName = goods.getSafeString("goodsName");
                    String goodsImage = goods.getSafeString("goodsImage");
                    int commonId = goods.getInt("commonId");
                    String jingle = goods.getSafeString("goodsFullSpecs");
                    double price;
                    int appUsable = goods.getInt("appUsable");
                    if (appUsable > 0) {
                        price = goods.getDouble("appPriceMin");
                    } else {
                        price = goods.getDouble("batchPrice0");
                    }

                    double batchPrice0 = goods.getDouble("batchPrice0");
                    double promotionDiscountRate = goods.getDouble("promotionDiscountRate");

                    int goodsStorage=goods.getInt("goodsStorage");
                    ElemeGroupedItem.ItemInfo goodsInfo = new ElemeGroupedItem.ItemInfo(goodsName, groupName, jingle, StringUtil.normalizeImageUrl(goodsImage),
                            StringUtil.formatPrice(getContext(), price, 0), promotionDiscountRate, batchPrice0, commonId, appUsable > 0,goodsStorage);
                    ElemeGroupedItem item1 = new ElemeGroupedItem(goodsInfo);
                    items.add(item1);

                    linkageItems.add(Goods.parse(goods));
                }

            }
            initLinkage();


        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

}
