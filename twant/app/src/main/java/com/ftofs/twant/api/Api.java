package com.ftofs.twant.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.ftofs.twant.config.Config.API_BASE_URL;


/**
 * Api接口
 * @author zwm
 */
public class Api {
    public static final int BUFFER_SIZE = 4096;

    /**
     * Http請求方式
     */
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;


    /**
     * 1.1 获取区号列表
     */
    public static final String PATH_MOBILE_ZONE = "/api/app/mobile/zone";

    /**
     * 1.2 获取key
     */
    public static final String PATH_MAKE_CAPTCHA_KEY = "/api/captcha/makecaptchakey";

    /**
     * 1.3 获取验证码图片
     */
    public static final String PATH_MAKE_CAPTCHA = "/api/captcha/makecaptcha";

    /**
     * 1.4 验证码发送
     */
    public static final String PATH_SEND_SMS_CODE = "/api/loginconnect/smscode/send";


    /**
     * 1.6 账号注册
     */
    public static final String PATH_MOBILE_REGISTER = "/api/loginconnect/mobile/register";


    /**
     * 1.7 驗證碼登錄
     */
    public static final String PATH_MOBILE_LOGIN = "/api/loginconnect/mobile/login";


    /**
     * 1.7 普通登錄
     */
    public static final String PATH_LOGIN = "/api/login";

    /**
     * 修改会员支付密码
     */
    public static final String PATH_EDIT_PAYMENT_PASSWORD = "/api/member/security/edit/payPwd";


    /**
     * 重置密碼
     */
    public static final String PATH_RESET_PASSWORD = "/api/loginconnect/mobile/findpwd";

    /**
     * 首頁輪播圖
     */
    public static final String PATH_HOME_CAROUSEL = "/api/app/home/index";


    /**
     * 最新入駐
     */
    public static final String PATH_NEW_ARRIVALS = "/api/app/home/recommend";


    /**
     * 店鋪分類
     */
    public static final String PATH_SHOP_CATEGORY = "/api/app/home/store_class_nav";


    /**
     * 商品分類
     */
    public static final String PATH_COMMODITY_CATEGORY = "/api/app/home/goods_class_nav";


    /**
     * 品牌分類
     */
    public static final String PATH_BRAND_CATEGORY = "/api/app/home/brand_label_nav";


    /**
     * 店鋪首頁
     */
    public static final String PATH_SHOP_HOME = "/api/app/store";


    /**
     * 商品搜索
     */
    public static final String PATH_SEARCH_GOODS = "/api/app/store/search/goods";


    /**
     * 商品詳情
     */
    public static final String PATH_GOODS_DETAIL = "/api/app/goods";

    /**
     * 添加購物車
     */
    public static final String PATH_ADD_CART = "/api/cart/add";


    /**
     * 購物車列表
     */
    public static final String PATH_CART_LIST = "/api/cart/list";


    /**
     * 發送Http請求
     * 如果ioCallback和uiCallback同時為null，表示同步方式執行
     * @param method GET或者POST
     * @param path URL的路徑
     * @param params 請求參數(可以為null)
     * @param ioCallback 在IO線程中執行的回調(可以為null)
     * @param uiCallback 在UI線程中執行的回調(可以為null)
     * @return 如果以異步方式執行，固定返回null；如果以同步方式執行，返回結果字符串
     */
    private static String httpRequest(int method, String path, EasyJSONObject params,
                                      Callback ioCallback, final UICallback uiCallback) {
        OkHttpClient client = new OkHttpClient();
        String url = API_BASE_URL + path;
        Request request = null;

        if (method == METHOD_GET) {
            // 如果有其他get参数，拼接到url中
            if (params != null) {
                url = url + makeQueryString(params);
            }

            request = new Request.Builder()
                    .url(url)
                    .build();
        } else if (method == METHOD_POST) {
            FormBody.Builder builder = new FormBody.Builder();

            // 如果有其他post参数，也拼装起来
            if (params != null) {
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    builder.add(param.getKey(), param.getValue().toString());
                }
            }

            RequestBody formBody = builder.build();
            request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
        }


