package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingPopup;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SellerSkuEditorFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    List<SellerSpecMapItem> sellerSelectedSpecList;

    EasyJSONArray goodsJsonVoList = EasyJSONArray.generate();
    EasyJSONArray goodsPicVoList = EasyJSONArray.generate();

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    HwLoadingPopup loadingPopup;

    List<SellerSpecPermutation> permutationList = new ArrayList<>();


    public static SellerSkuEditorFragment newInstance(List<SellerSpecMapItem> sellerSelectedSpecList) {
        Bundle args = new Bundle();

        SellerSkuEditorFragment fragment = new SellerSkuEditorFragment();
        fragment.setArguments(args);
        fragment.sellerSelectedSpecList = sellerSelectedSpecList;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_sku_editor, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);


        int TOTAL_SKU_COUNT = 1;

        for (SellerSpecMapItem item : sellerSelectedSpecList) {
            TOTAL_SKU_COUNT *= item.sellerSpecItemList.size();
        }
        SLog.info("TOTAL_SKU_COUNT[%d]", TOTAL_SKU_COUNT);

        for (int i = 0; i < TOTAL_SKU_COUNT; i++) {
            int n = i;
            StringBuilder skuDesc = new StringBuilder();  // SKU規格描述
            SellerSpecPermutation permutation = new SellerSpecPermutation();

            for (int j = 0; j < sellerSelectedSpecList.size(); j++) {
                SellerSpecMapItem sellerSpecMapItem = sellerSelectedSpecList.get(j);

                int index = n % sellerSpecMapItem.sellerSpecItemList.size();
                SellerSpecItem specItem = sellerSpecMapItem.sellerSpecItemList.get(index);

                permutation.sellerSpecItemList.add(specItem);

                skuDesc.append(sellerSpecMapItem.sellerSpecItemList.get(index).name).append("/");

                n /= sellerSpecMapItem.sellerSpecItemList.size();
            }

            SLog.info("skuDesc[%s]", skuDesc.toString());
            permutation.specValueString = StringUtil.trim(skuDesc.toString(), new char[] {'/'});
            permutationList.add(permutation);
        }

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.viewpager);

        titleList.add("SKU商品");
        titleList.add("SKU圖片");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));

        fragmentList.add(SellerSkuGoodsListFragment.newInstance(permutationList));
        fragmentList.add(SellerSkuImageListFragment.newInstance(sellerSelectedSpecList.get(0).sellerSpecItemList, this));

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            popBefore();
        }
    }

    @Override
    public void onSimpleCall(Object data) {

    }

    /**
     * 用戶點擊返回後調用
     */
    private void popAfter() {
        Bundle bundle = new Bundle();

        EasyJSONObject result = EasyJSONObject.generate(
                "goodsJsonVoList", goodsJsonVoList,
                "goodsPicVoList", goodsPicVoList
        );

        bundle.putString("result", result.toString());

        setFragmentResult(RESULT_OK, bundle);

        hideSoftInputPop();
    }

    /**
     * 用戶點擊返回前調用
     */
    private void popBefore() {
        try {
            List<SellerSpecPermutation> sellerSpecPermutationList = ((SellerSkuGoodsListFragment) fragmentList.get(0)).collectSkuGoodsInfo();
            /*
            "goodsJsonVoList": [ #SKU集合 【必填】
                {
                    "goodsSpecs": "A+B,,,XXS", #規格值 多個用三個英文逗號,,,隔開
                    "goodsFullSpecs": "顔色：A+B；尺碼：XXS", #完整規格
                    "specValueIds": "16,35", #規格值ID，多個用英文逗號隔開
                    "goodsPrice0": 10, #sku價格
                    "goodsSerial": "", #商家編號
                    "goodsStorage": 20, #縂庫存
                    "colorId": 16, #主規格值ID
                    "reserveStorage": #預留庫存
                }
             ]
             */
            for (SellerSpecPermutation permutation : sellerSpecPermutationList) {
                StringBuilder specValueIds = new StringBuilder();
                for (SellerSpecItem sellerSpecItem : permutation.sellerSpecItemList) {
                    specValueIds.append(sellerSpecItem.id).append(",");
                }
                SLog.info("specValueIds[%s]", specValueIds);

                goodsJsonVoList.append(
                        EasyJSONObject.generate(
                                "specValueIds", StringUtil.trim(specValueIds.toString(), new char[] {','}),
                                "goodsPrice0", permutation.price,
                                "goodsSerial", permutation.goodsSN,
                                "goodsStorage", permutation.storage,
                                "colorId", permutation.sellerSpecItemList.get(0).id,
                                "reserveStorage", permutation.reserved
                        )
                );
            }


            EasyJSONObject skuImageObj = ((SellerSkuImageListFragment) fragmentList.get(1)).collectSkuImageInfo();

            if (skuImageObj == null) {
                ToastUtil.error(_mActivity, "收集數據錯誤");
                return;
            }

            SLog.info("skuImageObj[%s]", skuImageObj.toString());


            loadingPopup = (HwLoadingPopup) new XPopup.Builder(_mActivity)
                    .dismissOnBackPressed(false) // 按返回键是否关闭弹窗，默认为true
                    .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new HwLoadingPopup(_mActivity, "正在上傳商品圖片，請稍候..."));
            loadingPopup.show();

            uploadSkuImage(skuImageObj);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void uploadSkuImage(EasyJSONObject skuImageObj) {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                int count = 0;
                Set<String> keySet = skuImageObj.getHashMap().keySet();
                for (String key : keySet) {
                    int specValue = Integer.parseInt(key.substring(11));
                    SLog.info("key[%s], specValue[%d]", key, specValue);

                    int order = 0;
                    EasyJSONArray skuImageList = skuImageObj.getArray(key);
                    for (Object object : skuImageList) {
                        count++;
                        String absolutePath = (String) object;
                        SLog.info("正在上传第%d张图片", count);

                        String url = Api.syncUploadFile(new File(absolutePath));
                        if (!StringUtil.isEmpty(url)) {
                            EasyJSONObject goodsPicVo = EasyJSONObject.generate(
                                    "colorId", specValue,
                                    "imageName", url,
                                    "imageSort", order,
                                    "isDefault", (order == 0 ? Constant.TRUE_INT: Constant.FALSE_INT)
                            );
                            goodsPicVoList.append(goodsPicVo);

                            order++;
                        } else {
                            SLog.info("Error!上传失败");
                            emitter.onError(new Exception("Error!上传失败"));
                        }
                    }
                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                SLog.info("onSubscribe, threadId[%s]", Thread.currentThread().getId());
            }
            @Override
            public void onNext(String s) {
                SLog.info("onNext[%s], threadId[%s]", s, Thread.currentThread().getId());
            }
            @Override
            public void onError(Throwable e) {
//                ToastUtil.error();
                SLog.info("onError[%s], threadId[%s]", e.getMessage(), Thread.currentThread().getId());
                if (loadingPopup != null) {
                    loadingPopup.dismiss();
                }
            }
            @Override
            public void onComplete() {
                SLog.info("onComplete, threadId[%s]", Thread.currentThread().getId());
                if (loadingPopup != null) {
                    loadingPopup.dismiss();
                }

                popAfter();
            }
        };

        observable.subscribe(observer);
    }
}

