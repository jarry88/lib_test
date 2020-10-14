package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.widget.Poster;
import com.ftofs.twant.widget.SharePosterPopup;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.Jarbon;
import com.gzp.lib_common.utils.FileUtil;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;

import org.jetbrains.annotations.NotNull;
import org.urllib.Urls;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 生成海报图Fragment
 * @author zwm
 */
public class GeneratePosterFragment extends BaseFragment implements View.OnClickListener {
    int commonId;
    String goodsName;
    String goodsImageUrl;

    String goodsUrl; // 商品詳情頁的Url

    File goodsImageFile; // 商品圖片
    File posterFile; // 海報圖片
    File avatarFile; // 頭像圖片

    View loadingView;
    Poster poster;

    public static GeneratePosterFragment newInstance(int commonId, String goodsName, String goodsImageUrl) {
        Bundle args = new Bundle();

        GeneratePosterFragment fragment = new GeneratePosterFragment();
        fragment.setArguments(args);

        fragment.commonId = commonId;
        fragment.goodsName = goodsName;
        fragment.goodsImageUrl = StringUtil.normalizeImageUrl(goodsImageUrl, "?x-oss-process=image/resize,w_800");

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_poster, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingView = view.findViewById(R.id.loading_view);
        poster = view.findViewById(R.id.poster);

        // https://www.twant.com/web/goods/62208?goodsId=209951
        goodsUrl = Config.WEB_BASE_URL + "/goods/" + commonId;

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                String filename = Urls.parse(goodsImageUrl).path().filename();
                SLog.info("filename[%s]", filename);
                goodsImageFile = FileUtil.getCacheFile(_mActivity, filename);

                SLog.info("goodsImageUrl[%s]", goodsImageUrl);
                boolean success = Api.syncDownloadFile(goodsImageUrl, goodsImageFile);
                if (!success || !goodsImageFile.exists()) {
                    SLog.info("Error!产品图片下载失败, goodsImageUrl[%s]", goodsImageUrl);
                    emitter.onError(new Exception("產品圖片下載失敗"));
                    return;
                }

                String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR, null);
                SLog.info("avatarUrl[%s]", avatarUrl);
                if (!StringUtil.isEmpty(avatarUrl)) {
                    avatarUrl = StringUtil.normalizeImageUrl(avatarUrl, "?x-oss-process=image/resize,w_128");
                    filename = Urls.parse(avatarUrl).path().filename();
                    SLog.info("filename[%s]", filename);
                    avatarFile = FileUtil.getCacheFile(_mActivity, filename);

                    SLog.info("avatarUrl[%s]", avatarUrl);
                    success = Api.syncDownloadFile(avatarUrl, avatarFile);
                    if (!success || !avatarFile.exists()) {
                        SLog.info("Error!頭像图片下载失败, avatarUrl[%s]", avatarUrl);
                        emitter.onError(new Exception("頭像圖片下載失敗"));
                        return;
                    }
                }

                SLog.info("here____");
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
                SLog.info("onError[%s], threadId[%s]", e.getMessage(), Thread.currentThread().getId());

                ToastUtil.error(_mActivity, e.getMessage());
                hideSoftInputPop();
            }
            @Override
            public void onComplete() {
                SLog.info("onComplete, threadId[%s]", Thread.currentThread().getId());

                loadingView.setVisibility(View.GONE);
                if (avatarFile != null) { // 如果已經登錄，則設置頭像
                    poster.setAvatar(avatarFile);
                }
                poster.setGoodsImage(goodsImageFile)
                        .setGoodsName(goodsName)
                        .setQrCode(goodsUrl);
                poster.setVisibility(View.VISIBLE);
                poster.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = QMUIDrawableHelper.createBitmapFromView(poster);
                        posterFile = FileUtil.getCacheFile(_mActivity, Guid.getSpUuid() + ".jpg");
                        SLog.info("path[%s]", posterFile.getAbsoluteFile());
                        BitmapUtil.Bitmap2File(bitmap, posterFile, Bitmap.CompressFormat.JPEG,75);

                        ((MainActivity) _mActivity).showSharePosterPopup(posterFile);
                        hideSoftInputPop();
                    }
                }, 200);
            }
        };
        observable.subscribe(observer);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
