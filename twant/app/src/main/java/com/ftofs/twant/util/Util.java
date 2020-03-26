package com.ftofs.twant.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentationMagician;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.facebook.AccessToken;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.Location;
import com.ftofs.twant.entity.SpecPair;
import com.ftofs.twant.fragment.AddPostFragment;
import com.ftofs.twant.fragment.ArrivalNoticeFragment;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.fragment.DoubleElevenFragment;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.LoginFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.MemberInfoFragment;
import com.ftofs.twant.fragment.MessageFragment;
import com.ftofs.twant.fragment.SearchResultFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.widget.TwLoadingPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.orhanobut.hawk.Hawk;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.urllib.Query;
import org.urllib.Urls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import cn.snailpad.easyjson.json.JSONObject;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


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
     * 檢測Youtube是否已經安裝
     * @param context
     * @return
     */
    public static boolean isYoutubeInstalled(Context context) {
        return isPackageInstalled(context, "com.google.android.youtube");
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
                if (StringUtil.isUrlString(result)) {
                    /*
                    https://192.168.5.29/web/store/85
                    如果是這種模式的url，則跳轉到店鋪頁
                     */
                    Pattern pattern = Pattern.compile("/web/store/(\\d+)");

                    Matcher matcher = pattern.matcher(result);
                    if (matcher.find()) {
                        String storeIdStr = matcher.group(1);
                        SLog.info("storeIdStr[%s]", storeIdStr);
                        int storeId = Integer.valueOf(storeIdStr);
                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                    }


                } else if (result.startsWith("tw_member_")) {
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
            staff.staffName = storeServiceStaffVo.getSafeString("staffName");
            staff.memberName = storeServiceStaffVo.getSafeString("memberName");
            staff.imName = storeServiceStaffVo.getSafeString("imName");
            staff.avatar = storeServiceStaffVo.getSafeString("avatar");
            staff.welcomeMessage = storeServiceStaffVo.getSafeString("welcome");
            staff.staffType = storeServiceStaffVo.getInt("staffType");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
     * 引用時一定要加判空處理！！！
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


    public static int getFragmentCount(FragmentActivity activity) {
        List<Fragment> fragmentList = FragmentationMagician.getActiveFragments(activity.getSupportFragmentManager());
        if (fragmentList == null) {
            return 0;
        }

        String packageName = activity.getPackageName();
        int fragmentCount = 0;
        for (Fragment fragment : fragmentList) {
            // SLog.info("fragment[%s][%s]", fragment, fragment.getClass().getName());
            String className = fragment.getClass().getName();
            if (className.startsWith(packageName)) {
                fragmentCount++;
            }
        }

        return fragmentCount;
    }


    public static void popToMainFragment(FragmentActivity activity) {
        SupportFragment topFragment = (SupportFragment) getFragmentByLayer(activity, 0);
        int fragmentCount = getFragmentCount(activity);
        SLog.info("fragmentCount[%d]", fragmentCount);
        if (fragmentCount < 2) {
            return;
        }


        Fragment targetFragment = getFragmentByLayer(activity, fragmentCount - 1);
        if (targetFragment != null) {
            topFragment.popTo(targetFragment.getClass(), false);

        }
    }

    public static String getAvailableCouponCountDesc(int count) {
        return "可用" + count + "張";
    }

    public static void getMemberToken(Context context) {
        SLog.info("當前禁用了請求membertoken的方法20200220");
        return;
//        String token = User.getToken();
//        if (!StringUtil.isEmpty(token)) {
//            EasyJSONObject params=EasyJSONObject.generate("token", token);
//            Api.getUI(Api.PATH_GET_MEMBER_TOKEN, params, new UICallback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    ToastUtil.showNetworkError(context, e);
//                }
//
//                @Override
//                public void onResponse(Call call, String responseStr) throws IOException {
//                    SLog.info("responseStr[%s]",responseStr);
//                    EasyJSONBase responseObj = EasyJSONObject.parse(responseStr);
//                    try {
//                        Hawk.put(SPField.FIELD_MEMBER_TOKEN, responseObj.get("datas.memberToken"));
//                    } catch (Exception e) {
//                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
//                    }
//                }
//            });
//        }
    }

    /**
     * 獲取屏幕的分辨率像素
     * @param context
     * @return (寬，高)
     */
    public static Pair<Integer, Integer> getScreenDimemsion(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new Pair<>(screenWidth, screenHeight);
    }


    /**
     * 獲取屏幕density
     * @param activity
     * @return
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 從Youtube視頻URL中獲取視頻Id
     * 有下列三種形式:
     * https://www.youtube.com/watch?v=ASeDClVcaN4&feature=player_embedded_uturn
     * https://www.youtube.com/watch?v=BiIRTvNlDgs
     * https://youtu.be/BiIRTvNlDgs?t=24
     * @param youtubeUrl
     * @return
     */
    public static String getYoutubeVideoId(String youtubeUrl) {
        if (StringUtil.isEmpty(youtubeUrl)) {
            return null;
        }
        // 先處理第3種形式
        if (youtubeUrl.startsWith("https://youtu.be/")) {
            return Urls.parse(youtubeUrl).path().filename();
        }
        List<Query.KeyValue> paramList = Urls.parse(youtubeUrl).query().params();
        for (Query.KeyValue kv : paramList) {
            if (kv.key().equals("v")) {
                return kv.value();
            }
        }
        return null;
    }


    /**
     * 生成H5雙十一活動鏈接，如果用戶未登錄，返回null
     * @param type 1 -- 抽獎遊戲 2 -- 我的獎品
     * @return
     */
    public static String makeDoubleElevenH5Url(int type) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return null;
        }

        String queryString = Api.makeQueryString(EasyJSONObject.generate(
                "token", token,
                "activityId", 1,
                "type", type,
                "device", "android"));

        String url = Config.API_BASE_URL + Api.PATH_ACTIVITY_INFO + queryString;
        SLog.info("url[%s]", url);
        return url;
    }

    public static String makeChristmasH5Url() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return null;
        }

        String queryString = Api.makeQueryString(EasyJSONObject.generate(
                "token", token,
                "device", "android"));

        String url = "https://www.twant.com/activities/spring/#/20191226/index" + queryString;
        SLog.info("url[%s]", url);
        return url;
    }

    public static void startDoubleElevenFragment() {
        Util.startFragment(DoubleElevenFragment.newInstance());
    }

    /**
     * 打开活动专场购物
     */
    public static void startActivityShopping() {
        EasyJSONObject params = EasyJSONObject.generate("is_double_eleven", true);
        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(), params.toString()));
    }


    public static final String CHRISTMAS_APP_INFO = "/want/activity/springInfo";
    public static final String CHRISTMAS_APP_GAME = "/want/activity/christmas/appGame";
    public static final String ACTIVITY_SPRING = "/activities/spring/#/20191226/index";
    public static final String DATA_IMAGE_PNG_PREFIX = "data:image/png;base64,";

    public static void startChristmasFragment() {
        String url = Config.WEB_BASE_URL + CHRISTMAS_APP_INFO + "?device=android";
        SLog.info("url[%s]",url);
        // String url = "http://192.168.240.5:8080/web" + CHRISTMAS_APP_INFO + "?device=android";
        Util.startFragment(H5GameFragment.newInstance(url, true));
    }


    public static String getLauncherClassName(Context context) {
        return context.getPackageName() + ".activity.SplashActivity";
    }


    /**
     * 模板代碼對應的資源Id
     * @param tplCode
     * @return
     */
    public static Integer tplCodeToResId(String tplCode) {
        if (tplCode.equals("memberReturnUpdate")) {
            return R.drawable.icon_notice_return;
        } else if (tplCode.equals("storeOpen") || tplCode.equals("storeClose") ||
                tplCode.equals("storeInfoUpdate") || tplCode.equals("storeGoodsCommonNew") || tplCode.equals("storeAnnouncement") ||
                tplCode.equals("storeGoodsCommonUpdate")) {
            return R.drawable.icon_notice_store;
        } else if (tplCode.equals("storeSalesPromotion") || tplCode.equals("memberDiscountCoupon")) {
            return R.drawable.icon_notice_bargain;
        } else if (tplCode.equals("memberWantCommentLike") || tplCode.equals("memberStoreWantCommentReply") || tplCode.equals("memberGoodsWantCommentReply")) {
            return R.drawable.icon_notice_interactive;
        } else if (tplCode.equals("memberWantPostLike") || tplCode.equals("memberFriendsApply") ||
                tplCode.equals("memberFollowWantPost") || tplCode.equals("memberAgreeFriendsApply")) {
            return R.drawable.icon_notice_friend;
        }

        return null;
    }


    public static void openApkFile(File apkFile, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            Uri uriForFile = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
        }else{
            intent.setDataAndType(Uri.fromFile(apkFile), getMIMEType(apkFile));
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 獲取一個文件的MIME描述
     * @param file
     * @return
     */
    public static String getMIMEType(File file) {
        String var1 = "";
        String var2 = file.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        SLog.info("mimeType[%s]", var1);
        return var1;
    }

    public static void showPickerView(Context context, String title,
                                      List<ListPopupItem> list1, List<ListPopupItem> list2, List<ListPopupItem> list3,
                                      OnOptionsSelectListener onOptionsSelectListener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, onOptionsSelectListener)
                .isDialog(false)
                .setTitleText(title)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                /*
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。

                 */

                // .setDecorView(flSalaryPickerContainer)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isCenterLabel(false)
                //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isCenterLabel(false)
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        SLog.info("onOptionsSelectChanged: %d, %d, %d", options1, options2, options3);
                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);
        //一级选择器
        /*pvOptions.setPicker(options1Items);*/
        //二级选择器
        pvOptions.setNPicker(list1, list2, list3);
        pvOptions.show();
    }

    public static void showPickerView(Context context, String title,
                                      List<ListPopupItem> list1, List<ListPopupItem> list2, List<ListPopupItem> list3,
                                      int index1,int index2,int index3,
                                      OnOptionsSelectListener onOptionsSelectListener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, onOptionsSelectListener)
                .isDialog(false)
                .setTitleText(title)
                //设置滚轮文字大小
                .setContentTextSize(20)
                //设置分割线的颜色
                .setDividerColor(Color.LTGRAY)
                .setSelectOptions(index1, index2)
                /*
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。

                 */

                // .setDecorView(flSalaryPickerContainer)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        SLog.info("onOptionsSelectChanged: %d, %d, %d", options1, options2, options3);
                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setNPicker(list1, list2, list3);//二级选择器
        pvOptions.show();
    }


    /**
     * 將圖片文件添加到系統相冊
     * 參考:
     * android 保存图片到相册并正常显示
     * https://blog.csdn.net/a394268045/article/details/51645411
     * @param context
     * @param file
     */
    public static void addImageToGallery(Context context, File file) {
        // 其次把文件插入到系统图库
        String path = file.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, file.getName(), null);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        SLog.info("HERE");
    }

    public static boolean makeParentDirectory(String absolutePath) {
        return makeParentDirectory(new File(absolutePath));
    }

    /**
     * 創建file的父目錄（如果不存在的話)
     * @param file
     * @return 如果創建成功或已經存在，返回 true; 創建失敗返回 false
     */
    public static boolean makeParentDirectory(File file) {
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            return true;
        }

        return parentFile.mkdirs();
    }

    /**
     * 修改購物車內容，比如添加購物車，刪除購物車，修改數量等
     * @param context
     * @param goodsId
     * @param buyNum
     * @param simpleCallback  成功時的回調
     */
    public static void changeCartContent(Context context, int goodsId, int buyNum, SimpleCallback simpleCallback) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONArray buyData = EasyJSONArray.generate(EasyJSONObject.generate("buyNum", buyNum, "goodsId", goodsId));
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "buyData", buyData.toString(),
                "clientType", Constant.CLIENT_TYPE_ANDROID);
        SLog.info("buyData[%s]", buyData.toString());

        SLog.info("params[%s]", params.toString());
        Api.postUI(Api.PATH_ADD_CART, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }

                // 通知更新購物袋紅點提示
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE, null);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_ADD_CART, null);

                // 如果要添加各種自定義動作，寫在simpleCallback裏面
                if (simpleCallback != null) {
                    simpleCallback.onSimpleCall(null);
                }
            }
        });
    }

    /**
     * 啟用/禁用按鈕
     * @param context
     * @param button
     * @param enable true -- 啟用  false -- 禁用
     */
    public static void setButtonStatus(Context context, TextView button, boolean enable) {
        if (button == null) {
            return;
        }
        SLog.info("setButtonStatus, enable[%s], button[%s]", enable, button);
        int color = context.getColor(enable ? R.color.tw_black : R.color.tw_light_grey);
        button.setTextColor(color);
    }

    /**
     * 檢查Facebook是否已登錄
     * @return
     */
    public static boolean isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
    }

    public static BasePopupView createLoadingPopup(Context context) {
        return new XPopup.Builder(context)
                .hasShadowBg(false)
                .asCustom(new TwLoadingPopup(context));
    }


    public static String makeSpringH5Url() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return null;
        }

