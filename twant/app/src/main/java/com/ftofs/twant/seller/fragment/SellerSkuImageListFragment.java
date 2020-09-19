package com.ftofs.twant.seller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.RequestCode;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.adapter.SellerSkuImageListAdapter;
import com.ftofs.twant.seller.entity.SellerGoodsPicVo;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SKU圖片列表
 * @author zwm
 */
public class SellerSkuImageListFragment extends BaseFragment {
    LinearLayout llContainer;
    List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();

    SellerSkuImageListAdapter[] adapters;
    // List<List<SellerGoodsPicVo>> imageListList = new ArrayList<>();

    SellerSpecMapItem colorSpecMapItem;
    Map<Integer, List<SellerGoodsPicVo>> colorImageMap;

    int currIndex; // 當前添加圖片的Index
    // int currColorId;  // 當前添加圖片的colorId

    public static SellerSkuImageListFragment newInstance(SellerSpecMapItem colorSpecMapItem, // 颜色规格，如果没选颜色时，则为null
                                                         Map<Integer, List<SellerGoodsPicVo>> colorImageMap // 对应的图片对象的列表
                                                         ) {
        Bundle args = new Bundle();

        SellerSkuImageListFragment fragment = new SellerSkuImageListFragment();
        fragment.setArguments(args);
        fragment.colorSpecMapItem = colorSpecMapItem;
        fragment.colorImageMap = colorImageMap;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_sku_image_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (colorSpecMapItem == null) { // 如果没选颜色时，则为null
            SLog.info("here");
            SellerSpecItem sellerSpecItem = new SellerSpecItem();
            sellerSpecItem.id = 0; // colorId為0
            sellerSpecItem.name = "圖片";

            sellerSpecItemList.add(sellerSpecItem);
        } else {
            SLog.info("here");
            sellerSpecItemList = colorSpecMapItem.sellerSpecItemList;
        }

        llContainer = view.findViewById(R.id.ll_container);
        adapters = new SellerSkuImageListAdapter[sellerSpecItemList.size()];
        SLog.info("adapters.length[%d]", adapters.length);
        for (int i = 0; i < sellerSpecItemList.size(); i++) {
            SellerSpecItem sellerSpecItem = sellerSpecItemList.get(i);
            int colorId = sellerSpecItem.id;

            View itemView = LayoutInflater.from(_mActivity)
                    .inflate(R.layout.seller_sku_image_list_item, llContainer, false);

            SLog.info("itemView[%s]", itemView.toString());

            List<SellerGoodsPicVo> imageList = colorImageMap.get(colorId);
            if (imageList == null) {
                imageList = new ArrayList<>();
                colorImageMap.put(colorId, imageList);
            }

            RecyclerView rvList = itemView.findViewById(R.id.rv_list);
            rvList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
            adapters[i] = new SellerSkuImageListAdapter(_mActivity, R.layout.seller_sku_spec_image_item, i, imageList);
            adapters[i].setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
                    if (id == R.id.btn_remove_image) {
                        List<String> dataList = adapter.getData();
                        dataList.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                }
            });
            rvList.setAdapter(adapters[i]);

            TextView tvSpecValue = itemView.findViewById(R.id.tv_spec_value);
            tvSpecValue.setText(sellerSpecItem.name);
            View btnAddImage = itemView.findViewById(R.id.btn_add_image);
            btnAddImage.setTag(i);
            btnAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    ImageSelector.builder()
                            .useCamera(true) // 设置是否使用拍照
                            .setSingle(false)  //设置是否单选
                            .setMaxSelectCount(0) // 图片的最大选择数量，小于等于0时，不限数量。
                            .start(SellerSkuImageListFragment.this, RequestCode.SELECT_MULTI_IMAGE.ordinal()); // 打开相册

                    currIndex = index;
                    SLog.info("currIndex[%d]", currIndex);
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            llContainer.addView(itemView, layoutParams);
        } // END OF for
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == RequestCode.SELECT_MULTI_IMAGE.ordinal()) {
            // 获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            if (images == null) {
                return;
            }

            SLog.info("images.size[%d]", images.size());
            for (String item : images) {
                File file = new File(item);

                SLog.info("item[%s], size[%d]", item, file.length());
                addSelectedImage(item);
            }

            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            SLog.info("isCameraImage[%s]", isCameraImage);
        }
    }

    private void addSelectedImage(String path) {
        SLog.info("currIndex[%d]", currIndex);
        SellerSkuImageListAdapter adapter = adapters[currIndex];
        List<SellerGoodsPicVo> dataList = adapter.getData();

        SellerGoodsPicVo newItem = new SellerGoodsPicVo();

        SellerSpecItem currColor = sellerSpecItemList.get(currIndex);
        newItem.absolutePath = path;
        newItem.colorId = currColor.id;
        newItem.colorName = currColor.name;
        dataList.add(newItem);
        adapter.notifyItemInserted(dataList.size() - 1);
    }

    public Map<Integer, List<SellerGoodsPicVo>> collectSkuImageInfo() {
        return colorImageMap;
    }

//    public EasyJSONObject collectSkuImageInfo() {
//
//        try {
//            EasyJSONObject result = EasyJSONObject.generate();
//            int count = 0;
//            for (int i = 0; i < sellerSpecItemList.size(); i++) {
//                SellerSpecItem sellerSpecItem = sellerSpecItemList.get(i);
//                int specValue = sellerSpecItem.id;
//
//                List<String> imageList = imageListList.get(i);
//
//                EasyJSONArray specImageArr = EasyJSONArray.generate();
//                for (int j = 0; j < imageList.size(); j++) {
//                    count++;
//                    String path = imageList.get(j);
//                    SLog.info("正在收集第%d张图片", count);
//
//                    specImageArr.append(path);
//                }
//
//                result.set("spec_value_" + specValue, specImageArr);
//            }
//
//            SLog.info("result[%s]", result.toString());
//            return result;
//        } catch (Exception e) {
//            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
//        }
//
//        return null;
//    }
}
