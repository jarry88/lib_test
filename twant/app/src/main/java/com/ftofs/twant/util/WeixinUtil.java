package com.ftofs.twant.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ftofs.twant.TwantApplication;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

/**
 * 微信分享工具類
 * @author zwm
 */
public class WeixinUtil {
    // 分享场景
    public static final int WX_SCENE_SESSION = SendMessageToWX.Req.WXSceneSession;
    public static final int WX_SCENE_TIMELINE = SendMessageToWX.Req.WXSceneTimeline;

    // 分享媒体类型
    public static final int SHARE_MEDIA_PHOTO = 1;  // 图片
    public static final int SHARE_MEDIA_VIDEO = 2;  // 视频
    public static final int SHARE_MEDIA_HTML = 3; // html页面
    public static final int SHARE_MEDIA_TEXT = 4; // 文本

    public static final int THUMB_SIZE = 150;

    /*
    参考文档
    https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=ddd20580630b72e67b040b1a651eb1358b1b5f00&lang=zh_CN
     */


    /**
     * 微信分享时用到的信息参数
     */
    public static class WeixinShareInfo {
        // 图片分享
        public String absolutePath = "";  // 图片的路径

        // 视频分享
        public String videoUrl = "";
        public String videoTitle = "";
        public String videoDescription = "";
        public Bitmap videoCover = null;

        // 网页分享
        public String htmlUrl = "";
        public String htmlTitle = "";
        public String htmlDescription = "";
        public Bitmap htmlCover = null;

        // 文本分享
        public String text;  // 文本內容
    }

    /**
     * 微信分享
     * 目前只支持2种场景值
     * WX_SCENE_SESSION -- 分享到聊天界面
     * WX_SCENE_TIMELINE -- 分享到朋友圈
     *
     * 目前只支持4种分享媒体
     * SHARE_MEDIA_PHOTO
     * SHARE_MEDIA_VIDEO
     * SHARE_MEDIA_HTML
     * SHARE_MEDIA_TEXT
     */
    public static void share(Context context, int scene, int shareMediaType, WeixinShareInfo shareInfo) {
        if (!TwantApplication.wxApi.isWXAppInstalled()) {
            ToastUtil.error(context, "未安裝微信");
        }

        // 参数检查
        if (scene != WX_SCENE_SESSION && scene != WX_SCENE_TIMELINE) {
            return;
        }

        if (shareMediaType != SHARE_MEDIA_PHOTO && shareMediaType != SHARE_MEDIA_VIDEO
                && shareMediaType != SHARE_MEDIA_HTML && shareMediaType != SHARE_MEDIA_TEXT) {
            return;
        }

        /*
        微信4.2以上支持朋友圈，如果需要检查微信版本支持API的情况， 可调用IWXAPI的getWXAppSupportAPI方法,0x21020001及以上支持发送朋友圈
        if (scene == WX_SCENE_TIMELINE) {  // 检查版本
            if (SPApplication.wxApi.getWXAppSupportAPI() < 0x21020001) {
                ASLog.error("Error!微信版本太旧");
                return;
            }
        }
        估计没人是微信4.2版本以下了
        */

        WXMediaMessage mediaMessage = null;
        if (shareMediaType == SHARE_MEDIA_PHOTO) {
            Bitmap bmp = BitmapFactory.decodeFile(shareInfo.absolutePath);

            // 初始化WXImageObject和WXMediaMessage对象
            WXImageObject imageObject = new WXImageObject(bmp);  // 注意传入的数据不能大于10M,开发文档上写的
            mediaMessage = new WXMediaMessage();
            mediaMessage.mediaObject = imageObject;

            // 设置缩略图
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            mediaMessage.thumbData = BitmapUtil.Bitmap2ByteArray(thumbBmp);  // 官方文档介绍这个bitmap不能超过32kb
        } else if (shareMediaType == SHARE_MEDIA_VIDEO) {
            // 初始化一个WXVideoObject对象，填写url
            WXVideoObject videoObject = new WXVideoObject();
            videoObject.videoUrl = shareInfo.videoUrl;

            // 用一个WXVideoObject对象初始化一个WXMediaMessage对象，填写标题、描述
            mediaMessage = new WXMediaMessage(videoObject);
            mediaMessage.title = shareInfo.videoTitle;
            mediaMessage.description = shareInfo.videoDescription;

            mediaMessage.thumbData = BitmapUtil.Bitmap2ByteArray(shareInfo.videoCover);

        } else if (shareMediaType == SHARE_MEDIA_HTML) {
            // 初始化一个WXWebpageObject对象，填写url
            WXWebpageObject webpageObject = new WXWebpageObject();
            webpageObject.webpageUrl = shareInfo.htmlUrl;

            // 用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题、描述
            mediaMessage = new WXMediaMessage(webpageObject);
            mediaMessage.title = shareInfo.htmlTitle;
            mediaMessage.description = shareInfo.htmlDescription;
            mediaMessage.thumbData = BitmapUtil.Bitmap2ByteArray(shareInfo.htmlCover);
        } else if (shareMediaType == SHARE_MEDIA_TEXT) {
            // 初始化一个 WXTextObject 对象，填写分享的文本内容
            WXTextObject textObj = new WXTextObject();
            textObj.text = shareInfo.text;

            // 用 WXTextObject 对象初始化一个 WXMediaMessage 对象
            mediaMessage = new WXMediaMessage(textObj);
            mediaMessage.mediaObject = textObj;
            mediaMessage.description = shareInfo.text;
        }

        if (mediaMessage.thumbData != null && mediaMessage.thumbData.length > 32 * 1024) {  // 缩略图数据不能超过32KB
            ToastUtil.error(context, "縮略圖過大");
            return;
        }

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = Guid.getSpUuid(); // transaction字段用于唯一标识一个请求, 用于在回调中分辨是哪个分享请求

        req.message = mediaMessage;
        req.scene = scene;

        boolean success = TwantApplication.wxApi.sendReq(req);  // 如果调用成功微信,会返回true
        if (!success) {
            ToastUtil.error(context, "分享失敗");
        }
    }
}

