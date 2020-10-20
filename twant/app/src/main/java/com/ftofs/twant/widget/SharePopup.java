package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.GeneratePosterFragment;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.Jarbon;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.task.TaskObservable;
import com.gzp.lib_common.task.TaskObserver;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.ClipboardUtils;
import com.gzp.lib_common.utils.FileUtil;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.ImageProcess;
import com.ftofs.twant.util.LogUtil;
import com.gzp.lib_common.utils.PathUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.WeixinUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;

import org.urllib.Urls;

import java.io.File;
import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


/**
 * 分享底部彈窗
 * @author zwm
 */
public class SharePopup extends BottomPopupView implements View.OnClickListener {
    int shareType;
    public static final int SHARE_TYPE_STORE = 1;  // 分享店鋪
    public static final int SHARE_TYPE_GOODS = 2;  // 分享商品
    public static final int SHARE_TYPE_POSTER = 3; // 分享海報

    // 記錄上次點擊的時間，防止點擊過快
    long lastClickTime = 0;
    // 過快點擊的時間間隔，500毫秒
    public static final int FAST_CLICK_INTERVAL = 500;

    /**
     * 分享商店的鏈接格式(1234為商店Id)
     * Config.WEB_BASE_URL + /store/1234
     *
     * 分享產品的鏈接格式(1234為spuId, 5678為skuId)
     * Config.WEB_BASE_URL + /goods/1234?goodsId=5678
     *
     * 分享想要帖的鏈接格式(1234為想要帖Id)
     * Config.WEB_BASE_URL + /wantpost/detail/1234
     */



    Context context;
    String shareUrl; // 分享的鏈接

    String title;
    String description;
    String coverUrl; // 分享的封面圖片的URL

    Object data; // 當shareType為SHARE_TYPE_STORE或SHARE_TYPE_GOODS才使用此數據
    // boolean showWordIcon;

    View btnCopyLink;
    View btnShareToTakewantCircle;
    View btnShareToWord;
    View btnShareToPoster;

    int commonId;
    String goodsName;
    int goodsModel;
    String goodsImageUrl;
    String goodsWord;  // 商品分享口令
    String storeWord;  // 店鋪分享口令

    String marketingUrl; // 分銷系統分享鏈接
    double goodsPrice; // 商品價格
    double cnyPrice;   // 人民幣價格

    /*
    public SharePopup(@NonNull Context context, String shareUrl, String title, String description, String coverUrl, Object data) {
        this(context, shareUrl, title, description, coverUrl, data, false);
    }

     */

