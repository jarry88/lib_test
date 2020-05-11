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
import com.ftofs.twant.adapter.SkuSpecListAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.SkuGalleryItem;
import com.ftofs.twant.entity.SkuSpecViewItem;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SpecSelectPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONObject;

public class SkuImageFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvImageList;
    RecyclerView rvSkuSpecList;

    int initGoodsId;  // 初始選中的GoodsId
    List<SkuGalleryItem> skuGalleryItemList;
    SkuImageListAdapter skuImageListAdapter;

    SkuSpecListAdapter skuSpecListAdapter;
    List<SkuSpecViewItem> skuSpecList = new ArrayList<>();


    TextView tvPrice;
    TextView tvPageIndex;
    int currPosition = 0;

    // 商品Id與RecyclerView Position之間的關係
    Map<Integer, Integer> goodsIdToRvPositionMap = new HashMap<>();

    SpecSelectPopup specSelectPopup;  // 如果不為null，表示來自於規格選擇框
    SimpleCallback simpleCallback;

    int skuSpecItemWidth;
    int screenWidth;
    double ratio;
    PagerSnapHelper skuSpecPagerSnapHelper;

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

        skuSpecItemWidth = _mActivity.getResources().getDimensionPixelSize(R.dimen.sku_spec_item_width);
        screenWidth = Util.getScreenDimension(_mActivity).first;
        SLog.info("skuSpecItemWidth[%d], screenWidth[%d]", skuSpecItemWidth, screenWidth);
        ratio = ((double) skuSpecItemWidth) / screenWidth;

        Util.setOnClickListener(view, R.id.btn_test, this);


        tvPrice = view.findViewById(R.id.tv_price);
        tvPageIndex = view.findViewById(R.id.tv_page_index);

        Util.setOnClickListener(view, R.id.rl_container, this);


        skuSpecList.add(new SkuSpecViewItem("", SkuSpecViewItem.STATUS_LEFT)); // 在最前面加個空串
        for (int i = 0; i < skuGalleryItemList.size(); i++) {
            SkuGalleryItem item = skuGalleryItemList.get(i);
            goodsIdToRvPositionMap.put(item.goodsId, i);

            if (i == 1) {
                skuSpecList.add(new SkuSpecViewItem(item.goodsSpecString, SkuSpecViewItem.STATUS_RIGHT));
            } else {
                skuSpecList.add(new SkuSpecViewItem(item.goodsSpecString, SkuSpecViewItem.STATUS_CENTER));
            }


            if (initGoodsId == item.goodsId) {
                currPosition = i;
            }
        }
        skuSpecList.add(new SkuSpecViewItem("", SkuSpecViewItem.STATUS_LEFT)); // 在最後面加個空串

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

                    snapToTargetExistingView();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // SLog.info("onScrolled, dx[%d], dy[%d]", dx, dy);
                rvSkuSpecList.scrollBy((int) (dx * ratio), 0);
            }
        });

        if (currPosition != 0) {
            rvImageList.scrollToPosition(currPosition);
        }

        rvSkuSpecList = view.findViewById(R.id.rv_sku_spec_list);
        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
        skuSpecPagerSnapHelper = new PagerSnapHelper();
        skuSpecPagerSnapHelper.attachToRecyclerView(rvSkuSpecList);
        rvSkuSpecList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        skuSpecListAdapter = new SkuSpecListAdapter(R.layout.sku_spec_item, skuSpecList);
        rvSkuSpecList.setAdapter(skuSpecListAdapter);
        rvSkuSpecList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rvSkuSpecList.getLayoutManager();
                    if (layoutManager == null) {
                        return;
                    }
                    int firstPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastPosition = layoutManager.findLastVisibleItemPosition();

                    SLog.info("firstPosition[%d], lastPosition[%d]", firstPosition, lastPosition);

                    if (firstPosition + 2 != lastPosition) {
                        SLog.info("Error!位置數據無效");
                        return;
                    }

                    skuSpecList.get(firstPosition).status = SkuSpecViewItem.STATUS_LEFT;
                    skuSpecList.get(firstPosition + 1).status = SkuSpecViewItem.STATUS_CENTER;
                    skuSpecList.get(lastPosition).status = SkuSpecViewItem.STATUS_RIGHT;

                    skuSpecListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        updateInfoView();
        snapToPosition(currPosition + 1);  // 規格列表滑動到指定position
    }

    /**
     * Snap滑動到最適合位置
     * 參考：
     * 让你明明白白的使用RecyclerView——SnapHelper详解
     * https://www.jianshu.com/p/e54db232df62
     */
    void snapToTargetExistingView() {
        RecyclerView.LayoutManager layoutManager = rvSkuSpecList.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        //找出SnapView
        View snapView = skuSpecPagerSnapHelper.findSnapView(layoutManager);
        if (snapView == null) {
            return;
        }
        //计算出SnapView需要滚动的距离
        int[] snapDistance = skuSpecPagerSnapHelper.calculateDistanceToFinalSnap(layoutManager, snapView);
        //如果需要滚动的距离不是为0，就调用smoothScrollBy（）使RecyclerView滚动相应的距离
        if (snapDistance[0] != 0 || snapDistance[1] != 0) {
            rvSkuSpecList.smoothScrollBy(snapDistance[0], snapDistance[1]);
        }
    }

    /**
     * snap滑動到指定位置
     */
    void snapToPosition(int position) {
        SLog.info("snapToPosition[%d]", position);
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvSkuSpecList.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, (screenWidth - skuSpecItemWidth) / 2);
        skuSpecListAdapter.notifyDataSetChanged();
        snapToTargetExistingView();
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
        SLog.info("goodsSpecString[%s]", skuGalleryItem.goodsSpecString);
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
        int id = v.getId();
        if (id == R.id.btn_test) {
            skuSpecListAdapter.notifyDataSetChanged();
            return;
        }
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

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        skuSpecListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
