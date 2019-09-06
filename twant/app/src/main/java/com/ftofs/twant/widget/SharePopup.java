package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.ImageProcess;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.WeixinUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.File;


/**
 * 分享底部彈窗
 * @author zwm
 */
public class SharePopup extends BottomPopupView implements View.OnClickListener {
    /**
     * 分享店鋪的鏈接格式(1234為店鋪Id)
     * Config.WEB_BASE_URL + /store/1234
     *
     * 分享商品的鏈接格式(1234為spuId, 5678為skuId)
     * Config.WEB_BASE_URL + /goods/1234?goodsId=5678
     *
     * 分享貼文的鏈接格式(1234為貼文Id)
     * Config.WEB_BASE_URL + /wantpost/detail/1234
     */



    Context context;
    String shareUrl; // 分享的鏈接

    String title;
    String description;
    String coverUrl; // 分享的封面圖片的URL

    public SharePopup(@NonNull Context context, String shareUrl, String title, String description, String coverUrl) {
        super(context);

        this.context = context;
        this.shareUrl = shareUrl;
        this.title = title;
        this.description = description;
        this.coverUrl = StringUtil.normalizeImageUrl(coverUrl);
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
        findViewById(R.id.btn_copy_link).setOnClickListener(this);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
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
            if (!TwantApplication.wxApi.isWXAppInstalled()) {
                ToastUtil.error(context, context.getString(R.string.weixin_not_installed_hint));
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
                    shareToWeixin(scene, filepath);
                }
            };

            TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    String filename = PathUtil.getFilename(coverUrl);
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
                }
            });
        } else if (id == R.id.btn_copy_link) {
            ClipboardUtils.copyText(context, shareUrl);
            new XPopup.Builder(context)
//                         .dismissOnTouchOutside(false)
        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                    .setPopupCallback(new XPopupCallback() {
            @Override
            public void onShow() {
            }
            @Override
            public void onDismiss() {
            }
        }).asCustom(new TwConfirmPopup(context, "分享鏈接已復制", shareUrl, new OnConfirmCallback() {
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
    }
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
        } else {
            SLog.info("bitmapByteCount[%d]", shareInfo.htmlCover.getByteCount());
        }

        WeixinUtil.share(context, scene, WeixinUtil.SHARE_MEDIA_HTML, shareInfo);
    }



    public static String generateStoreShareLink(int storeId) {
        return Config.WEB_BASE_URL + "/store/" + storeId;
    }

    public static String generateGoodsShareLink(int spuId, int skuId) {
        return Config.WEB_BASE_URL + String.format("/goods/%d?goodsId=%d", spuId, skuId);
    }

    public static String generatePostShareLink(int postId) {
        return Config.WEB_BASE_URL + "/wantpost/detail/" + postId;
    }
}
