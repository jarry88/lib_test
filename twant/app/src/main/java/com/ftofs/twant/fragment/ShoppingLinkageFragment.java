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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SlantedWidget;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;

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


    public static ShoppingLinkageFragment newInstance(ShoppingSpecialFragment shoppingSpecialFragment) {
        ShoppingLinkageFragment fragment = new ShoppingLinkageFragment();
        fragment.parentFragment = shoppingSpecialFragment;
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        linkage.setVisibility(parentFragment.hasGoodsCategory );
        rvGoodsWithoutCategory.setVisibility(1-parentFragment.hasGoodsCategory);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linkage = view.findViewById(R.id.linkage);
        rvSecondList = linkage.findViewById(R.id.rv_secondary);
        rvPrimaryList = linkage.findViewById(R.id.rv_primary);
        rvGoodsWithoutCategory = view.findViewById(R.id.rv_shopping_good_without_category_list);
        initLinkage();

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_shopping_linkage, container, false);
        return view;
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
        if (parentFragment.items == null || parentFragment.items.size() == 0) {
            return;
        }
        ElemePrimaryAdapterConfig primaryAdapterConfig = new ElemePrimaryAdapterConfig();
        primaryAdapterConfig.setBackgroundColor(R.color.mask15_background_color, getResources().getDrawable(white_4dp_right_radius_bg));
        SLog.info("twColor%s",twColor);
        primaryAdapterConfig.setTwColor(twColor);
        ElemeSecondaryAdapterConfig secondaryAdapterConfig = new ElemeSecondaryAdapterConfig();
        linkage.init(parentFragment.items,primaryAdapterConfig,secondaryAdapterConfig);
    }

}
