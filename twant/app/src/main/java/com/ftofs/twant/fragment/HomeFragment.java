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
public class HomeFragment extends BaseFragment {
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

        bannerView = view.findViewById(R.id.banner_view);
        loadCarouselImage();
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
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    int code = responseObj.getInt("code");
                    if (code != ResponseCode.SUCCESS) {
                        return null;
                    }
                    EasyJSONArray itemList = responseObj.getArray("datas.webSliderItem");
                    for (Object object : itemList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;
                        String imageUrl = Config.OSS_BASE_URL + "/" + itemObj.getString("image");
                        SLog.info("imageUrl[%s]", imageUrl);
                        String ext = PathUtil.getExtension(imageUrl);

                        String md5 = MD5.get(imageUrl);
                        // 新文件名重命名為路徑的md5值
                        String newFilename = md5 + "." + ext;
                        File imageFile = FileUtil.getCacheFile(_mActivity, "imageCache/" + newFilename);

                        if (!imageFile.exists()) {
                            // 文件已經不存在，則下載
                            boolean success = Api.syncDownloadFile(imageUrl, imageFile);
                            SLog.info("success[%s]", success);
                        }

                        imageFileList.add(imageFile);
                    }
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
                return imageFileList;
            }
        });
    }
}
