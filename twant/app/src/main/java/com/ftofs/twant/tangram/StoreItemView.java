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
import com.ftofs.twant.entity.StoreItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

public class StoreItemView extends LinearLayout implements ITangramViewLifeCycle, View.OnClickListener {
    Context context;

    BaseCell cell;

    LinearLayout goodsImageLeftContainer;
    LinearLayout goodsImageMiddleContainer;
    LinearLayout goodsImageRightContainer;

    ImageView goodsImageLeft;
    ImageView goodsImageMiddle;
    ImageView goodsImageRight;

    TextView tvStoreName;
    TextView tvStoreClass;
    ImageView imgStoreFigure;

    public StoreItemView(Context context) {
        this(context, null);
    }

    public StoreItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoreItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);


        View contentView = LayoutInflater.from(context).inflate(R.layout.store_view, this, false);

        goodsImageLeftContainer = contentView.findViewById(R.id.goods_image_left_container);
        goodsImageMiddleContainer = contentView.findViewById(R.id.goods_image_middle_container);
        goodsImageRightContainer = contentView.findViewById(R.id.goods_image_right_container);
        goodsImageLeftContainer.setOnClickListener(this);
        goodsImageMiddleContainer.setOnClickListener(this);
        goodsImageRightContainer.setOnClickListener(this);

        goodsImageLeft = contentView.findViewById(R.id.goods_image_left);
        goodsImageMiddle = contentView.findViewById(R.id.goods_image_middle);
        goodsImageRight = contentView.findViewById(R.id.goods_image_right);

        tvStoreName = contentView.findViewById(R.id.tv_store_name);
        tvStoreClass = contentView.findViewById(R.id.tv_store_class);
        imgStoreFigure = contentView.findViewById(R.id.img_store_figure);
        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {
        this.cell = cell;
    }

    @Override
    public void postBindView(BaseCell cell) {
        StoreItem storeItem = (StoreItem) cell.optParam("data");

        tvStoreName.setText(storeItem.storeName);
        tvStoreClass.setText(storeItem.storeClass);
        if (StringUtil.isEmpty(storeItem.storeFigureImage)) {
            Glide.with(getContext()).load(R.drawable.store_figure_default).centerCrop().into(imgStoreFigure);
        } else {
            Glide.with(getContext()).load(StringUtil.normalizeImageUrl(storeItem.storeFigureImage)).centerCrop().into(imgStoreFigure);
        }

        goodsImageLeftContainer.setVisibility(GONE);
        goodsImageMiddleContainer.setVisibility(GONE);
        goodsImageRightContainer.setVisibility(GONE);
        for (int i = 0; i < storeItem.goodsList.size(); i++) {
            String imageSrc = StringUtil.normalizeImageUrl(storeItem.goodsList.get(i).imageUrl);

            if (i == 0) {
                goodsImageLeftContainer.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(imageSrc).centerCrop().into(goodsImageLeft);
            } else if (i == 1) {
                goodsImageMiddleContainer.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(imageSrc).centerCrop().into(goodsImageMiddle);
            } else if (i == 2) {
                goodsImageRightContainer.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(imageSrc).centerCrop().into(goodsImageRight);
            }
        }
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        StoreItem storeItem = (StoreItem) cell.optParam("data");

        if (id == R.id.goods_image_left_container || id == R.id.goods_image_middle_container || id == R.id.goods_image_right_container) {
            int commonId;
            if (id == R.id.goods_image_left_container) {
                commonId = storeItem.goodsList.get(0).id;
            } else if (id == R.id.goods_image_middle_container) {
                commonId = storeItem.goodsList.get(1).id;
            } else {
                commonId = storeItem.goodsList.get(2).id;
            }

            Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
        }
    }
}


