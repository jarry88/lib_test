package com.ftofs.twant.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.DownloadImageResult;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.base.callback.CommonCallback;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.utils.FileUtil;
import com.gzp.lib_common.utils.PermissionUtil;
import com.ftofs.twant.util.QRCode;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 顯示想要城二維碼的彈窗
 * @author zwm
 */
public class TwQRCodePopup extends CenterPopupView implements View.OnClickListener {
    Activity activity;
    String url;
    boolean directlyShow; // 是否直接顯示url表示的圖片，true -- 直接顯示url指向的圖片 false -- 根據url生成二維碼圖片，然後才顯示

    Bitmap qrCode;


    public TwQRCodePopup(@NonNull Activity activity, String url, boolean directlyShow) {
        super(activity);

        this.activity = activity;
        this.url = StringUtil.normalizeImageUrl(url);
        this.directlyShow = directlyShow;
    }



    public TwQRCodePopup(@NonNull Activity activity, String url) {
        this(activity, url, false);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.tw_qrcode_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ImageView imgQRCode = findViewById(R.id.img_qrcode);

        findViewById(R.id.btn_download).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);

        if (directlyShow) {
            Glide.with(imgQRCode).load(url)
                    .into(imgQRCode);
        } else {
            qrCode = QRCode.createQRCode(url);
            Glide.with(imgQRCode).load(qrCode)
                    .into(imgQRCode);
        }
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
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 1f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_download) {
            PermissionUtil.actionWithPermission(activity, new String[] {Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.READ_EXTERNAL_STORAGE}, "下載二維碼需要授予", new CommonCallback() {

                @Override
                public String onSuccess(@Nullable String data) {
                    try {
                        String filename = FileUtil.getAppDataRoot() + "/download/" + System.currentTimeMillis() + ".jpg";
                        if (!Util.makeParentDirectory(filename)) {
                            SLog.info("Error!創建文件[%s]的父目錄失敗", filename);
                            ToastUtil.error(activity, "創建父目錄失敗");
                            return null;
                        }

                        if (directlyShow) { // 如果是直接顯示遠程圖片，需要下載保存
                            downloadImage(new File(filename));
                        } else { // 如果是生成的圖片，直接保存
                            FileOutputStream out = new FileOutputStream(filename);
                            SLog.info("filename[%s]", filename);

                            qrCode.compress(Bitmap.CompressFormat.JPEG, 85, out);
                            out.close();

                            Util.addImageToGallery(activity, new File(filename));

                            ToastUtil.success(activity, "下載成功");
                        }
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        ToastUtil.error(activity, "下載失敗" + e.getMessage());
                    }
                    return null;
                }

                @Override
                public String onFailure(@Nullable String data) {

                    return null;
                }
            });
        } else if (id == R.id.btn_share) {
            try {
                if (directlyShow) { // 如果是直接顯示遠程圖片，需要下載後再分享
                    shareRemoteImage();
                } else { // 如果是生成的圖片，直接分享
                    doShareQrCode(qrCode);
                }
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }
    }

    /**
     * 下載圖片到targetFile處
     * @param targetFile
     */
    private void downloadImage(File targetFile) {
        Util.getRemoteImage(activity, url, new SimpleCallback() {
            @Override
            public void onSimpleCall(Object data) {
                if (data == null) {
                    return;
                }

                try {
                    DownloadImageResult result = (DownloadImageResult) data;

                    if (result.success) {
                        SLog.info("width[%d], height[%d]", result.bitmap.getWidth(), result.bitmap.getHeight());
                        FileUtil.copyFile(result.file, targetFile);

                        Util.addImageToGallery(activity, targetFile);

                        ToastUtil.success(activity, "下載成功");
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void doShareQrCode(Bitmap qrBmp) {
        try {
            String filename = System.currentTimeMillis() + ".jpg";
            File file = new File(activity.getExternalCacheDir(),filename);

            FileOutputStream fOut = new FileOutputStream(file);
            qrBmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpeg");
            activity.startActivity(Intent.createChooser(intent, "分享店鋪二維碼"));
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }

    private void shareRemoteImage() {
        Util.getRemoteImage(activity, url, new SimpleCallback() {
            @Override
            public void onSimpleCall(Object data) {
                if (data == null) {
                    return;
                }

                DownloadImageResult result = (DownloadImageResult) data;

                if (result.success) {
                    SLog.info("width[%d], height[%d]", result.bitmap.getWidth(), result.bitmap.getHeight());
                    doShareQrCode(result.bitmap);
                }
            }
        });
    }
}

