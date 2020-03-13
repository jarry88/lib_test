package com.ftofs.twant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.google.gson.Gson;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;

/**
 * @author gzp
 * 顯示layout佈局界面
 */
public class BlackTestFragment extends BaseFragment{
    int res;
    LinkageRecyclerView linkage;
    public static BlackTestFragment newInstance() {
        BlackTestFragment fragment = new BlackTestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.black_layout, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linkage = view.findViewById(R.id.linkage);
        initLinkageDatas(linkage);


    }
    private void initLinkageDatas(LinkageRecyclerView linkage) {
        Gson gson = new Gson();
//        List<ElemeGroupedItem> items = gson.fromJson(getString(R.string.operators_json),
//                new TypeToken<List<ElemeGroupedItem>>() {
//                }.getType());
//
//        linkage.init(items, new ElemePrimaryAdapterConfig(), new ElemeSecondaryAdapterConfig());
    }

    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutId() {
            return com.kunminx.linkage.R.layout.default_adapter_linkage_primary;
        }

        @Override
        public int getGroupTitleViewId() {
            return com.kunminx.linkage.R.id.tv_group;
        }

        @Override
        public int getRootViewId() {
            return com.kunminx.linkage.R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            TextView tvTitle = ((TextView) holder.mGroupTitle);
            tvTitle.setText(title);

            tvTitle.setBackgroundColor(mContext.getResources().getColor(
                    selected ? com.kunminx.linkage.R.color.colorPurple : com.kunminx.linkage.R.color.colorWhite));
            tvTitle.setTextColor(ContextCompat.getColor(mContext,
                    selected ? com.kunminx.linkage.R.color.colorWhite : com.kunminx.linkage.R.color.colorGray));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? -1 : 0);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
        }
    }

    private static class ElemeSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private Context mContext;

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
            return com.kunminx.linkage.R.layout.default_adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return 0;
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

            ((TextView) holder.getView(R.id.tv_goods_name)).setText(item.info.getTitle());
            Glide.with(mContext).load(item.info.getImgUrl()).into((ImageView) holder.getView(R.id.iv_goods_img));
            holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                //TODO
            });

            holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                //TODO
            });
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

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }
}
