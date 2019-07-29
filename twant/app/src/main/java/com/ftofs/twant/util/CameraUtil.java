package com.ftofs.twant.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.log.SLog;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.io.IOException;

public class CameraUtil {
    /**
     * 打开相册Fragment的摄像头
     * @param cameraAction
     */
    public void openAlbumCamera(Context context, Object caller, final int cameraAction) {
        String rationaleHint;
        if (cameraAction == Constant.CAMERA_ACTION_IMAGE) {
            rationaleHint = "拍攝照片需要授予";
        } else {
            rationaleHint = "拍攝視頻需要授予";
        }
        PermissionUtil.actionWithPermission(context, new String[] {Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE}, rationaleHint, new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                openAlbumCamera(context, caller, cameraAction);
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                ToastUtil.error(context, "您拒絕了授權，無法使用本功能>_<");
                return null;
            }
        });
    }



    /**
     * 打开Camera
     * @param caller  调用Camera的Activity或Fragment
     * @param cameraAction  拍照片还是拍视频
     * @return
     */
    public static File openCamera(Context context, Object caller, int cameraAction) {
        try {

            // 用當前時間給取得的圖片或視頻命名
            String pureFilename = String.valueOf(System.currentTimeMillis());
            String ext = "";  // 扩展名
            if (cameraAction == Constant.CAMERA_ACTION_IMAGE) {
                ext = ".jpg";
            } else if (cameraAction == Constant.CAMERA_ACTION_VIDEO) {
                ext = ".mp4";
            }


            String filename = pureFilename + ext;
            // File photoFile = FileUtil.getCacheFile(context, fullFilename);
            File photoFile = new File(FileUtil.getTodayImageRoot() + "/" + filename);
            try {
                FileUtil.createDirNomedia(new File(FileUtil.getTodayImageRoot()));
            } catch (IOException e) {
                e.printStackTrace();
                SLog.info("Error!%s", e.getMessage());
            }

            // Uri imageUri = Uri.fromFile(photoFile);
            Uri imageUri = FileUtil.getCompatUriFromFile(context, photoFile);

            /*
            系统的摄像
            从系统现有的相机应用中获取拍摄的视频，与获取拍摄的图片过程大致相同，但是它除了可以通过putExtra()设置MediaStore.EXTRA_OUTPUT输出路径外，还可以设置其它值，这里简单介绍一下：
            MediaStore.EXTRA_OUTPUT：设置媒体文件的保存路径。
            MediaStore.EXTRA_VIDEO_QUALITY：设置视频录制的质量，0为低质量，1为高质量。
            MediaStore.EXTRA_DURATION_LIMIT：设置视频最大允许录制的时长，单位为秒。
            MediaStore.EXTRA_SIZE_LIMIT：指定视频最大允许的尺寸，单位为byte。
            */

            // 启动Camera
            String action = "";
            int requestCode = 0;
            if (cameraAction == Constant.CAMERA_ACTION_IMAGE) {
                action = MediaStore.ACTION_IMAGE_CAPTURE;
                requestCode = RequestCode.CAMERA_IMAGE.ordinal();
            } else if (cameraAction == Constant.CAMERA_ACTION_VIDEO) {
                action = MediaStore.ACTION_VIDEO_CAPTURE;
                requestCode = RequestCode.CAMERA_VIDEO.ordinal();
            }
            Intent intent = new Intent(action);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            if (caller instanceof Activity) {
                SLog.info("從Activity啟動");
                Activity activity = (Activity)caller;
                activity.startActivityForResult(intent, requestCode);
            } else if (caller instanceof Fragment) {
                SLog.info("從Fragment啟動");
                Fragment fragment = (Fragment)caller;
                fragment.startActivityForResult(intent, requestCode);
            }

            /*
            所有的媒体资源 在开机的时候 还有内存卡 挂载的时候 系统会 扫描我们的存储设备 将它 存放在 一个系统的 媒体资源数据库中
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(pathFile)));

            在非相机相关的项目中，如果需要拍照的话，一般都是调用系统现有的相机应用，而不会直接调用Camera硬件去获取图像。
            关于怎么调用图库，与裁剪图片点击(http://www.cnblogs.com/0616–ataozhijia/p/4162092.html)
             */

            SLog.info("photoFile[%s]", photoFile.getAbsoluteFile());
            return photoFile;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
