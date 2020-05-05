package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SkuImageListAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.SkuGalleryItem;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SpecSelectPopup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONObject;

public class SkuImageFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvImageList;

    int initGoodsId;  // 初始選中的GoodsId
    List<SkuGalleryItem> skuGalleryItemList;
    SkuImageListAdapter skuImageListAdapter;

    TextView tvCurrentSpecs;
    TextView tvPrice;
    TextView tvPageIndex;
    int currPosition = 0;

    // 商品Id與RecyclerView Position之間的關係
    Map<Integer, Integer> goodsIdToRvPositionMap = new HashMap<>();

    SpecSelectPopup specSelectPopup;  // 如果不為null，表示來自於規格選擇框
    SimpleCallback simpleCallback;

    public static SkuImageFragment newInstance(int initGoodsId, List<SkuGalleryItem> skuGalleryItemList, SpecSelectPopup specSelectPopup, SimpleCallback simpleCallback) {
        Bundle args = new Bundle();

        SkuImageFragment fragment = new SkuImageFragment();
        fragment.setArguments(args);

        fragment.initGoodsId = initGoodsId;
        fragment.skuGalleryItemList = skuGalleryItemList;
        fragment.specSelectPopup = specSelectPopup;
        fragment.simpleCallback = simpleCallback;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sku_image, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCurrentSpecs = view.findViewById(R.id.tv_current_specs);
        tvPrice = view.findViewById(R.id.tv_price);
        tvPageIndex = view.findViewById(R.id.tv_page_index);

        Util.setOnClickListener(view, R.id.rl_container, this);


        for (int i = 0; i < skuGalleryItemList.size(); i++) {
            SkuGalleryItem item = skuGalleryItemList.get(i);
            goodsIdToRvPositionMap.put(item.goodsId, i);

            if (initGoodsId == item.goodsId) {
                currPosition = i;
            }
        }

        rvImageList = view.findViewById(R.id.rv_image_list);

        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
        (new PagerSnapHelper()).attachToRecyclerView(rvImageList);

        rvImageList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        skuImageListAdapter = new SkuImageListAdapter(_mActivity, R.layout.sku_image_item, skuGalleryItemList);
        skuImageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                commonPop();
            }
        });
        rvImageList.setAdapter(skuImageListAdapter);

        rvImageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = getCurrPosition();
                    if (position < 0) {  // 有可能會出現-1的情況
                        return;
                    }
                    currPosition = position;
                    updateInfoView();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        if (currPosition != 0) {
            rvImageList.scrollToPosition(currPosition);
        }

        updateInfoView();
    }

    /**
     * 获取当前显示的图片的position
     *
     * @return
     */
    private int getCurrPosition() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvImageList.getLayoutManager();
        int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        return position;
    }

    private void updateInfoView() {
        SkuGalleryItem skuGalleryItem = skuGalleryItemList.get(currPosition);
        tvCurrentSpecs.setText(skuGalleryItem.goodsSpecString);
        tvPrice.setText(StringUtil.formatPrice(_mActivity, skuGalleryItem.price, 0));
        tvPageIndex.setText((currPosition + 1) + "/" + skuGalleryItemList.size());
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        commonPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        commonPop();
    }

    private void commonPop() {
        pop();
        if (specSelectPopup != null) {
            specSelectPopup.show();
        }
        if (simpleCallback != null) {
            SkuGalleryItem skuGalleryItem = skuGalleryItemList.get(currPosition);
            simpleCallback.onSimpleCall(EasyJSONObject.generate(
                    "type", PopupType.SELECT_SKU_IMAGE.ordinal(),
                    "goodsId", skuGalleryItem.goodsId,
                    "specValueIds", skuGalleryItem.specValueIds
            ));
        }
    }
}
