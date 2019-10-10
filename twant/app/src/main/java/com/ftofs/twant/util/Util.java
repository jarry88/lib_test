package com.ftofs.twant.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentationMagician;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.domain.member.PurchaseBuy;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.SpecPair;
import com.ftofs.twant.fragment.AddPostFragment;
import com.ftofs.twant.fragment.ArrivalNoticeFragment;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.fragment.LoginFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.MemberInfoFragment;
import com.ftofs.twant.fragment.MessageFragment;
import com.ftofs.twant.log.SLog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import cn.snailpad.easyjson.json.JSONObject;
import me.yokeyword.fragmentation.ISupportFragment;


/**
 * 常用工具類
 * @author zwm
 */
public class Util {
    /**
     * 需要登錄的才能顯示的Fragment列表
     */
    public static List<String> needLoginFragmentName = new ArrayList<>();
    static {
        needLoginFragmentName.add(AddPostFragment.class.getSimpleName());
        needLoginFragmentName.add(MemberInfoFragment.class.getSimpleName());
        needLoginFragmentName.add(MessageFragment.class.getSimpleName());
        needLoginFragmentName.add(ChatFragment.class.getSimpleName());
        needLoginFragmentName.add(ArrivalNoticeFragment.class.getSimpleName());
    }

    /**
     * 进入全屏模式
     * 参考
     * Android 正确进入全屏和退出全屏的姿势
     * https://blog.csdn.net/bobcat_kay/article/details/82794317
     * @param activity
     */
    public static void enterFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * 退出全屏模式
     * 参考
     * Android 正确进入全屏和退出全屏的姿势
     * https://blog.csdn.net/bobcat_kay/article/details/82794317
     * @param activity
     */
    public static void exitFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    public static void hideSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }


    /**
     * 在parentView中根據id查找目標view，并設置OnClickListener
     * @param parentView
     * @param id
     * @param listener
     */
    public static void setOnClickListener(View parentView, int id, View.OnClickListener listener) {
        View targetView = parentView.findViewById(id);

        if (targetView != null) {
            targetView.setOnClickListener(listener);
        }
    }



    public static void setWindowStatusBarColor(Activity activity, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 將byte數組轉換為字符串
     * @param byteArray
     * @return
     */
    public static String byteArrayToString(byte[] byteArray) {
        return byteArrayToString(byteArray, 0, byteArray.length);
    }

    /**
     * 將byte數組轉換為字符串
     * @param byteArray
     * @param len
     * @return
     */
    public static String byteArrayToString(byte[] byteArray, int len) {
        return byteArrayToString(byteArray, 0, len);
    }


    /**
     * 將byte數組轉換為字符串
     * @param byteArray
     * @param begin 開始位置(inclusive)
     * @param end 結束位置(exclusive)
     * @return
     */
    public static String byteArrayToString(byte[] byteArray, int begin, int end) {
        if (byteArray == null || begin >= end) {
            return null;
        }

        int len = byteArray.length;
        if (len < 1) {
            return "";
        }


        byte[] buffer = new byte[end - begin];

        for (int i = begin; i < end; ++i) {
            buffer[i - begin] = byteArray[i];
        }

        return new String(buffer);
    }

    /**
     * dp和pixel转换
     *
     * @param dipValue
     *            dp值
     * @return 像素值
     */
    public static int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue / scale) + 0.5f);
    }

    public static void showLoginFragment() {
        Util.startFragment(LoginFragment.newInstance());
    }

    public static String formatSpecString(List<SpecPair> specPairList) {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (SpecPair specPair : specPairList) {
            if (!first) {
                sb.append(";");
            }
            sb.append(specPair.specName);
            sb.append(": ");
            sb.append(specPair.specValueName);
            first = false;
        }
        return sb.toString();
    }

    /**
     * 獲取SPU的價格
     * @param goods
     * @return
     */
    public static float getSpuPrice(EasyJSONObject goods) {
        float price = 0;
        try {
            int appUsable = goods.getInt("appUsable");
            if (appUsable > 0) {
                price = (float) goods.getDouble("appPrice0");
            } else {
                price = (float) goods.getDouble("batchPrice2");
            }
        } catch (Exception e) {

        }
        return price;
    }

    /**
     * 獲取SKU的價格
     * @param goods
     * @return
     */
    public static float getSkuPrice(EasyJSONObject goods) {
        float price = 0;
        try {
            int appUsable = goods.getInt("appUsable");
            if (appUsable > 0) {
                price = (float) goods.getDouble("appPrice0");
            } else {
                price = (float) goods.getDouble("goodsPrice0");
            }
        } catch (Exception e) {

        }
        return price;
    }

    /**
     * 拨打电话（直接拨打电话）
     * 這種方法需要在AndroidMenifest文件里加上这个权限：<uses-permission android:name="android.permission.CALL_PHONE" />，
     * 在Android6.0中，还要在代码中动态申请权限。
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity activity, String phoneNum){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }



    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     * 這種方法不需要申請權限
     * @param phoneNum 电话号码
     */
    public static void dialPhone(Activity activity, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }


    /**
     * 判斷是否已經安裝了某個應用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageInstalled(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(final View view) {
        if (view == null || view.getContext() == null) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 200L);
    }

    public static void handleQRCodeResult(Context context, Intent data) {
        //处理扫描结果（在界面上显示）
        SLog.info("here");
        if (null != data) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                SLog.info("here");
                return;
            }
            SLog.info("here");
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                SLog.info("解析结果[%s]", result);
                if (result.startsWith("tw_member_")) {
                    // 添加好友
                    String memberName = result.substring(10);
                    SLog.info("memberName[%s]", memberName);

                    Util.startFragment(MemberInfoFragment.newInstance(memberName));
                } else {
                    ToastUtil.error(context, "無效的二維碼");
                }
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                ToastUtil.error(context, "解析二維碼失敗");
            }
        }
    }

    public static void startFragment(ISupportFragment fragment) {
        String fragmentClassName = fragment.getClass().getSimpleName();
        SLog.info("fragmentClassName[%s]", fragmentClassName);

        if (needLoginFragmentName.contains(fragmentClassName)) {
            // 需要登錄才能顯示的Fragment，判斷用戶是否已經登錄，如果未登錄，則顯示登錄對話框
            if (!User.isLogin()) {
                showLoginFragment();
                return;
            }
        }

        MainFragment mainFragment = MainFragment.getInstance();
        if (mainFragment == null) {
            ToastUtil.error(TwantApplication.getInstance(), "MainFragment為空");
            return;
        }

        mainFragment.start(fragment);
    }

    public static void startFragmentForResult(ISupportFragment fragment, int requestCode) {
        MainFragment mainFragment = MainFragment.getInstance();
        if (mainFragment == null) {
            ToastUtil.error(TwantApplication.getInstance(), "MainFragment為空");
            return;
        }

        mainFragment.startForResult(fragment, requestCode);
    }

    public static void packStaffInfo(CustomerServiceStaff staff, EasyJSONObject storeServiceStaffVo) {
        try {
            staff.storeId = storeServiceStaffVo.getInt("storeId");
            staff.staffId = storeServiceStaffVo.getInt("staffId");
            staff.staffName = storeServiceStaffVo.getString("staffName");
            staff.memberName = storeServiceStaffVo.getString("memberName");
            staff.imName = storeServiceStaffVo.getString("imName");
            staff.avatar = storeServiceStaffVo.getString("avatar");
            staff.welcomeMessage = storeServiceStaffVo.getString("welcome");
            staff.staffType = storeServiceStaffVo.getInt("staffType");
        } catch (Exception e) {

        }
    }

    /**
     * 獲取物流狀態描述
     * 物流狀態 1000待处理，2000进行中，3000已完成
     * @param context
     * @param state
     * @return
     */
    public static String getLogisticsStateDesc(Context context, int state) {
        if (state == 1000) {
            return context.getString(R.string.text_logistics_state_desc_to_be_processed);
        } else if (state == 2000) {
            return context.getString(R.string.text_logistics_state_desc_in_progress);
        } else if (state == 3000) {
            return context.getString(R.string.text_logistics_state_desc_finished);
        } else {
            return "";
        }
    }

    /**
     * 判斷一個JSON值是否為null
     * @param value
     * @return
     */
    public static boolean isJsonNull(Object value) {
        if (value == null) {
            return true;
        }

        return value.equals(JSONObject.NULL);
    }

    /**
     * 參考
     * APP内部跳转Google Play
     * https://www.jianshu.com/p/050dcda2603d
     * @param activity
     */
    public static void gotoGooglePlay(Activity activity) {
        SLog.info("PackageName[%s]", activity.getPackageName());
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + activity.getPackageName()));
            intent.setPackage("com.android.vending"); //这里对应的是谷歌商店，跳转别的商店改成对应的即可  (如果不設置package，會調用默認的應用商店，例如 應用寶等？？？)
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            } else {//没有应用市场，通过浏览器跳转到Google Play
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName()));
                if (intent2.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(intent2);
                } else {
                    //没有Google Play 也没有浏览器
                }
            }
        } catch (ActivityNotFoundException activityNotFoundException1) {
        }
    }

    /**
     * 數組倒序
     * @param arr
     * @param <T>
     */
    public static <T> void arrayReverse(T[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        int i = 0;
        int j = arr.length - 1;

        while (i < j) {
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;

            i++;
            j--;
        }
    }

    public static int getYOnScreen(View view) {
        int[] location = new int[2] ;
        view.getLocationOnScreen(location);
        return location[1];
    }

    /**
     * 版本號比較
     * @param version1
     * @param version2
     * @return
     *      * 如果version1比version2新，返回1
     *      * 如果version1比version2舊，返回-1
     *      * 如果version1與version2相同，返回0
     */
    public static int versionCompare(String version1, String version2) {
        String[] version1Arr = version1.split("\\.");
        String[] version2Arr = version2.split("\\.");


        // 獲取版本號分段數，取較小值是為了預防分段數不一致
        int fieldCount = Math.min(version1Arr.length, version2Arr.length);
        SLog.info("version1Arr[%s], version2Arr[%s], fieldCount[%d]", version1Arr, version2Arr, fieldCount);

        for (int i = 0; i < fieldCount; i++) {
            int field1 = Integer.valueOf(version1Arr[i]);
            int field2 = Integer.valueOf(version2Arr[i]);
            if (field1 > field2) {
                return 1;
            } else if (field1 < field2) {
                return -1;
            }
        }

        return 0;
    }

    /**
     * 獲取指定層的Fragment
     * @param activity 所在的Activity
     * @param layer 第幾層，從0開始，棧頂的layer為0
     * @return
     */
    public static Fragment getFragmentByLayer(FragmentActivity activity, int layer) {
        if (layer < 0) {
            return null;
        }
        String packageName = activity.getPackageName();
        // SLog.info("packageName[%s]", packageName);
        List<Fragment> resultList = new ArrayList<>();
        List<Fragment> fragmentList = FragmentationMagician.getActiveFragments(activity.getSupportFragmentManager());
        for (Fragment fragment : fragmentList) {
            // SLog.info("fragment[%s][%s]", fragment, fragment.getClass().getName());
            String className = fragment.getClass().getName();
            if (className.startsWith(packageName)) {
                resultList.add(fragment);
            }
        }

        if (layer < resultList.size()) {
            layer = resultList.size() - 1 - layer; // 進行倒序操作
            return resultList.get(layer);
        }

        return null;
    }
}
