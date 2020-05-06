package com.ftofs.twant.fragment;

import android.content.Context;
import android.graphics.Color;
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
import com.ftofs.twant.adapter.ShopGoodsListAdapter;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
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
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

import static com.ftofs.twant.R.drawable.white_4dp_right_radius_bg;


/**
 * 購物專場二級聯動子頁面
 * @author gzp
 */
public class ShoppingLinkageFragment extends BaseFragment{

    LinkageRecyclerView linkage;
    private RecyclerView rvSecondList;
    private RecyclerView rvPrimaryList;
    private ShoppingSpecialFragment parentFragment;
    private int twColor;
    private RecyclerView rvGoodsWithoutCategory;
    List<ElemeGroupedItem> items = new ArrayList<>();

    private ShopGoodsListAdapter shopGoodsListAdapter;
    private List<Goods> goodsList;
    private EasyJSONArray zoneGoodsVoList;


    public static ShoppingLinkageFragment newInstance(ShoppingSpecialFragment shoppingSpecialFragment) {
        ShoppingLinkageFragment fragment = new ShoppingLinkageFragment();
        fragment.parentFragment = shoppingSpecialFragment;
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");

        linkage.setVisibility(parentFragment.hasGoodsCategory );
        rvGoodsWithoutCategory.setVisibility(1-parentFragment.hasGoodsCategory);
        if (parentFragment.hasGoodsCategory == 1) {

        } else {
            if (zoneGoodsVoList != null) {
                updateGoodVoList(zoneGoodsVoList);
            }
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linkage = view.findViewById(R.id.linkage);
        rvSecondList = linkage.findViewById(R.id.rv_secondary);
        rvPrimaryList = linkage.findViewById(R.id.rv_primary);
        rvGoodsWithoutCategory = view.findViewById(R.id.rv_shopping_good_without_category_list);
        initAdapter();

        initLinkage();
        initGoodsAdapter();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvSecondList.getLayoutParams();
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout .LayoutParams) rvPrimaryList.getLayoutParams();
        layoutParams.height =parentFragment.scrollView.getHeight()-44;
        layoutParams1.height =parentFragment.scrollView.getHeight();
//        rvSecondList.setLayoutParams(layoutParams);
        rvPrimaryList.setLayoutParams(layoutParams1);


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

    private void initGoodsAdapter() {
        shopGoodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                Goods goods = goodsList.get(position);
                int commonId = goods.id;
                int userId = User.getUserId();
                if (id == R.id.btn_add_to_cart) {
                    if (userId > 0) {
                        showSpecSelectPopup(commonId);
                    } else {
                        Util.showLoginFragment();
                    }
                }
            }
        });
    }
    private void initAdapter() {
        goodsList = new ArrayList<>();
        shopGoodsListAdapter = new ShopGoodsListAdapter(_mActivity, goodsList);
        shopGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = goodsList.get(position);
                int commonId = goods.id;

                Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvGoodsWithoutCategory.setLayoutManager(linearLayoutManager);
        rvGoodsWithoutCategory.setAdapter(shopGoodsListAdapter);

//
//        shopGoodsListAdapter.setEnableLoadMore(true);
//        shopGoodsListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                SLog.info("onLoadMoreRequested");
//
////                if (!hasMore) {
////                    shopGoodsListAdapter.setEnableLoadMore(false);
////                    return;
////                }
////                loadStoreGoods(paramsOriginal, mExtra, currPage + 1);
//            }
//        }, rvGoodsWithoutCategory);
    }
    private void updateGoodVoList(EasyJSONArray goodsList,String groupName) {
        try {
            for (Object object1 : goodsList) {
                EasyJSONObject goods = (EasyJSONObject) object1;
                String goodsName = goods.getSafeString("goodsName");

                String goodsImage = goods.getSafeString("goodsImage");
                int commonId = goods.getInt("commonId");
                String jingle  = goods.getSafeString("jingle");
                double price;
                int appUsable = goods.getInt("appUsable");
                if (appUsable > 0) {
                    price =  goods.getDouble("appPriceMin");
                } else {
                    price =  goods.getDouble("batchPrice0");
                }

                double batchPrice0 =  goods.getDouble("batchPrice0");
                double promotionDiscountRate =  goods.getDouble("promotionDiscountRate");


                if (groupName == null) {
                    Goods goodsInfo = new Goods(commonId, goodsImage, goodsName, jingle, price);
                    this.goodsList.add(goodsInfo);
                } else {
                    ElemeGroupedItem.ItemInfo goodsInfo = new ElemeGroupedItem.ItemInfo(goodsName, groupName, jingle, StringUtil.normalizeImageUrl(goodsImage),
                            StringUtil.formatPrice(getContext(), price, 0),promotionDiscountRate,batchPrice0,commonId,appUsable > 0);
                    ElemeGroupedItem item1 = new ElemeGroupedItem(goodsInfo);
                    items.add(item1);
                }
            }
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shopping_linkage, container, false);
        return view;
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
    }

    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;
        private int backgroundColor;
        private Drawable default_drawbg;

        public void setTwColor(int twColor) {
            this.twColor = twColor;
        }

        private int twColor=R.color.tw_black;

        public void setContext(Context context) {
            mContext = context;
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
            blue.setVisibility(selected?View.VISIBLE:View.GONE);
            if (selected) {
                tvTitle.setBackground(default_drawbg);
                holder.mLayout.setBackgroundColor(Color.argb(0,0,0,0));
            } else {
                holder.mLayout.setBackgroundColor(Color.argb(26,0,0,0));
                tvTitle.setBackgroundColor(Color.argb(0,0,0,0));
            }
            tvTitle.setTextColor( ContextCompat.getColor(mContext,
                    selected ?R.color.tw_blue:R.color.tw_black));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? -1 : 0);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
