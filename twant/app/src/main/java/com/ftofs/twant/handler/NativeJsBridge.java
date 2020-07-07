package com.ftofs.twant.handler;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentationMagician;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.fragment.AddPostFragment;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.LoginFragment;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONException;
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
        this.context = context;
        this.supportFragment = supportFragment;
    }



    /**
     * 雙11活動
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void activity11(String data) {
        SLog.info("activity11[%s]", data);

        Util.startActivityShopping();
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
            ShareDialog.show(MainActivity.getInstance(), content);
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

            if ("1".equals(isNeedLogin)) {
                String token = User.getToken();
                String memberToken = User.getUserInfo(SPField.FIELD_MEMBER_TOKEN, "");
                if (StringUtil.isEmpty(token)) {
                    Util.showLoginFragment();
                    return;
                }

                if (url.contains("?")) {
                    SLog.info("!!!HERE");
//                    url += "&token=" + token;
                    url += "&token=" + memberToken;
                } else {
//                    url += "?token=" + token;
                    url += "?token=" + memberToken;
                    SLog.info("!!!HERE");
                }

            }
//            url = url + "#/20191226/index";
//            SLog.info("url[%s]",url);

            Util.startFragment(H5GameFragment.newInstance(url, true, true, title, H5GameFragment.ARTICLE_ID_INVALID));
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

        Util.startFragment(AddPostFragment.newInstance(true));
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

            FriendInfo friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
            friendInfo.nickname = nickname;
            friendInfo.avatarUrl = avatarUrl;
            friendInfo.role = ChatUtil.ROLE_MEMBER;
            Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER), friendInfo));
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

        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity == null) {
            return;
        }

        String packageName = mainActivity.getPackageName();
        List<SupportFragment> popFragmentList = new ArrayList<>(); // 需要pop出去的Fragment

        boolean enqueueFlag = false;
        List<Fragment> fragmentList = FragmentationMagician.getActiveFragments(mainActivity.getSupportFragmentManager());
        for (Fragment fragment : fragmentList) {
            String className = fragment.getClass().getName();
            SLog.info("className[%s], packageName[%s]", className, packageName);
            if (!className.startsWith(packageName)) {
                continue;
            }

            if (!(fragment instanceof H5GameFragment)) {
                continue;
            }

            H5GameFragment h5GameFragment = (H5GameFragment) fragment;
            String url = h5GameFragment.getUrl();
            SLog.info("url[%s]", url);
            if (url != null && url.contains(Util.CHRISTMAS_APP_GAME)) {  // 根據Url來確定是要pop出去
                enqueueFlag = true;
            }

            if (enqueueFlag) {
                popFragmentList.add(h5GameFragment);
            }
        }

        for (int i = popFragmentList.size() - 1; i > 0; i--) {
            popFragmentList.get(i).pop();
        }
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
        String memberToken = User.getUserInfo(SPField.FIELD_MEMBER_TOKEN, null);
        if (StringUtil.isEmpty(memberToken)) {
            Util.startFragment(LoginFragment.newInstance());
        }
        H5GameFragment fragment = (H5GameFragment) supportFragment;
        SLog.info(fragment.getUrl());
        String url = String.format("%s?memberToken=%s", fragment.getUrl(), memberToken);
        fragment.loadUrl(url);
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
                    if (!Util.makeParentDirectory(filename)) {
                        SLog.info("Error!創建文件[%s]的父目錄失敗", filename);
                        return null;
                    }
                    SLog.info("filename[%s]", filename);

                    try {
                        FileOutputStream out = new FileOutputStream(filename);
                        out.write(buffer);
                        out.close();

                        Util.addImageToGallery(context, new File(filename));

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
