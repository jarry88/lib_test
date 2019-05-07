package com.ftofs.twant.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.MD5;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 首頁
 * @author zwm
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    LinearLayout llNewArrivalsContainer;
    MZBannerView bannerView;

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

        Util.setOnClickListener(view, R.id.btn_category, this);
        Util.setOnClickListener(view, R.id.ll_search_box, this);

        llNewArrivalsContainer = view.findViewById(R.id.ll_new_arrivals_container);
        bannerView = view.findViewById(R.id.banner_view);

        // 加載輪播圖片
        loadCarouselImage();
        // 加載最新入駐
        loadNewArrivals();
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
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
        }
    }

    public static class BannerViewHolder implements MZViewHolder<File> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.carousel_banner_item,null);
            mImageView = view.findViewById(R.id.img_banner);
            return view;
        }

        @Override
        public void onBind(Context context, int position, File file) {
            // 数据绑定
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mImageView.setImageBitmap(bitmap);
        }
    }


    /**
     * 加載輪播圖片
     */
    private void loadCarouselImage() {
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                List carouselList = (List) message;

                // 设置数据
                bannerView.setPages(carouselList, new MZHolderCreator<BannerViewHolder>() {
                    @Override
                    public BannerViewHolder createViewHolder() {
                        return new BannerViewHolder();
                    }
                });
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                List <File> imageFileList = new ArrayList<>();
                try {
                    String responseStr = Api.syncGet(Api.PATH_HOME_CAROUSEL, null);
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.isError(responseObj)) {
                        return null;
                    }
                    int code = responseObj.getInt("code");
                    if (code != ResponseCode.SUCCESS) {
                        return null;
                    }
                    EasyJSONArray itemList = responseObj.getArray("datas.webSliderItem");
                    for (Object object : itemList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;
                        String imageUrl = Config.OSS_BASE_URL + "/" + itemObj.getString("image");
                        SLog.info("imageUrl[%s]", imageUrl);
                        File imageFile = downloadIfNotExists(imageUrl);

                        imageFileList.add(imageFile);
                    }
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
                return imageFileList;
            }
        });
    }

    /**
     * 加載最新入駐
     */
    private void loadNewArrivals() {
        SLog.info("loadNewArrivals");
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                try {
                    List<EasyJSONObject> result = (List<EasyJSONObject>) message;
                    if (result == null) {
                        return;
                    }

                    SLog.info("store count[%d]", result.size());
                    for (EasyJSONObject storeObject : result) {
                        View storeView = LayoutInflater.from(_mActivity).inflate(R.layout.store_view, null, false);
                        ImageView imgStoreFigure = storeView.findViewById(R.id.img_store_figure);
                        Bitmap bitmap = BitmapFactory.decodeFile(storeObject.getString("figureImagePath"));
                        imgStoreFigure.setImageBitmap(bitmap);

                        // 獲取店鋪Id
                        int shopId = storeObject.getInt("storeId");

                        // 設置店鋪名稱
                        TextView tvStoreName = storeView.findViewById(R.id.tv_store_name);
                        tvStoreName.setText(storeObject.getString("storeName"));
                        // 設置店鋪類別
                        TextView tvStoreClass = storeView.findViewById(R.id.tv_store_class);
                        String className = storeObject.getString("className");
                        // 拆分中英文
                        String[] classNameArr = className.split(",");
                        tvStoreClass.setText(classNameArr[0]);
                        // 設置點贊數據
                        TextView tvLikeData = storeView.findViewById(R.id.tv_like_data);
                        String likeDataStr = getResources().getString(R.string.text_like) + " " + storeObject.getInt("likeCount");
                        tvLikeData.setText(likeDataStr);
                        // 設置關注數據
                        TextView tvFollowData = storeView.findViewById(R.id.tv_follow_data);
                        String followDataStr = getResources().getString(R.string.text_follow) + " " + storeObject.getInt("followCount");
                        tvFollowData.setText(followDataStr);

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
                                mainFragment.start(ShopMainFragment.newInstance());
                            }
                        });
                        llNewArrivalsContainer.addView(storeView, layoutParams);
                    }

                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                String token = User.getToken();
                SLog.info("token[%s]", token);
                // 必須是登錄用戶才可以
                if (StringUtil.isEmpty(token)) {
                    return null;
                }

                EasyJSONObject params = EasyJSONObject.generate(
                        "token", token
                );


                String responseStr = Api.syncPost(Api.PATH_NEW_ARRIVALS, params);
                SLog.info("PATH_NEW_ARRIVALS, responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.isError(responseObj)) {
                    SLog.info("Error!responseObj is invalid");
                    return null;
                }

                try {
                    EasyJSONArray storeList = responseObj.getArray("datas.storeList");
                    SLog.info("storeList size[%d]", storeList.length());
                    List<EasyJSONObject> storeObjectList = new ArrayList<>();
                    for (Object object : storeList) {
                        EasyJSONObject store = (EasyJSONObject) object;
                        String storeFigureImageUrl = Config.OSS_BASE_URL + "/" + store.getString("storeVo.storeFigureImage");
                        SLog.info("storeFigureImageUrl[%s]", storeFigureImageUrl);
                        // 如果店鋪圖片不存在，則下載
                        File imageFile = downloadIfNotExists(storeFigureImageUrl);
                        if (imageFile == null) {
                            continue;
                        }

                        // 打包店鋪數據
                        EasyJSONObject storeObject = EasyJSONObject.generate(
                                "storeId", store.getInt("storeVo.storeId"),
                                "storeName", store.getString("storeVo.storeName"),
                                "className", store.getString("storeVo.className"),
                                "figureImagePath", imageFile.getAbsolutePath(),
                                "likeCount", store.getInt("storeVo.likeCount"),
                                "followCount", store.getInt("storeVo.collectCount"));

                        storeObjectList.add(storeObject);
                    }
                    return storeObjectList;
                } catch (EasyJSONException e) {
                    SLog.info("Error!%s", e.getMessage());
                }

                return null;
            }
        });
    }


    /**
     * 如果文件不存在，則下載
     * @param url 文件的url
     * @return 如果下載不成功，返回null
     */
    private File downloadIfNotExists(String url) {
        String ext = PathUtil.getExtension(url);

        String md5 = MD5.get(url);
        // 新文件名重命名為路徑的md5值
        String newFilename = md5 + "." + ext;
        File file = FileUtil.getCacheFile(_mActivity, "imageCache/" + newFilename);

        if (!file.exists()) {
            // 文件已經不存在，則下載
            boolean success = Api.syncDownloadFile(url, file);
            SLog.info("success[%s]", success);
            if (!success) {
                return null;
            }
        }

        return file;
    }
}