//            ToastUtil.error(mContext,title);
        }
    }

    private static class ElemeSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private Context mContext;

        @Override
        public void setContext(Context context) {
            mContext = context;
        }
        @Override
        public int getGridLayoutId() {
            return 0;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_eleme_secondary_linear;
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
                ((TextView) holder.getView(R.id.tv_goods_price)).setText(StringUtil.formatPrice(mContext, Double.valueOf(item.info.getCost().substring(1)), 0, true));
                holder.getView(R.id.sw_price).setVisibility(item.info.show?View.VISIBLE:View.GONE);
                ((SlantedWidget) holder.getView(R.id.sw_price)).setDiscountInfo(mContext,item.info.getDiscount(),item.info.getOriginal());
                ImageView imageView =  holder.getView(R.id.iv_goods_img);
                Glide.with(mContext).load(item.info.getImgUrl()).centerCrop().into(imageView);
                holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                    //TODO
                    Util.startFragment(GoodsDetailFragment.newInstance(item.info.commonId,0));
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
        if (items == null || items.size() == 0) {
            return;
        }
        ElemePrimaryAdapterConfig primaryAdapterConfig = new ElemePrimaryAdapterConfig();
        primaryAdapterConfig.setBackgroundColor(R.color.mask15_background_color, getResources().getDrawable(white_4dp_right_radius_bg));
        SLog.info("twColor%s",twColor);
        primaryAdapterConfig.setTwColor(twColor);
        ElemeSecondaryAdapterConfig secondaryAdapterConfig = new ElemeSecondaryAdapterConfig();
        linkage.init(items,primaryAdapterConfig,secondaryAdapterConfig);
    }
    public void setLinkageData(EasyJSONArray zoneGoodsCategoryVoList) {
        try {
            SLog.info("設置二級聯動列表數據");

            for (Object object : zoneGoodsCategoryVoList) {
                EasyJSONObject categoryData = (EasyJSONObject) object;

                String groupName = categoryData.getSafeString("categoryName");
                EasyJSONArray goodsList = categoryData.getSafeArray("zoneGoodsVoList");
                ElemeGroupedItem category = new ElemeGroupedItem(true, groupName);
                if(goodsList==null){
                    continue;
                }
                items.add(category);
                for (Object object1 : goodsList) {
                    EasyJSONObject goods = (EasyJSONObject) object1;
                    String goodsName = goods.getSafeString("goodsName");
                    String goodsImage = goods.getSafeString("goodsImage");
                    int commonId = goods.getInt("commonId");
                    String jingle  = goods.getSafeString("jingle");
                    double price;
                    int appUsable = goods.getInt("appUsable");
                    if (appUsable > 0) {
                        price =  goods.getDouble("appPriceMin");
                    } else {
                        price =  goods.getDouble("batchPrice0");
                    }

                    double batchPrice0 = goods.getDouble("batchPrice0");
                    double promotionDiscountRate =  goods.getDouble("promotionDiscountRate");

                    ElemeGroupedItem.ItemInfo goodsInfo = new ElemeGroupedItem.ItemInfo(goodsName, groupName, jingle, StringUtil.normalizeImageUrl(goodsImage),
                            StringUtil.formatPrice(getContext(), price, 0),promotionDiscountRate,batchPrice0,commonId,appUsable > 0);
                    ElemeGroupedItem item1 = new ElemeGroupedItem(goodsInfo);
                    items.add(item1);
                }
                initLinkage();

            }

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    private void showSpecSelectPopup(int commonId) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, Constant.ACTION_ADD_TO_CART, commonId, null, null, null, 1, null, null, 0,2,null))
                .show();
    }
}
