package com.ftofs.lib_net.handler;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.annotation.Nullable;

import com.facebook.share.model.ShareLinkContent;
import com.github.richardwrq.krouter.annotation.Inject;
import com.github.richardwrq.krouter.api.core.KRouter;
import com.gzp.lib_common.base.callback.CommonCallback;
import com.gzp.lib_common.service.AppService;
import com.gzp.lib_common.service.ConstantsPath;
import com.gzp.lib_common.utils.FileUtil;
import com.gzp.lib_common.utils.PermissionUtil;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.utils.ToastUtil;
import com.gzp.lib_common.utils.Util;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.FileOutputStream;

import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * js調用原生接口
 * 最全面总结 Android WebView与 JS 的交互方式
 * https://www.jianshu.com/p/345f4d8a5cfa
 * @author zwm
 */
public class NativeJsBridge {
    Context context;
    SupportFragment supportFragment;

    public NativeJsBridge(Context context, SupportFragment supportFragment) {
        KRouter.INSTANCE.inject(this);
        this.context = context;
        this.supportFragment = supportFragment;
    }

    @Inject(name=ConstantsPath.APP_SERVICE_PATH)
    AppService appService;
    /**
     * 雙11活動
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void activity11(String data) {
        SLog.info("activity11[%s]", data);
        appService.startActivityShopping();
//        Util.startActivityShopping();
    }

    /**
     * facebook分享
     * @param data
     */
    @JavascriptInterface
    public void shareFB(String data) {
        SLog.info("shareFB[%s]", data);

        try {
            EasyJSONObject params = EasyJSONObject.parse(data);
            if (params == null) {
                return;
            }

            String title = params.getSafeString("shareTitle");
            String desc = params.getSafeString("shareDes");
            String url = params.getSafeString("shareUrl");

            SLog.info("title[%s], desc[%s], url[%s]", title, desc, url);

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(desc)
                    .setContentUrl(Uri.parse(url))
                    .build();

            //调用分享弹窗
            appService.shareDialog(title,desc,Uri.parse(url));
        } catch (Exception e) {

        }
    }

    /**
     * 打開指定網頁
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void webUrl(String data) {
        SLog.info("webUrl[%s]", data);

        try {
            EasyJSONObject params = EasyJSONObject.parse(data);
            if (params == null) {
                return;
            }

            /*
            title:網頁標題
            url:跳轉的URL
            isNeedLogin:是否需要登錄訪問(0/1),當需要登錄的時候,app需在URL後面增加token信息
             */
            String title = params.getSafeString("title");
            String url = params.getSafeString("url");
            String isNeedLogin = params.getSafeString("isNeedLogin");

            SLog.info("title[%s], url[%s], isNeedLogin[%s]", title, url, isNeedLogin);

            appService.startH5Fragment(title,url,isNeedLogin);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * 跳轉想要圈
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void sendWant(String data) {
        SLog.info("sendWant[%s]", data);
        appService.sendWant();
    }


    /**
     * 跳轉im
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void chat(String data) {
        /*
        {
        memberName:會話id
        nickName:暱稱
        avatarUrl:頭像
        role:角色
        storeId:商店id
        }
         */

        EasyJSONObject params = EasyJSONObject.parse(data);
        if (params == null) {
            return;
        }

        try {
            String memberName = params.getSafeString("memberName");
            String nickname = params.getSafeString("nickName");
            String avatarUrl = params.getSafeString("avatarUrl");


            appService.startChatFragment(memberName, nickname, avatarUrl);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        SLog.info("chat[%s]", data);
    }


    /**
     * 跳轉活動首頁(第一個打開的webview)
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void activityHome(String data) {
        SLog.info("activityHome[%s]", data);

        appService.goActivityHome();
    }


    /**
     * 關閉當前webview
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void close(String data) {
        SLog.info("close[%s]", data);

        supportFragment.pop();
    }


    /**
     * 彈出登錄界面(需要判斷是否已經登錄,已經登錄將token,memberToken放進URL在進行網頁刷新)
     *
     * @param data
     */
    @JavascriptInterface
    public void login(String data) {
        SLog.info("login data[%s]", data);
        appService.goLogin(supportFragment,data);
    }


    /**
     * js的log输出
     * @param data js傳進來的日志内容
     */
    @JavascriptInterface
    public void jsLog(String data) {
        SLog.info("jsLog[%s]", data);
    }



    /**
     * 傳base64圖片數據過來進行保存
     * @param data js傳進來的圖片數據
     */
    @JavascriptInterface
    public void saveImageData(String data) {
        SLog.info("data[%s]", data);
        EasyJSONObject easyJSONObject = EasyJSONObject.parse(data);

        try {
            String imgData = easyJSONObject.getSafeString("imgData");
            saveBase64ImageToGallery(imgData);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    private void saveBase64ImageToGallery(String imageUrl) {
        PermissionUtil.actionWithPermission(context, new String[] {Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE}, "保存圖片需要授予", new CommonCallback() {

            @Override
            public String onSuccess(@Nullable String data) {
                if (imageUrl.startsWith(Util.DATA_IMAGE_PNG_PREFIX)) { // base64图片
                    String base64Code = imageUrl.substring(22);
                    byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
                    String filename = FileUtil.getAppDataRoot() + "/download/" + System.currentTimeMillis() + ".jpg";
                    if (!Util.INSTANCE.makeParentDirectory(filename)) {
                        SLog.info("Error!創建文件[%s]的父目錄失敗", filename);
                        return null;
                    }
                    SLog.info("filename[%s]", filename);

                    try {
                        FileOutputStream out = new FileOutputStream(filename);
                        out.write(buffer);
                        out.close();

                        Util.INSTANCE.addImageToGallery(context, new File(filename));

                        ToastUtil.success(context, "保存成功");
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        ToastUtil.success(context, "保存失敗");
                    }
                }
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {

                return null;
            }
        });
    }
}