        if (uiCallback != null) {
            // 在UI線程中執行回調
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    uiCallback.setOnFailure(call, e);
                    handler.post(uiCallback);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int statusCode = response.code();
                    SLog.info("statusCode[%d]", statusCode);

                    Handler handler = new Handler(Looper.getMainLooper());
                    uiCallback.setOnResponse(call, response);
                    handler.post(uiCallback);
                }
            });
        } else if(ioCallback != null) {
            // 在IO線程中執行回調
            client.newCall(request).enqueue(ioCallback);
        } else {
            // 同步方式執行
            try {
                Response response = client.newCall(request).execute();
                String responseStr = response.body().string();
                return responseStr;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String postInternal(String path, EasyJSONObject params, Callback ioCallback, final UICallback uiCallback) {
        return httpRequest(METHOD_POST, path, params, ioCallback, uiCallback);
    }

    /**
     * POST請求，回調在UI線程中執行
     * @param path
     * @param params
     * @param uiCallback
     */
    public static void postUI(String path, EasyJSONObject params, UICallback uiCallback) {
        postInternal(path, params, null, uiCallback);
    }

    /**
     * POST請求，回調在IO線程中執行
     * @param path
     * @param params
     * @param ioCallback
     */
    public static void postIO(String path, EasyJSONObject params, Callback ioCallback) {
        postInternal(path, params, ioCallback, null);
    }

    /**
     * 同步POST
     * @param path
     * @param params
     * @return
     */
    public static String syncPost(String path, EasyJSONObject params) {
        return postInternal(path, params, null, null);
    }


    private static String getInternal(String path, EasyJSONObject params, Callback ioCallback, final UICallback uiCallback) {
        return httpRequest(METHOD_GET, path, params, ioCallback, uiCallback);
    }

    /**
     * GET請求，回調在UI線程中執行
     * @param path
     * @param params
     * @param uiCallback
     */
    public static void getUI(String path, EasyJSONObject params, UICallback uiCallback) {
        getInternal(path, params, null, uiCallback);
    }

    /**
     * GET請求，回調在IO線程中執行
     * @param path
     * @param params
     * @param ioCallback
     */
    public static void getIO(String path, EasyJSONObject params, Callback ioCallback) {
        getInternal(path, params, ioCallback, null);
    }

    /**
     * 同步GET
     * @param path
     * @param params
     * @return
     */
    public static String syncGet(String path, EasyJSONObject params) {
        return getInternal(path, params, null, null);
    }

    /**
     * 同步下載文件，并保存到file中
     * @param url
     * @param file
     * @return true -- 成功 false -- 失敗
     */
    public static boolean syncDownloadFile(String url, File file) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            Response response = client.newCall(request).execute();
            byte[] buffer = new byte[BUFFER_SIZE];

            is = response.body().byteStream();
            fos = new FileOutputStream(file);

            while (true) {
                int n = is.read(buffer);
                if (n == -1) {
                    break;
                }
                if (n == 0) {
                    continue;
                }
                fos.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    /**
     * 同步方式獲取驗證碼圖片
     */
    public static Pair<Bitmap, String> getCaptcha() {
        InputStream is = null;
        Bitmap bitmap = null;
        String captchaKey = null;
        try {
            // 1. 首先獲取captcha key
            String responseStr = syncGet(PATH_MAKE_CAPTCHA_KEY, null);
            SLog.info("responseStr[%s]", responseStr);
            EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
            int code = responseObj.getInt("code");
            if (code != ResponseCode.SUCCESS) {
                return null;
            }

            captchaKey = responseObj.getString("datas.captchaKey");


            // 2. 獲取到captcha key后，再下載驗證碼圖片
            String path = PATH_MAKE_CAPTCHA + makeQueryString(EasyJSONObject.generate("captchaKey", captchaKey, "clientType", Constant.CLIENT_TYPE_ANDROID));
            SLog.info("path[%s]", path);

            String url = API_BASE_URL + path;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

            is = response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (EasyJSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            }
            catch (IOException e) {
            }

            if (bitmap == null || captchaKey == null) {
                return null;
            }
            return new Pair<>(bitmap, captchaKey);
        }
    }

    public static void refreshCaptcha(TaskObserver taskObserver) {
        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                return Api.getCaptcha();
            }
        });
    }

    public static void getMobileZoneList(TaskObserver taskObserver) {
        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                List<MobileZone> mobileZoneList = new ArrayList<>();
                try {
                    String responseStr = Api.syncGet(Api.PATH_MOBILE_ZONE, null);
                    if (StringUtil.isEmpty(responseStr)) {
                        return null;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return null;
                    }

                    EasyJSONArray adminMobileAreaList = responseObj.getArray("datas.adminMobileAreaList");

                    for (Object object : adminMobileAreaList) {
                        final MobileZone mobileZone = (MobileZone) EasyJSONBase.jsonDecode(MobileZone.class, object.toString());
                        SLog.info("mobileZone: %s", mobileZone);
                        mobileZoneList.add(mobileZone);
                    }

                    SLog.info("获取MobileZone数据成功");
                    return mobileZoneList;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    /**
     * 生成查詢參數字符串，例如:   ?param1=1111&param2=2222
     * @param params
     */
    public static String makeQueryString(EasyJSONObject params) {
        StringBuilder queryString = new StringBuilder("?");
        // 是否為第1個參數對
        boolean isFirst = true;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String item = String.format("%s=%s", param.getKey(), param.getValue());
            if (!isFirst) {
                queryString.append("&");
            }
            queryString.append(item);
            isFirst = false;
        }

        return queryString.toString();
    }
}
