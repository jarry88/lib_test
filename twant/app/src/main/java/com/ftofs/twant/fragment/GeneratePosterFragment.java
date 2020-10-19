package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.QRCode;
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

import cn.snailpad.easyjson.EasyJSONObject;
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
    // 海報類型
    int posterType;
    EasyJSONObject data; // 傳進來的參數


    int commonId;
    String goodsName;
    String goodsImageUrl;

    String goodsUrl; // 商品詳情頁的Url

    String marketingUrl; // 邀請分銷Url

    File goodsImageFile; // 商品圖片
    File posterFile; // 海報圖片
    File avatarFile; // 頭像圖片

    View loadingView;
    Poster poster;

    // 邀請海報
    ImageView imgInvitationAvatar;
    TextView tvInvitationNickname;
    ImageView imgInvitationQrCode;
    View invitationPoster;

    public static GeneratePosterFragment newInstance(int posterType, EasyJSONObject data) {
        Bundle args = new Bundle();

        GeneratePosterFragment fragment = new GeneratePosterFragment();
        fragment.setArguments(args);

        fragment.posterType = posterType;
        fragment.data = data;

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
        invitationPoster = view.findViewById(R.id.invitation_poster);

        if (posterType == Constant.POSTER_TYPE_GOODS) {
            commonId = data.optInt("commonId");
            goodsName = data.optString("goodsName");
            goodsImageUrl = StringUtil.normalizeImageUrl(data.optString("goodsImageUrl"), "?x-oss-process=image/resize,w_800");

            generateGoodsPoster();
        } else if (posterType == Constant.POSTER_TYPE_INVITATION) {
            marketingUrl = data.optString("marketingUrl");

            imgInvitationAvatar = view.findViewById(R.id.img_invitation_avatar);
            tvInvitationNickname = view.findViewById(R.id.tv_invitation_nickname);
            imgInvitationQrCode = view.findViewById(R.id.img_invitation_qr_code);

            generateInvitationPoster();
        }
    }

    private void generateGoodsPoster() {
        // https://www.twant.com/web/goods/62208?goodsId=209951
        goodsUrl = Config.WEB_BASE_URL + "/goods/" + commonId;

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                String filename = Urls.parse(goodsImageUrl).path().filename();
                SLog.info("filename[%s]", filename);
                goodsImageFile = FileUtil.getCacheFile(_mActivity, filename);

                if (!goodsImageFile.exists()) {
                    SLog.info("goodsImageUrl[%s]", goodsImageUrl);
                    boolean success = Api.syncDownloadFile(goodsImageUrl, goodsImageFile);
                    if (!success || !goodsImageFile.exists()) {
                        SLog.info("Error!产品图片下载失败, goodsImageUrl[%s]", goodsImageUrl);
                        emitter.onError(new Exception("產品圖片下載失敗"));
                        return;
                    }
                }


                String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR, null);
                SLog.info("avatarUrl[%s]", avatarUrl);
                if (!StringUtil.isEmpty(avatarUrl)) {
                    avatarUrl = StringUtil.normalizeImageUrl(avatarUrl, "?x-oss-process=image/resize,w_128");
                    filename = Urls.parse(avatarUrl).path().filename();
                    SLog.info("filename[%s]", filename);
                    avatarFile = FileUtil.getCacheFile(_mActivity, filename);

                    if (!avatarFile.exists()) {
                        SLog.info("avatarUrl[%s]", avatarUrl);
                        boolean success = Api.syncDownloadFile(avatarUrl, avatarFile);
                        if (!success || !avatarFile.exists()) {
                            SLog.info("Error!頭像图片下载失败, avatarUrl[%s]", avatarUrl);
                            emitter.onError(new Exception("頭像圖片下載失敗"));
                            return;
                        }
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

                String nickname = User.getUserInfo(SPField.FIELD_NICKNAME, "");
                if (!StringUtil.isEmpty(nickname)) {
                    poster.setNickname(nickname);
                }

                double mopPrice = data.optDouble("mopPrice");
                double cnyPrice = data.optDouble("cnyPrice");

                poster.setGoodsImage(goodsImageFile)
                        .setGoodsName(goodsName)
                        .setQrCode(goodsUrl)
                        .setMopPrice(mopPrice)
                        .setCnyPrice(cnyPrice);

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


    private void generateInvitationPoster() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR, null);
                SLog.info("avatarUrl[%s]", avatarUrl);
                if (!StringUtil.isEmpty(avatarUrl)) {
                    avatarUrl = StringUtil.normalizeImageUrl(avatarUrl, "?x-oss-process=image/resize,w_128");
                    String filename = Urls.parse(avatarUrl).path().filename();
                    SLog.info("filename[%s]", filename);
                    avatarFile = FileUtil.getCacheFile(_mActivity, filename);

                    if (!avatarFile.exists()) {
                        SLog.info("avatarUrl[%s]", avatarUrl);
                        boolean success = Api.syncDownloadFile(avatarUrl, avatarFile);
                        if (!success || !avatarFile.exists()) {
                            SLog.info("Error!頭像图片下载失败, avatarUrl[%s]", avatarUrl);
                            emitter.onError(new Exception("頭像圖片下載失敗"));
                            return;
                        }
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
                    Glide.with(_mActivity).load(avatarFile).centerCrop().into(imgInvitationAvatar);
                }

                String nickname = User.getUserInfo(SPField.FIELD_NICKNAME, "");
                tvInvitationNickname.setText(nickname);
                String marketingUrl = data.optString("marketingUrl");
                SLog.info("marketingUrl[%s]", marketingUrl);
                Bitmap qrCode = QRCode.createQRCode(marketingUrl);
                Glide.with(imgInvitationQrCode).load(qrCode).centerCrop().into(imgInvitationQrCode);
                invitationPoster.setVisibility(View.VISIBLE);
                invitationPoster.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = QMUIDrawableHelper.createBitmapFromView(invitationPoster);
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
