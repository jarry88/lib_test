package com.ftofs.twant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 首頁
 * @author zwm
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    LinearLayout llNewArrivalsContainer;
    MZBannerView bannerView;

    boolean carouselLoaded;
    boolean newArrivalsLoaded;

    List<WebSliderItem> webSliderItemList = new ArrayList<>();

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_category, this);
        Util.setOnClickListener(view, R.id.ll_search_box, this);
        Util.setOnClickListener(view, R.id.btn_message, this);

        llNewArrivalsContainer = view.findViewById(R.id.ll_new_arrivals_container);
        bannerView = view.findViewById(R.id.banner_view);
        bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                SLog.info("i = %d", i);
                WebSliderItem webSliderItem = webSliderItemList.get(i);
                String linkType = webSliderItem.linkType;

                MainFragment mainFragment = MainFragment.getInstance();

                switch (linkType) {
                    case "none":
                        // 无操作
                        break;
                    case "url":
                        // 外部鏈接
                        // String url = "https://www.jianshu.com/p/596a168c33d7";
                        String url = webSliderItem.linkValue;
                        mainFragment.start(ExplorerFragment.newInstance(url));
                        break;
                    case "keyword":
                        // 关键字
                        String keyword = webSliderItem.linkValue;
                        mainFragment.start(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("keyword", keyword).toString()));
                        break;
                    case "goods":
                        // 商品
                        int commonId = Integer.valueOf(webSliderItem.linkValue);
                        mainFragment.start(GoodsDetailFragment.newInstance(commonId));
                        break;
                    case "store":
                        // 店铺
                        int storeId = Integer.valueOf(webSliderItem.linkValue);
                        mainFragment.start(ShopMainFragment.newInstance(storeId));
                        break;
                    case "category":
                        // 商品搜索结果页(分类)
                        String cat = webSliderItem.linkValue;
                        mainFragment.start(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("cat", cat).toString()));
                        break;
                    case "brandList":
                        // 品牌列表
                        break;
                    case "voucherCenter":
                        // 领券中心
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        SLog.info("carouselLoaded[%s], newArrivalsLoaded[%s]", carouselLoaded, newArrivalsLoaded);
        // 加載輪播圖片
        if (!carouselLoaded) {
            loadCarousel();
        }

        // 加載最新入駐
        if (!newArrivalsLoaded) {
            loadNewArrivals();
        }

        bannerView.start();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        bannerView.pause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_category) {
            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(CategoryFragment.newInstance());
        } else if (id == R.id.ll_search_box) {
            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(SearchFragment.newInstance());
        } else if (id == R.id.btn_test) {
            if (Config.DEVELOPER_MODE) {
                MainFragment mainFragment = (MainFragment) getParentFragment();
                mainFragment.start(TestFragment.newInstance());
            }
        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        SLog.info("onSelected, type[%d], id[%d], extra[%s]", type, id, extra);
    }

    public static class BannerViewHolder implements MZViewHolder<WebSliderItem> {
        private ImageView mImageView;

        public static final int GOODS_IMAGE_COUNT = 3;
        ImageView imgDesktop;
        ImageView[] goodsImageArr = new ImageView[GOODS_IMAGE_COUNT];

        List<WebSliderItem> webSliderItemList;

        public BannerViewHolder(List<WebSliderItem> webSliderItemList) {
            this.webSliderItemList = webSliderItemList;
        }

        public void setGoodsImageVisibility(int visibility) {
            for (int i = 0; i < GOODS_IMAGE_COUNT; ++i) {
                goodsImageArr[i].setVisibility(visibility);
            }
        }

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.carousel_banner_item,null);
            mImageView = view.findViewById(R.id.img_banner);

            imgDesktop = view.findViewById(R.id.img_goods_desktop);
            goodsImageArr[0] = view.findViewById(R.id.goods_image1);
            goodsImageArr[1] = view.findViewById(R.id.goods_image2);
            goodsImageArr[2] = view.findViewById(R.id.goods_image3);
            return view;
        }

        @Override
        public void onBind(Context context, int position, WebSliderItem webSliderItem) {
            try {
                // 数据绑定
                String imageUrl = Config.OSS_BASE_URL + "/" + webSliderItem.image;
                // SLog.info("imageUrl[%s]", imageUrl);
                Glide.with(context).load(imageUrl).centerCrop().into(mImageView);

                if (webSliderItem.linkType.equals("store")) {
                    imgDesktop.setVisibility(View.VISIBLE);
                    setGoodsImageVisibility(View.VISIBLE);

                    String goodsCommons = webSliderItem.goodsCommons;
                    EasyJSONArray goodsArray = (EasyJSONArray) EasyJSONArray.parse(goodsCommons);

                    int i = 0;
                    for (Object object : goodsArray) {
                        EasyJSONObject goods = (EasyJSONObject) object;
                        String goodsImage = Config.OSS_BASE_URL + "/" + goods.getString("goodsImage");
                        Glide.with(context).load(goodsImage).centerCrop().into(goodsImageArr[i]);
                        ++i;
                    }
                } else {
                    imgDesktop.setVisibility(View.GONE);
                    setGoodsImageVisibility(View.GONE);
                }
            } catch (Exception e) {

            }
        }
    }


    /**
     * 加載輪播圖片
     */
    private void loadCarousel() {
        Api.getUI(Api.PATH_HOME_CAROUSEL, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray itemList = responseObj.getArray("datas.webSliderItem");
                    webSliderItemList.clear();
                    for (Object object : itemList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;

                        WebSliderItem webSliderItem = new WebSliderItem(
                                itemObj.getString("image"),
                                itemObj.getString("linkType"),
                                itemObj.getString("linkValue"),
                                itemObj.getString("goodsIds"),
                                itemObj.getString("goodsCommons"));
                        webSliderItemList.add(webSliderItem);
                    }

                    // 设置数据
                    bannerView.setPages(webSliderItemList, new MZHolderCreator<BannerViewHolder>() {
                        @Override
                        public BannerViewHolder createViewHolder() {
                            return new BannerViewHolder(webSliderItemList);
                        }
                    });
                    bannerView.start();
                } catch (Exception e) {

                }
                carouselLoaded = true;
            }
        });
    }

    /**
     * 加載最新入駐
     */
    private void loadNewArrivals() {
        SLog.info("loadNewArrivals");

        String token = User.getToken();
        SLog.info("token[%s]", token);

        try {
            EasyJSONObject params = EasyJSONObject.generate();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            Api.postUI(Api.PATH_NEW_ARRIVALS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("PATH_NEW_ARRIVALS, responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        SLog.info("Error!responseObj is invalid");
                        return;
                    }

                    try {
                        EasyJSONArray storeList = responseObj.getArray("datas.storeList");
                        SLog.info("storeList size[%d]", storeList.length());

                        llNewArrivalsContainer.removeAllViews();
                        for (Object object : storeList) {
                            View storeView = LayoutInflater.from(_mActivity).inflate(R.layout.store_view, llNewArrivalsContainer, false);

                            EasyJSONObject store = (EasyJSONObject) object;

                            // 獲取店鋪Id
                            final int storeId = store.getInt("storeVo.storeId");

                            // 設置店鋪名稱
                            String storeName = store.getString("storeVo.storeName");
                            TextView tvStoreName = storeView.findViewById(R.id.tv_store_name);
                            tvStoreName.setText(storeName);

                            // 設置店鋪類別
                            TextView tvStoreClass = storeView.findViewById(R.id.tv_store_class);
                            String className = store.getString("storeVo.className");
                            String[] classNameArr = className.split(",");  // 拆分中英文
                            tvStoreClass.setText(classNameArr[0]);


                            // 店鋪形象圖
                            String storeFigureImageUrl = Config.OSS_BASE_URL + "/" + store.getString("storeVo.storeFigureImage");
                            // SLog.info("storeFigureImageUrl[%s]", storeFigureImageUrl);
                            ImageView imgStoreFigure = storeView.findViewById(R.id.img_store_figure);
                            Glide.with(_mActivity).load(storeFigureImageUrl).centerCrop().into(imgStoreFigure);


                            // 設置點贊數據
                            int likeCount = store.getInt("storeVo.likeCount");
                            TextView tvLikeData = storeView.findViewById(R.id.tv_like_data);
                            String likeDataStr = getResources().getString(R.string.text_like) + " " + likeCount;
                            tvLikeData.setText(likeDataStr);


                            // 設置關注數據
                            int followCount = store.getInt("storeVo.collectCount");
                            TextView tvFollowData = storeView.findViewById(R.id.tv_follow_data);
                            String followDataStr = getResources().getString(R.string.text_follow) + " " + followCount;
                            tvFollowData.setText(followDataStr);


                            int index = 0;
                            // 店鋪的3個商品展示
                            for (Object object2 : store.getArray("goodsList")) {
                                EasyJSONObject goodsObject = (EasyJSONObject) object2;
                                String imageSrc = goodsObject.getString("imageSrc");

                                String uri = Config.OSS_BASE_URL + "/" + imageSrc;
                                if (index == 0) {
                                    ImageView goodsImageLeft = storeView.findViewById(R.id.goods_image_left);
                                    Glide.with(_mActivity).load(uri).centerCrop().into(goodsImageLeft);
                                    storeView.findViewById(R.id.goods_image_left_container).setVisibility(View.VISIBLE);
                                } else if (index == 1) {
                                    ImageView goodsImageMiddle = storeView.findViewById(R.id.goods_image_middle);
                                    Glide.with(_mActivity).load(uri).centerCrop().into(goodsImageMiddle);
                                    storeView.findViewById(R.id.goods_image_middle_container).setVisibility(View.VISIBLE);
                                } else if (index == 2) {
                                    ImageView goodsImageRight = storeView.findViewById(R.id.goods_image_right);
                                    Glide.with(_mActivity).load(uri).centerCrop().into(goodsImageRight);
                                    storeView.findViewById(R.id.goods_image_right_container).setVisibility(View.VISIBLE);
                                }

                                ++index;
                            }

                            // 添加控件到容器中
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            int marginTop = Util.dip2px(_mActivity, 15);
                            int marginBottom = Util.dip2px(_mActivity, 20);
                            layoutParams.setMargins(0, marginTop, 0, marginBottom);


                            storeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MainFragment mainFragment = (MainFragment) getParentFragment();
                                    mainFragment.start(ShopMainFragment.newInstance(storeId));
                                }
                            });
                            llNewArrivalsContainer.addView(storeView, layoutParams);
                        }
                    } catch (Exception e) {

                    }

                    newArrivalsLoaded = true;
                }
            });
        } catch (EasyJSONException e) {
            SLog.info("Error!%s", e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
    }
}
