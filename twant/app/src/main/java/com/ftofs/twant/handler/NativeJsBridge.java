package com.ftofs.twant.handler;

import android.net.Uri;
import android.webkit.JavascriptInterface;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * js調用原生接口
 * 最全面总结 Android WebView与 JS 的交互方式
 * https://www.jianshu.com/p/345f4d8a5cfa
 * @author zwm
 */
public class NativeJsBridge {
    /**
     * 雙11活動
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void activity11(String data) {
        SLog.info("activity11[%s]", data);

        EasyJSONObject params = EasyJSONObject.generate("is_double_eleven", true);
        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(), params.toString()));
    }

    /**
     * facebook分享
     * @param data
     */
    @JavascriptInterface
    public void shareFB(String data) {
        SLog.info("shareFB[%s]", data);

        try {
            EasyJSONObject params = (EasyJSONObject) EasyJSONObject.parse(data);
            if (params == null) {
                return;
            }

            String title = params.getString("shareTitle");
            String desc = params.getString("shareDes");
            String url = params.getString("shareUrl");

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
}