//        String queryString = Api.makeQueryString(EasyJSONObject.generate(
////                "token", token,
////                "device", "android"));
//
        String url = Config.BASE_URL + "/activities/spring/#/20191226/index"+"?token="+User.getUserInfo(SPField.FIELD_MEMBER_TOKEN,"");
        return url;
    }

    /**
     * 判斷接收到的json數組是否為空
     * @param list
     * @return
     */
    public static boolean isJsonArrayEmpty(EasyJSONArray list) {
        if (list == null) {
            return true;
        }
        if (list.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判斷接收到的json對象是否為空
     * @param object
     * @return
     */
    public static boolean isJsonObjectEmpty(EasyJSONObject object) {
        if (object == null) {
            return true;
        }
        if (object.getHashMap().isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 判斷String、JSONObject、JSONArray是否為空
     * @param val
     * @return
     */
    public static boolean isEmpty(Object val) {
        if (val == null) {
            return true;
        }

        if (val instanceof String) {
            return StringUtil.isEmpty((String) val);
        }
        if (val instanceof EasyJSONObject) {
            return isJsonObjectEmpty((EasyJSONObject) val);
        }
        if (val instanceof EasyJSONArray) {
            return isJsonArrayEmpty((EasyJSONArray) val);
        }

        return false; // 默認值
    }

    /**
     * 為參數添加最新的坐標值
     * @param params
     * @return
     */
    public static EasyJSONObject upLocation(EasyJSONObject params)  {
        String locationStr = Hawk.get(SPField.FIELD_AMAP_LOCATION, "");
        SLog.info("locationStr[%s]", locationStr);

        if (!StringUtil.isEmpty(locationStr)) {
            Location location = null;
            try {
                location = (Location) EasyJSONBase.jsonDecode(Location.class, locationStr);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            // 1小時內的定位才考慮
            if (System.currentTimeMillis() - location.timestamp < Config.LOCATION_EXPIRE) {
                try {
                    params.set("lng", location.longitude);
                    params.set("lat", location.latitude);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
                SLog.info("定位數據有效");
            } else {
                SLog.info("定位數據過期");
            }
        }
        return params;
    }

    public static boolean bigImageError(SupportActivity activity, File file) {
        if (file.exists() && file.isFile()) {
            if (file.length() / 1024 > 495) {
                ToastUtil.error(activity, "圖片超過上限");
                return true;
            }
            return false;
        } else {
            ToastUtil.error(activity, "圖片打開失敗");
            return true;
        }
    }
}