    public SharePopup(@NonNull Context context, String shareUrl, String title, String description, String coverUrl, Object data) {
        super(context);

        this.context = context;
        this.shareUrl = shareUrl;
        this.title = title;
        this.description = description;
        this.coverUrl = StringUtil.normalizeImageUrl(coverUrl);
        this.data = data;
        // this.showWordIcon = showWordIcon;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.share_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        findViewById(R.id.btn_share_to_friend).setOnClickListener(this);
        findViewById(R.id.btn_share_to_timeline).setOnClickListener(this);
        findViewById(R.id.btn_share_to_facebook).setOnClickListener(this);
        btnCopyLink = findViewById(R.id.btn_copy_link);
        btnCopyLink.setOnClickListener(this);

        btnShareToTakewantCircle = findViewById(R.id.btn_share_to_takewant_circle);
        if (data != null) {
            btnShareToTakewantCircle.setOnClickListener(this);
        } else {
            btnShareToTakewantCircle.setVisibility(GONE);
        }

        btnShareToWord = findViewById(R.id.btn_share_to_word);
        // 判斷是否為商品分享，如果是，就生成分享口令
        try {
            if (data != null && data instanceof EasyJSONObject) {
                EasyJSONObject shareData = (EasyJSONObject) data;
                if (shareData.exists("shareType")) {
                    shareType = shareData.getInt("shareType");
                    if (shareType == SHARE_TYPE_GOODS) {
                        commonId = shareData.optInt("commonId");
                        goodsName = shareData.optString("goodsName");
                        goodsModel = shareData.optInt("goodsModel");
                        goodsImageUrl = shareData.optString("goodsImage");
                        goodsPrice = shareData.optDouble("goodsPrice");
                        cnyPrice = shareData.optDouble("cnyPrice");
                        getCommandWord(shareType, commonId);

                        btnShareToWord.setOnClickListener(this);
                        btnShareToWord.setVisibility(VISIBLE);
                    } else if (shareType == SHARE_TYPE_STORE) {
                        int storeId = shareData.getInt("storeId");
                        getCommandWord(shareType, storeId);

                        btnShareToWord.setOnClickListener(this);
                        btnShareToWord.setVisibility(VISIBLE);
                    } else if (shareType == SHARE_TYPE_POSTER) {
                        marketingUrl = shareData.optString("marketingUrl");
                        SLog.info("marketingUrl[%s]", marketingUrl);
                    }
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        if (commonId > 0 || shareType == SHARE_TYPE_POSTER) { // 如果是商品分享 或 海報分享， 則顯示海報按鈕
            btnShareToPoster = findViewById(R.id.btn_share_to_poster);
            btnShareToPoster.setVisibility(VISIBLE);
            btnShareToPoster.setOnClickListener(this);
        }
    }

    /**
     * 獲取商品或店鋪口令碼
     * @param type 类型，店铺或商品
     * @param id
     */
    private void getCommandWord(int type, int id) {
        String url;
        EasyJSONObject params;

        if (type == SHARE_TYPE_STORE) {
            url = Api.PATH_STORE_CREATE_WORD;
            params = EasyJSONObject.generate(
                    "storeId", id
            );
        } else {
            url = Api.PATH_GOODS_CREATE_WORD;
            params = EasyJSONObject.generate(
                    "commonId", id
            );
        }

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(context, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                try {
                    if (type == SHARE_TYPE_GOODS) {
                        goodsWord = responseObj.getSafeString("datas.command");
                    } else {
                        storeWord = responseObj.getSafeString("datas.command");
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }




    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();

        int btnCopyLinkWidth = btnCopyLink.getWidth();
        SLog.info("btnCopyLinkWidth[%d]", btnCopyLinkWidth);
        ViewGroup.LayoutParams layoutParams = btnShareToTakewantCircle.getLayoutParams();
        layoutParams.width = btnCopyLinkWidth;

    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_share_to_friend || id == R.id.btn_share_to_timeline) {
            // 檢測微信是否已經安裝
            if (TwantApplication.Companion.get().getWxApi()!=null&&!TwantApplication.Companion.get().getWxApi().isWXAppInstalled()) {
                ToastUtil.error(context, context.getString(R.string.weixin_not_installed_hint));
                return;
            }

            if (isFastClick()) {
                return;
            }

            int scene;
            if (id == R.id.btn_share_to_friend) {
                scene = WeixinUtil.WX_SCENE_SESSION;
            } else {
                scene = WeixinUtil.WX_SCENE_TIMELINE;
            }

            if (StringUtil.isEmpty(coverUrl)) {  // 如果沒有封面圖片
                shareToWeixin(scene, null);
                return;
            }

            // 下載封面圖片
            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    String filepath = (String) message;
                    if (StringUtil.isEmpty(filepath)) {
                        ToastUtil.error(context, "處理微信分享圖片失敗");
                        return;
                    }
                    shareToWeixin(scene, filepath);
                }
            };
            TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    try {
                        String filename = Urls.parse(coverUrl).path().filename();
                        String ext = PathUtil.getExtension(filename, true);
                        SLog.info("coverUrl[%s], filename[%s]", coverUrl, filename);
                        File file = FileUtil.getCacheFile(context, filename);
                        if (Api.syncDownloadFile(coverUrl, file)) {
                            SLog.info("封面圖片下載成功[%s]", file.getAbsolutePath());
                            // 裁剪圖片大小在微信限制范圍內
                            String thumbFilename = Guid.getSpUuid() + "." + ext;
                            SLog.info("thumbFilename[%s]", thumbFilename);
                            File thumb = FileUtil.getCacheFile(context, thumbFilename);
                            ImageProcess.with(context).from(file).centerCrop().resize(160, 160).toFile(thumb.getAbsolutePath());
                            SLog.info("thumb[%s]", thumb.getAbsolutePath());
                            return thumb.getAbsolutePath();
                        } else {
                            SLog.info("Error!封面圖片下載失敗");
                            return null;
                        }
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        return null;
                    }
                }
            });
        } else if (id == R.id.btn_share_to_facebook) {
            if (isFastClick()) {
                return;
            }

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(shareUrl))
                    .build();

