package com.ftofs.twant.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");  // （ 二进制流，不知道下载文件类型）
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8"); // JSON字符串

    public static final int BUFFER_SIZE = 4096;

    /**
     * Http請求方式
     */
    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;


    /**
     * 1.1 获取区号列表
     */
    public static final String PATH_MOBILE_ZONE = "/app/mobile/zone";

    /**
     * 1.2 获取key
     */
    public static final String PATH_MAKE_CAPTCHA_KEY = "/captcha/makecaptchakey";

    /**
     * 1.3 获取验证码图片
     */
    public static final String PATH_MAKE_CAPTCHA = "/captcha/makecaptcha";

    /**
     * 1.4 验证码发送
     */
    public static final String PATH_SEND_SMS_CODE = "/loginconnect/smscode/send";


    /**
     * 1.6 账号注册
     */
    public static final String PATH_MOBILE_REGISTER = "/loginconnect/mobile/register";


    /**
     * 1.7 驗證碼登錄
     */
    public static final String PATH_MOBILE_LOGIN = "/loginconnect/mobile/login";


    /**
     * 1.7 普通登錄
     */
    public static final String PATH_LOGIN = "/login";

    /**
     * 修改会员支付密码
     */
    public static final String PATH_EDIT_PAYMENT_PASSWORD = "/member/security/edit/payPwd";


    /**
     * 重置密碼
     */
    public static final String PATH_RESET_PASSWORD = "/loginconnect/mobile/findpwd";

    /**
     * 首頁輪播圖
     */
    public static final String PATH_HOME_CAROUSEL = "/app/home/index";


    /**
     * 最新入駐
     */
    public static final String PATH_NEW_ARRIVALS = "/app/home/recommend";


    /**
     * 店鋪分類
     */
    public static final String PATH_SHOP_CATEGORY = "/app/home/store_class_nav";


    /**
     * 商品分類
     */
    public static final String PATH_COMMODITY_CATEGORY = "/app/home/goods_class_nav";


    /**
     * 品牌分類
     */
    public static final String PATH_BRAND_CATEGORY = "/app/home/brand_label_nav";


    /**
     * 店鋪首頁
     */
    public static final String PATH_SHOP_HOME = "/app/store";

    /**
     * 商品搜索
     */
    public static final String PATH_SEARCH_GOODS = "/search";

    /**
     * 店鋪搜索
     */
    public static final String PATH_SEARCH_STORE = "/search/store";

    /**
     * 店铺内商品搜索
     */
    public static final String PATH_SEARCH_GOODS_IN_STORE = "/app/store/search/goods";


    /**
     * 商品詳情
     */
    public static final String PATH_GOODS_DETAIL = "/app/goods";

    /**
     * 添加購物車
     */
    public static final String PATH_ADD_CART = "/cart/add";

    /**
     * 刪除購物車
     */
    public static final String PATH_DELETE_CART = "/cart/del/batch/sku";


    /**
     * 購物車列表
     */
    public static final String PATH_CART_LIST = "/cart/list";

    /**
     * 购买第一步：显示商品信息
     */
    public static final String PATH_DISPLAY_BILL_DATA = "/member/buy/step1";


    /**
     * 购买第一步：计算运费
     */
    public static final String PATH_CALC_FREIGHT = "/member/buy/calc/freight";


    /**
     * 總金额计算
     */
    public static final String PATH_CALC_TOTAL = "/member/buy/calc";


    /**
     * 地區列表
     */
    public static final String PATH_AREA_LIST = "/area/list";


    /**
     * 会员收货地址添加
     */
    public static final String PATH_ADD_ADDRESS = "/member/address/add";

    /**
     * 会员收货地址列表
     */
    public static final String PATH_LIST_ADDRESS = "/member/address/list";

    /**
     * 会员收货地址编辑
     */
    public static final String PATH_EDIT_ADDRESS = "/member/address/edit";

    /**
     * 刪除会员收货地址
     */
    public static final String PATH_DELETE_ADDRESS = "/member/address/delete";

    /**
     * 购买第二步：保存生成订单
     */
    public static final String PATH_COMMIT_BILL_DATA = "/member/buy/step2";


    /**
     * 訂單列表
     */
    public static final String PATH_ORDER_LIST = "/member/orders/list";

    /**
     * 訂單詳情
     */
    public static final String PATH_ORDER_DETAIL = "/member/orders/info";

    /**
     * 商品關注/取消關注
     */
    public static final String PATH_GOODS_FAVORITE = "/app/goods/favorite";


    /**
     * 商品點贊/取消點贊
     */
    public static final String PATH_GOODS_LIKE = "/app/goods/like";

    /**
     * 收藏店铺
     */
    public static final String PATH_STORE_FAVORITE_ADD = "/member/store/favorite/add";


    /**
     * 取消收藏店铺
     */
    public static final String PATH_STORE_FAVORITE_DELETE = "/member/store/favorite/delete";


    /**
     * 店鋪：會員點贊/取消點贊
     */
    public static final String PATH_STORE_LIKE = "/app/store/like";

    /**
     * 店鋪-商品分類搜索
     */
    public static final String PATH_STORE_CATEGORY = "/app/store/category";

    /**
     * 店铺券/活动列表
     */
    public static final String PATH_STORE_ACTIVITY = "/app/store/active";

    /**
     * 领取店铺券
     */
    public static final String PATH_RECEIVE_VOUCHER = "/member/voucher/receive/free";

    /**
     * 個人中心：会员信赖值日志
     */
    public static final String PATH_TRUST_VALUE_LOG = "/member/app/exppoints/log";


    /**
     * 個人中心：会员积分日志
     */
    public static final String PATH_BONUS_LOG = "/member/app/points/log";


    /**
     * 個人中心：我的足跡
     */
    public static final String PATH_FOOTPRINT = "/member/app/browse/list";


    /**
     * 個人中心：批量刪除瀏覽足跡
     */
    public static final String PATH_DELETE_FOOTPRINT = "/member/app/browse/delete";

    /**
     * 订单取消（未付款）
     */
    public static final String PATH_CANCEL_ORDER = "/member/orders/cancel";


    /**
     * 刪除訂單
     */
    public static final String PATH_DELETE_ORDER = "/member/orders/delete";


    /**
     * 確認收貨
     */
    public static final String PATH_CONFIRM_RECEIVE= "/member/orders/receive";


    /**
     * 訂單再次購買
     */
    public static final String PATH_BUY_AGAIN = "/member/orders/buy/again";


    /**
     * 會員詳情
     */
    public static final String PATH_MEMBER_DETAIL = "/member/detail";

    /**
     * 個人中心：修改昵稱
     */
    public static final String PATH_MODIFY_NICKNAME = "/member/modify/nick_name";

    /**
     * 修改会员 性别
     */
    public static final String PATH_SET_GENDER = "/member/sex/edit";

    /**
     * 修改会员生日
     */
    public static final String PATH_SET_BIRTHDAY = "/member/birthday/edit";

    /**
     * 热门关键词
     */
    public static final String PATH_HOT_KEYWORD = "/search/default/keyword";

    /**
     * 默認搜索詞
     */
    public static final String PATH_DEFAULT_KEYWORD = "/search/hot/keyword";

    /**
     * 搜索时下拉联想提示词
     */
    public static final String PATH_SEARCH_SUGGESTION = "/search/suggest.json";

    /**
     * 分类消息列表
     */
    public static final String PATH_MESSAGE_CATEGORY = "/member/message/class";

    /**
     * 消息列表
     */
    public static final String PATH_MESSAGE_LIST = "/member/message/list";

    /**
     * 個人專頁數據
     */
    public static final String PATH_USER_DATA = "/memberpage/index";

    /**
     * 個人專頁-互動-我的點贊-店鋪
     */
    public static final String PATH_MY_LIKE_STORE = "/memberpage/like/store";

    /**
     * 個人專頁-互動-我的點贊-商品
     */
    public static final String PATH_MY_LIKE_GOODS = "/memberpage/like/goods";

    /**
     * 個人專頁-我的關注-店鋪
     */
    public static final String PATH_MY_FOLLOW_STORE = "/memberpage/favor/store";

    /**
     * 個人專頁-我的關注-商品
     */
    public static final String PATH_MY_FOLLOW_GOODS = "/memberpage/favor/goods";

    /**
     * 商品详情 ：计算运费
     * 用戶切換送貨地區時，重新計算運費
     */
    public static final String PATH_REFRESH_FREIGHT = "/app/goods/freight";


    /**
     * 個人中心：会员店铺券列表
     */
    public static final String PATH_STORE_COUPON_LIST = "/member/app/voucher/list";

    /**
     * 卡密领取店铺券
     */
    public static final String PATH_RECEIVE_STORE_COUPON = "/member/voucher/receive/pwd";


    /**
     * 修改個人簡介
     */
    public static final String PATH_SAVE_PERSONAL_PROFILE = "/memberpage/modify/bio";


    /**
     * 修改会员地区
     */
    public static final String PATH_SET_MEMBER_ADDRESS = "/member/memberaddress/edit";


    /**
     * 图片上传（退款退货凭证、评价追评、投诉凭证）
     */
    public static final String PATH_UPLOAD_FILE = "/member/image/upload";

    /**
     * 会员头像修改
     */
    public static final String PATH_SET_AVATAR = "/member/avatar/edit";


    /**
     * 貼文分類
     */
    public static final String PATH_POST_CATEGORY_LIST = "/want_post/category/list";


    /**
     * 貼文發佈
     */
    public static final String PATH_COMMIT_POST = "/want_post/issue";

    /**
     * 貼文搜索
     */
    public static final String PATH_SEARCH_POST = "/want_post/list";

    /**
     * 評論列表
     */
    public static final String PATH_COMMENT_LIST = "/app/comment/list";

    /**
     * 评论点赞/取消点赞
     */
    public static final String PATH_COMMENT_LIKE = "/app/comment/like";

    /**
     * 評論詳情
     */
    public static final String PATH_COMMENT_DETAIL = "/app/comment/info";


    /**
     * 發表回復評論
     */
    public static final String PATH_PUBLISH_COMMENT = "/member/comment/send";

    /**
     * 单个商品退款申请页面
     */
    public static final String PATH_SINGLE_GOODS_REFUND = "/member/refund/goods";


    /**
     * 单个商品退款申请保存
     */
    public static final String PATH_SINGLE_GOODS_REFUND_SAVE = "/member/refund/goods/save";


    /**
     * 退款记录列表
     */
    public static final String PATH_REFUND_LIST = "/member/refund/list";

    /**
     * 退款申请取消
     */
    public static final String PATH_CANCEL_REFUND = "/member/refund/cancel";

    /**
     * 查看退款详情
     */
    public static final String PATH_REFUND_INFO = "/member/refund/info";

    /**
     * 单个商品退货申请页面
     */
    public static final String PATH_SINGLE_GOODS_RETURN = "/member/return/add";

    /**
     * 单个商品申请退货保存
     */
    public static final String PATH_SINGLE_GOODS_RETURN_SAVE = "/member/return/save";

    /**
     * 退货记录列表
     */
    public static final String PATH_RETURN_LIST = "/member/return/list";

    /**
     * 退货申请取消
     */
    public static final String PATH_CANCEL_RETURN = "/member/return/cancel";

    /**
     * 查看退货详情
     */
    public static final String PATH_RETURN_INFO = "/member/return/info";

    /**
     * 投诉列表
     */
    public static final String PATH_COMPLAIN_LIST = "/member/complain/list";

    /**
     * 投诉发起页面
     */
    public static final String PATH_COMPLAIN = "/member/complain/add";

    /**
     * 投诉保存
     */
    public static final String PATH_COMPLAIN_SAVE = "/member/complain/add/save";


    /**
     * 撤销投诉
     */
    public static final String PATH_CANCEL_COMPLAIN = "/member/complain/close";

    /**
     * 查看投诉详情
     */
    public static final String PATH_COMPLAIN_INFO = "/member/complain/info";

    /**
     * 快遞查詢
     */
    public static final String PATH_SEARCH_LOGISTICS = "/member/logistics/search";

    /**
     * 獲取表情列表
     */
    public static final String PATH_GET_EMOJI_LIST = "/app/emoji/list";

    /**
     * mpay參數獲取
     */
    public static final String PATH_MPAY_PARAMS = "/member/mpay/params";

    /**
     * mpay支付
     */
    public static final String PATH_MPAY = "/member/buy/pay/app/mpay";

    /**
     * 通訊錄
     */
    public static final String PATH_CONTACT_LIST = "/member/contact_list";

    /**
     * 查看好友信息
     */
    public static final String PATH_FRIEND_INFO = "/member/friend/friend_info";

    /**
     * MPay訂單支付查詢
     */
    public static final String PATH_MPAY_ORDERS_QUERY = "/member/mpay/orders/query";

    /**
     * IM發送消息
     */
    public static final String PATH_IM_SEND_MESSAGE = "/member/im/message/send";

    /**
     * 購買：門店自提
     */
    public static final String PATH_SELF_TAKE = "/member/buy/take/save";

    /**
     * 物流詳情
     */
    public static final String PATH_LOGISTICS_DETAIL = "/member/orders/logistics/info";

    /**
     * 全部退款申请页面
     */
    public static final String PATH_REFUND_ALL = "/member/refund/all";

    /**
     * 全部退款申请保存
     */
    public static final String PATH_REFUND_ALL_SAVE = "/member/refund/all/save";

    /**
     * 获取作者详情和作者其他文章
     */
    public static final String PATH_MEMBER_INFO = "/want_post/member/info";

    /**
     * 關注/取消關注會員
     */
    public static final String PATH_MEMBER_FOLLOW = "/member/follow";


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
                    uiCallback.setOnResponse(call, response.body().string());
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

    public static void getMobileZoneList(final List<MobileZone> mobileZoneList) {
        Api.getUI(Api.PATH_MOBILE_ZONE, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return;
                    }

                    EasyJSONArray adminMobileAreaList = responseObj.getArray("datas.adminMobileAreaList");
                    for (Object object : adminMobileAreaList) {
                        MobileZone mobileZone = (MobileZone) EasyJSONBase.jsonDecode(MobileZone.class, object.toString());
                        SLog.info("mobileZone: %s", mobileZone);
                        mobileZoneList.add(mobileZone);
                    }

                    SLog.info("获取MobileZone数据成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    /**
     * 會員異步上傳文件
     * @param file
     */
    public static void asyncUploadFile(File file) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("token", token);
        // 拼装文件参数
        builder.addFormDataPart("file", file.getName(), RequestBody.create(STREAM, file));

        RequestBody requestBody = builder.build();

        String url = Config.API_BASE_URL + Api.PATH_UPLOAD_FILE;

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    long threadId = Thread.currentThread().getId();

                    String responseStr = response.body().string();
                    SLog.info("threadId[%d], responseStr[%s]", threadId, responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        return;
                    }

                    String avatarUrl = responseObj.getString("datas.name");
                    SLog.info("avatarUrl[%s]", avatarUrl);

                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_UPLOAD_AVATAR_SUCCESS, avatarUrl);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 會員同步上傳文件
     * 成功上傳返回文件相對URL  失敗返回null
     * @param file
     */
    public static String syncUploadFile(File file) {
        long threadId = Thread.currentThread().getId();

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return null;
        }

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("token", token);
        // 拼装文件参数
        builder.addFormDataPart("file", file.getName(), RequestBody.create(STREAM, file));

        RequestBody requestBody = builder.build();

        String url = Config.API_BASE_URL + Api.PATH_UPLOAD_FILE;

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();

            String responseStr = response.body().string();
            SLog.info("threadId[%d], responseStr[%s]", threadId, responseStr);

            EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
            if (ToastUtil.isError(responseObj)) {
                return null;
            }

            return responseObj.getString("datas.name");
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 同步POST JSON
     * @param path
     * @param json
     * @return
     * @throws IOException
     */
    public static String syncPostJson(String path, String json) {
        return postJson(path, json, null, null);
    }

    /**
     * 以POST方式提交 JSON,回調在UI線程中執行
     * @param path
     * @param json
     * @param uiCallback
     */
    public static void postJsonUi(String path, String json, UICallback uiCallback) {
        postJson(path, json, null, uiCallback);
    }

    /**
     * 以POST方式提交 JSON,回調在UI線程中執行
     * @param path
     * @param json
     * @param ioCallback
     */
    public static void postJsonIo(String path, String json, Callback ioCallback) {
        postJson(path, json, ioCallback, null);
    }


    /**
     * 以【POST】方式提交JSON字符串
     * 如果ioCallback和uiCallback同時為null，表示同步方式執行
     * @param path URL的路徑
     * @param json 提交給服務器的json字符串
     * @param ioCallback 在IO線程中執行的回調(可以為null)
     * @param uiCallback 在UI線程中執行的回調(可以為null)
     * @return 如果以異步方式執行，固定返回null；如果以同步方式執行，返回結果字符串
     */
    public static String postJson(String path, String json, Callback ioCallback, final UICallback uiCallback) {
        String url = API_BASE_URL + path;

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();

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
                    uiCallback.setOnResponse(call, response.body().string());
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

    /**
     * IM發送消息到服務器
     */
    public static void imSendMessage(String to, String messageType, String messageId, String content, String ossUrl,
                                     String easemobUrl, int commonId, String goodsName, String goodsImage,
                                     int ordersId, String ordersSn) {
        SLog.info("IM發送消息到服務器");
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject messageBody = EasyJSONObject.generate(
                "messageId", messageId,
                "content", content);

        try {
            if (ossUrl != null) {
                messageBody.set("ossUrl", ossUrl);
            }
            if (easemobUrl != null) {
                messageBody.set("easemobUrl", easemobUrl);
            }
            if (commonId > 0) {
                messageBody.set("commonId", commonId);
            }
            if (goodsName != null) {
                messageBody.set("goodsName", goodsName);
            }
            if (goodsImage != null) {
                messageBody.set("goodsImage", goodsImage);
            }
            if (ordersId > 0) {
                messageBody.set("ordersId", ordersId);
            }
            if (ordersSn != null) {
                messageBody.set("ordersSn", ordersSn);
            }
        } catch (Exception e) {

        }


        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "to", to,
                "messageType", messageType,
                "targetType", "users",
                "messageBody", messageBody.toString()
        );

        SLog.info("params[%s]", params.toString());

        Api.postIO(Api.PATH_IM_SEND_MESSAGE, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.info("Error!%s", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SLog.info("發送成功");
            }
        });
    }
}
