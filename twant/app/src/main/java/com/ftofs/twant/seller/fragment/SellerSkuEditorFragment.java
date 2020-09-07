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
import com.ftofs.twant.interfaces.EditorResultInterface;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerGoodsPicVo;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

/**
 * 【賣家】SKU信息頁
 * @author zwm
 */
public class SellerSkuEditorFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    EditorResultInterface editorResultInterface;
    List<String> specValueIdStringList;
    Map<String, SellerSpecPermutation> specValueIdStringMap;
    SellerSpecMapItem colorSpecMapItem;
    Map<Integer, List<SellerGoodsPicVo>> colorImageMap;  // colorId與圖片列表的映射關係

    List<SellerSpecPermutation> permutationList = new ArrayList<>();

    EasyJSONArray goodsJsonVoList = EasyJSONArray.generate();
    EasyJSONArray goodsPicVoList = EasyJSONArray.generate();

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    HwLoadingPopup loadingPopup;
    private boolean loaded;
    boolean isConsult;

    public static SellerSkuEditorFragment newInstance(
            EditorResultInterface editorResultInterface,
            List<String> specValueIdStringList,
            Map<String, SellerSpecPermutation> specValueIdStringMap,
            SellerSpecMapItem colorSpecMapItem,  // 颜色规格，如果没选颜色时，则为null
            Map<Integer, List<SellerGoodsPicVo>> colorImageMap  // 对应的图片对象的列表
    ) {
        return newInstance(editorResultInterface, specValueIdStringList, specValueIdStringMap, colorSpecMapItem, colorImageMap, false);
    }


    public static SellerSkuEditorFragment newInstance(
            EditorResultInterface editorResultInterface,
            List<String> specValueIdStringList,
            Map<String, SellerSpecPermutation> specValueIdStringMap,
            SellerSpecMapItem colorSpecMapItem,  // 颜色规格，如果没选颜色时，则为null
            Map<Integer, List<SellerGoodsPicVo>> colorImageMap,  // 对应的图片对象的列表
            boolean isConsult) {
        Bundle args = new Bundle();

        SellerSkuEditorFragment fragment = new SellerSkuEditorFragment();
        fragment.setArguments(args);
        fragment.editorResultInterface = editorResultInterface;
        fragment.specValueIdStringList = specValueIdStringList;
        fragment.specValueIdStringMap = specValueIdStringMap;
        fragment.colorSpecMapItem = colorSpecMapItem;
        fragment.colorImageMap = colorImageMap;
        fragment.isConsult = isConsult;

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

        // 生成PermutationList
        for (String specValueIdString : specValueIdStringList) {
            SellerSpecPermutation permutation = specValueIdStringMap.get(specValueIdString);
            permutationList.add(permutation);
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);


        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.viewpager);

        titleList.add("SKU商品");
        titleList.add("SKU圖片");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));

        fragmentList.add(SellerSkuGoodsListFragment.newInstance(permutationList));
        fragmentList.add(SellerSkuImageListFragment.newInstance(colorSpecMapItem, colorImageMap));

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

        if (isConsult) { // 如果是諮詢型商品，默認切換到[SKU圖片]Tab
            viewPager.setCurrentItem(1);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            if (loaded) {
                hideSoftInputPop();
            } else {
                new XPopup.Builder(_mActivity)
                        .asCustom((new TwConfirmPopup(_mActivity, "确认离开？", null, "确定离开", "继续编辑", new OnConfirmCallback() {
                            @Override
                            public void onYes() {
                                SLog.info("onYes");
                                hideSoftInputPop();
                            }

                            @Override
                            public void onNo() {
                                SLog.info("onNo");
                            }
                        }))).show();
            }
                    }
        if (id == R.id.btn_ok) {
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
        if (editorResultInterface != null) {
            editorResultInterface.setEditorResult(specValueIdStringMap, colorImageMap);
        }

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

            colorImageMap = ((SellerSkuImageListFragment) fragmentList.get(1)).collectSkuImageInfo();

            loadingPopup = (HwLoadingPopup) new XPopup.Builder(_mActivity)
                    .dismissOnBackPressed(false) // 按返回键是否关闭弹窗，默认为true
                    .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new HwLoadingPopup(_mActivity, "正在上傳商品圖片，請稍候..."));
            loadingPopup.show();

            uploadSkuImage();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void uploadSkuImage() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                int count = 0;
                Set<Integer> keySet = colorImageMap.keySet();
                for (int colorId : keySet) {
                    SLog.info("colorId[%d]", colorId);

                    int order = 0;  // 表示第幾張圖片，從1開始
                    List<SellerGoodsPicVo> skuImageList = colorImageMap.get(colorId);
                    for (SellerGoodsPicVo picVo : skuImageList) {
                        order++;
                        if (StringUtil.isEmpty(picVo.absolutePath)) {
                            continue;
                        }

                        count++;
                        SLog.info("正在上传第%d张图片[%s]", count, picVo.absolutePath);
                        picVo.imageSort = order - 1;
                        picVo.isDefault = ((order == 1) ? Constant.TRUE_INT : Constant.FALSE_INT);

                        String url = Api.syncUploadFile(new File(picVo.absolutePath));
                        if (!StringUtil.isEmpty(url)) {
                            // 上傳成功
                            picVo.imageName = url;
                            picVo.absolutePath = null;
                            EasyJSONObject goodsPicVo = EasyJSONObject.generate(
                                    "colorId", colorId,
                                    "imageName", url,
                                    "imageSort", order - 1,  // imageSort從零開始
                                    "isDefault", (order == 1 ? Constant.TRUE_INT: Constant.FALSE_INT)
                            );
                            goodsPicVoList.append(goodsPicVo);
                            loaded = true;
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

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