            //调用分享弹窗
            ShareDialog.show(MainActivity.getInstance(), content);
        } else if (id == R.id.btn_copy_link) {
            ClipboardUtils.copyText(context, shareUrl);
            new XPopup.Builder(context)
//                         .dismissOnTouchOutside(false)
        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                  .asCustom(new TwConfirmPopup(context, "分享鏈接已復制", shareUrl, new OnConfirmCallback() {
                @Override
                public void onYes() {
                    SLog.info("onYes");
                }

                @Override
                public void onNo() {
                    SLog.info("onNo");
                }
            }))
                    .show();
            dismiss();
        } else if (id == R.id.btn_share_to_takewant_circle) { // 分享到想要圈
            if (isFastClick()) {
                return;
            }

            if (data == null || !(data instanceof EasyJSONObject)) {
                return;
            }

            EasyJSONObject dataObj = (EasyJSONObject) data;
            ApiUtil.addPost(getContext(),false,dataObj);
//            Util.startFragment(AddPostFragment.newInstance(dataObj, false));
            dismiss();
        } else if (id == R.id.btn_share_to_word) { // 分享口令碼
            String word = null;
            if (shareType == SHARE_TYPE_STORE) {
                word = storeWord;
            } else if (shareType == SHARE_TYPE_GOODS) {
                word = goodsWord;
            }
            if (StringUtil.isEmpty(word)) {
                ToastUtil.error(context, "口令為空，請稍後再試");
                return;
            }
            new XPopup.Builder(context)
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                    .asCustom(new WordSharePopup(context, word)).show();

            dismiss();
        } else if (id == R.id.btn_share_to_poster) { // 分享海報
            if (commonId > 0) { // 分享商品海報
                EasyJSONObject posterData = EasyJSONObject.generate(
                        "commonId", commonId,
                        "goodsName", goodsName,
                        "goodsModel", goodsModel,
                        "goodsImageUrl", goodsImageUrl,
                        "mopPrice", goodsPrice,
                        "cnyPrice", cnyPrice
                );
                Util.startFragment(GeneratePosterFragment.newInstance(Constant.POSTER_TYPE_GOODS, posterData));
            } else { // 分享邀請海報
                EasyJSONObject posterData = EasyJSONObject.generate(
                        "marketingUrl", marketingUrl
                );
                Util.startFragment(GeneratePosterFragment.newInstance(Constant.POSTER_TYPE_INVITATION, posterData));
            }

            dismiss();
        }
    }


    /**
     * 判斷是否點擊過快，如果不是，則記錄最近點擊的時間
     * @return
     */
    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < FAST_CLICK_INTERVAL) {
            SLog.info("點擊過快");
            return true;
        }
        lastClickTime = now;
        return false;
    }

    /**
     * 分享到微信
     * @param filepath 封面圖片文件
     */
    private void shareToWeixin(int scene, String filepath) {
        WeixinUtil.WeixinShareInfo shareInfo = new WeixinUtil.WeixinShareInfo();
        shareInfo.htmlUrl = shareUrl;
        shareInfo.htmlTitle = title;
        shareInfo.htmlDescription = description;

        SLog.info("shareUrl[%s], title[%s], description[%s]", shareUrl, title, description);

        if (filepath == null) {
            // 如果下載封面圖片不成功，則以應用圖標作為封面圖片
            shareInfo.htmlCover = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        } else {
            shareInfo.htmlCover = BitmapFactory.decodeFile(filepath);
        }

        if (shareInfo.htmlCover == null) {
            SLog.info("Error!shareInfo.htmlCover is null");
            return;
        } else {
            SLog.info("bitmapByteCount[%d]", shareInfo.htmlCover.getByteCount());
        }

        WeixinUtil.share(context, scene, WeixinUtil.SHARE_MEDIA_HTML, shareInfo);
    }



    public static String generateStoreShareLink(int storeId) {
        return Config.WEB_BASE_URL + "/store/" + storeId;
    }

    public static String generateGoodsShareLink(int spuId, int skuId) {
        if (skuId > 0) {
            return Config.WEB_BASE_URL + String.format("/goods/%d?goodsId=%d", spuId, skuId);
        } else {
            return Config.WEB_BASE_URL + String.format("/goods/%d", spuId);
        }
    }

    public static String generatePostShareLink(int postId) {
        return Config.WEB_BASE_URL + "/wantpost/article/detail/" + postId;
    }
}
