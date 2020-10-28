package com.ftofs.twant.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.gzp.lib_common.utils.SLog;

import java.util.Hashtable;
import java.util.Map;

/**
 * @ClassName: QRCodeUtil
 * @Description: 二维码工具类
 * @Author Wangnan
 * @Date 2017/2/8
 */

public class QRCodeUtil {
    public static Bitmap createTrimmedBitmap(String content) {
        Bitmap qrCode = QRCodeUtil.createQRCodeBitmap(content, 500, 500);

        int blank = 20;
        Bitmap trimmed = Bitmap.createBitmap(qrCode, blank, blank,
                qrCode.getWidth() - 2 * blank, qrCode.getHeight() - 2 * blank);
        return trimmed;
    }


    /**
     * 创建二维码位图
     *
     * @param content 字符串内容(支持中文)
     * @param width 位图宽度(单位:px)
     * @param height 位图高度(单位:px)
     * @return
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height){
        return createQRCodeBitmap(content, width, height, "UTF-8", ErrorCorrectionLevel.H, 2, Color.BLACK, Color.WHITE);
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content 字符串内容
     * @param width 位图宽度,要求>=0(单位:px)
     * @param height 位图高度,要求>=0(单位:px)
     * @param character_set 字符集/字符转码格式 (支持格式:{@link //CharacterSetECI })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param error_correction 容错级别 (支持级别:{@link ErrorCorrectionLevel })。传null时,zxing源码默认使用 "L"
     * @param margin 空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param color_black 黑色色块的自定义颜色值
     * @param color_white 白色色块的自定义颜色值
     * @return
     */
    @Nullable
    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            @Nullable String character_set, @Nullable ErrorCorrectionLevel error_correction, int margin,
                                            @ColorInt int color_black, @ColorInt int color_white){

        /** 1.参数合法性判断 */
        if(TextUtils.isEmpty(content)){ // 字符串内容判空
            return null;
        }

        if(width < 0 || height < 0){ // 宽和高都需要>=0
            return null;
        }

        try {
            /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();

            if(!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
            }

            hints.put(EncodeHintType.ERROR_CORRECTION, error_correction); // 容错级别设置


            hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置

            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(bitMatrix.get(x, y)){
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 生成条形码
     * @param content
     * @param //format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param //isShowText boolean isShowText
     * @param //textSize
     * @param codeColor
     * @return
     */
    public static Bitmap createBarCode(String content, int desiredWidth, int desiredHeight, Map<EncodeHintType,?> hints , @ColorInt int codeColor) {
        if(TextUtils.isEmpty(content)){
            return null;
        }
        final int WHITE = Color.WHITE;
        final int BLACK = codeColor;

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix result = writer.encode(content, BarcodeFormat.CODE_128, desiredWidth,
                    desiredHeight, hints);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//            if(isShowText){
//                return addCode(bitmap,content,textSize,codeColor,textSize/2);
//            }
            return  rotateBitmap(bitmap, 90f);

        } catch (WriterException e) {
            SLog.info(e.getMessage());
        }
        return null;
    }

}
