package com.ftofs.twant.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.entity.ToastData;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
    public static final String PATH_HOME_INDEX = "/app/home/index";


    /**
     * 首頁最新想要店
     */
    public static final String PATH_NEW_ARRIVALS = "/app/home/recommend";


    /**
     * 商店分類
     */
    public static final String PATH_SHOP_CATEGORY = "/app/home/store_class_nav";


    /**
     * 產品分類
     */
    public static final String PATH_COMMODITY_CATEGORY = "/app/home/goods_class_nav";


    /**
     * 品牌分類
     */
    public static final String PATH_BRAND_CATEGORY = "/app/home/brand_label_nav";


    /**
     * 商店首頁
     */
    public static final String PATH_SHOP_HOME = "/store/home";
    /**
     * 商店首頁評論總數
     */
    public static final String PATH_COMMENT_STORE_COUNT = "/app/comment/store/count";
    /**
     * 商店首頁評論
     */
    public static final String PATH_STORE_COMMENT = "/store/comment";
    /**
     * 商店招聘列表
     */
    public static final String PATH_STORE_HRPOST = "/store/hrpost";
    /**
     * 商店招聘職位詳情
     */
    public static final String PATH_STORE_HR_INFO = "/app/store/hr/info";
    /**
     * 產品搜索
     */
    public static final String PATH_SEARCH_GOODS = "/search";

    /**
     * 商店搜索
     */
    public static final String PATH_SEARCH_STORE = "/app/search/store";

    /**
     * 店铺内產品搜索
     */
    public static final String PATH_SEARCH_GOODS_IN_STORE = "/app/store/search/goods";


    /**
     * 產品詳情
     */
    public static final String PATH_GOODS_DETAIL = "/app/goods";

    /**
     * 添加購物袋
     */
    public static final String PATH_ADD_CART = "/cart/add";
    /**
     * 修改購物袋
     */
    public static final String PATH_EDIT_CART = "/cart/edit";

    /**
     * 刪除購物袋
     */
    public static final String PATH_DELETE_CART = "/cart/del/batch/sku";


    /**
     * 購物袋列表
     */
    public static final String PATH_CART_LIST = "/cart/list";

    /**
     * 购买第一步：显示產品信息
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
     * 產品關注/取消關注
     */
    public static final String PATH_GOODS_FAVORITE = "/app/goods/favorite";


    /**
     * 產品讚想/取消讚想
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
     * 商店：城友讚想/取消讚想
     */
    public static final String PATH_STORE_LIKE = "/app/store/like";

    /**
     * 商店-產品分類搜索
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
     * 個人中心：瀏覽記憶
     */
    public static final String PATH_FOOTPRINT = "/member/app/browse/list";


    /**
     * 個人中心：批量刪除瀏覽足跡
     */
    public static final String PATH_DELETE_FOOTPRINT = "/member/app/browse/delete";
    /**
     * 個人中心：批量刪除城友關注
     */
    public static final String PATH_MEMBER_FOLLOW_MULTI_DELETE = "member/follow/multi_delete";
    /**
     * 個人中心：批量刪除商店關注
     */
    public static final String PATH_STORE_FOLLOW_MULTI_DELETE = "/member/store/favorite/multi_delete";
    /**
     * 批量刪除產品關注
     */
    public static final String PATH_GOODS_FOLLOW_MULTI_DELETE = "/member/goods/favorite/multi_delete";
    /**
     * 批量取消想要帖收藏
     */
    public static final String PATH_ARTICAL_FOLLOW_MULTI_DELETE = "/member/want_post/favor/multi_delete";
    /**
     * 批量取消想要帖收藏
     */
    public static final String PATH_WANT_POST_DELETE = "/want_post/delete";
    /**
     * 批量取消招聘收藏
     */
    public static final String PATH_HRPOST_FOLLOW_MULTI_DELETE = "/member/hr_post/favor/multi_delete";

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
     * 城友詳情
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
    // public static final String PATH_MESSAGE_LIST = "/member/message/list";
    public static final String PATH_MESSAGE_LIST = "/member/message/all/list";

    /**
     * 個人專頁數據
     */
    //此處修改路徑
    public static final String PATH_MEMBER_PAGE = "/member/page/index";

    /**
     * 個人專頁-互動-我的讚想-商店
     */
    public static final String PATH_MY_LIKE_STORE = "/memberpage/like/store";

    /**
     * 個人專頁-互動-我的讚想-產品
     */
    public static final String PATH_MY_LIKE_GOODS = "/memberpage/like/goods";

    /**
     * 個人專頁-互動-我的讚想-想要帖
     */
    public static final String PATH_MY_LIKE_POST = "/memberpage/like/want_post";


    /**
     * 個人專頁-我的關注-商店
     */
    public static final String PATH_MY_FOLLOW_STORE = "/memberpage/favor/store";

    /**
     * 個人專頁-我的關注-產品
     */
    public static final String PATH_MY_FOLLOW_GOODS = "/memberpage/favor/goods";

    /**
     * 產品详情 ：计算运费
     * 用戶切換送貨地區時，重新計算運費
     */
    public static final String PATH_REFRESH_FREIGHT = "/app/goods/freight";
    /**
     * 刪除個人評論
     */
    public static final String PATH_COMMENT_REMOVE = "/comment/remove.json";


    /**
     * 個人中心：会员店铺券列表
     */
    public static final String PATH_STORE_COUPON_LIST = "/member/app/voucher/list";


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
     * 想要帖分類
     */
    public static final String PATH_POST_CATEGORY_LIST = "/want_post/category/list";


    /**
     * 想要帖發佈
     */
    public static final String PATH_COMMIT_POST = "/want_post/issue";

    /**
     * 想要帖搜索
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
     * 發表回覆評論
     */
    public static final String PATH_PUBLISH_COMMENT = "/member/comment/send";

    /**
     * 单个產品退款申请页面
     */
    public static final String PATH_SINGLE_GOODS_REFUND = "/member/refund/goods";


    /**
     * 单个產品退款申请保存
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
     * 单个產品退货申请页面
     */
    public static final String PATH_SINGLE_GOODS_RETURN = "/member/return/add";

    /**
     * 单个產品申请退货保存
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
    // public static final String PATH_MPAY_PARAMS = "/member/mpay/params";

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
     * 查詢im信息
     */
    public static final String PATH_IM_INFO = "/member/im/info";
    /**
     * 查詢對方是否發送過名片
     */
    public static final String PATH_IM_IS_EXCHANGE_CARD = "/member/im/isExchangeCard";

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
     * 获取作者详情和作者其他想要帖
     */
    public static final String PATH_MEMBER_INFO = "/want_post/member/info";

    /**
     * 關注/取消關注城友
     */
    public static final String PATH_MEMBER_FOLLOW = "/member/follow";

    /**
     * 寄件/創建受理單
     */
    public static final String PATH_PACKAGE_DELIVERY = "/member/logistics/delivery";


    /**
     * 發起好友申請 send_request服務器拼寫錯誤，將錯就錯
     */
    public static final String PATH_ADD_FRIEND_APPLICATION = "/member/friend/send_reqeust";


    /**
     * 刪除好友
     */
    public static final String PATH_DELETE_FRIEND = "/member/friend/delete";


    /**
     * IM商店產品列表【新】
     */
    public static final String PATH_IM_STORE_GOODS_LIST = "/member/im/goods/list";


    /**
     * 我在商店的訂單記錄【新】
     */
    public static final String PATH_IM_STORE_ORDER_LIST = "/member/im/orders/list";


    /**
     * 用戶搜索
     */
    public static final String PATH_SEARCH_USER = "/member/friend/search_user";


    /**
     * 大豐支付
     */
    public static final String PATH_TAIFUNG_PAY = "/member/buy/pay/app/tfpay";


    /**
     * 大豐支付完成
     */
    public static final String PATH_TAIFUNG_PAY_FINISH = "/member/tfpay/finish";


    /**
     * 好友請求列表
     */
    public static final String PATH_FRIEND_REQUEST_LIST = "/member/friend/request_list";


    /**
     * 處理好友請求
     */
    public static final String PATH_HANDLE_FRIEND_REQUEST = "/member/friend/handle_request";

    /**
     * 【個人專頁】-》【好友列表】
     */
    public static final String PATH_MY_FRIEND_LIST = "/memberpage/friend/list";


    /**
     * 想要帖詳情
     */
    public static final String PATH_POST_DETAIL = "/want_post/info";

    /**
     * 想要帖讚想/取消讚想
     */
    public static final String PATH_POST_THUMB = "/want_post/like";

    /**
     * 想要帖關注/取消關注
     */
    public static final String PATH_POST_LIKE = "/want_post/favorite";

    /**
     * 猜你喜欢的產品
     */
    public static final String PATH_GUESS_YOUR_LIKE = "/goods/guess/like";

    /**
     * 個人專頁-想要帖(想要帖列表)
     */
    public static final String PATH_POST_LIST = "/memberpage/want_post/list";

    /**
     * 個人專頁-我的關注-城友
     */
    public static final String PATH_MY_FOLLOW_MEMBER = "/memberpage/favor/member";

    /**
     * 個人專頁-我的關注-想要帖
     */
    public static final String PATH_MY_FOLLOW_POST = "/memberpage/favor/want_post";

    /**
     * 個人專頁-我的關注-招聘
     */
    public static final String PATH_MY_FOLLOW_RECRUITMENT = "/memberpage/favor/hr_post";

    /**
     * 微信支付
     */
    public static final String PATH_WXPAY = "/member/buy/pay/new/wxpay";

    /**
     * 微信支付完成
     */
    public static final String PATH_WXPAY_FINISH = "/member/wxpay/finish";

    /**
     * 支付寶支付
     */
    public static final String PATH_ALIPAY = "/member/buy/pay/new/alipay";

    /**
     * 支付寶支付完成
     */
    public static final String PATH_ALIPAY_FINISH = "/member/alipay/finish";

    /**
     * 關注職位
     */
    public static final String PATH_FOLLOW_JOB = "/app/store/hr/follow";
    /**
     *
     */

    /**
     * 取消關注商店職位
     */
    public static final String PATH_UNFOLLOW_JOB = "/app/store/hr/unfollow";

    /**
     * 個人專頁-我的評論
     */
    public static final String PATH_MEMBER_PAGE_ALL_COMMENT = "/memberpage/all/comment";
    /**
     * 個人專頁-互動-我的評論
     */
    public static final String PATH_MEMBER_PAGE_COMMENT = "/memberpage/comment";

    /**
     * 商店客服列表
     */
    public static final String PATH_STORE_CUSTOMER_SERVICE = "/app/store/service";

    /**
     * 通訊錄模糊搜索
     */
    public static final String PATH_CONTACT_SEARCH = "/member/friend/contact_search";

    /**
     * 產品詳情：到貨通知
     */
    public static final String PATH_ARRIVAL_NOTICE = "/app/goods/arrival/notice";

    /**
     * 提交反饋
     */
    public static final String PATH_COMMIT_FEEDBACK = "/app/suggest/save";

    /**
     * 用户反馈列表
     */
    public static final String PATH_FEEDBACK_LIST = "/app/suggest/list";


    /**
     * 檢測更新
     */
    public static final String PATH_CHECK_UPDATE = "/app/android";

    /**
     * 訂單數量查詢
     */
    public static final String PATH_ORDER_COUNT = "/member/orders/count";

    /**
     * 訂單產品評論
     */
    public static final String PATH_GOODS_COMMENT = "/member/orders/comment/send";

    /**
     * 修改個性簽名
     */
    public static final String PATH_EDIT_MEMBER_SIGNATURE = "/memberpage/modify/signature";

    /**
     * 獲取啟動引導頁
     */
    public static final String PATH_APP_GUIDE = "/app/guide";

    /**
     * 我的錢包詳情
     */
    public static final String PATH_WALLET_INFO = "/member/wallet/info";

    /**
     * 设置/修改支付密码/激活想付钱包
     */
    public static final String PATH_WALLET_PAYMENT_PASSWORD = "/member/wallet/paypwd";

    /**
     * 預存款支付
     */
    public static final String PATH_WALLET_PAY = "/member/buy/pay/app/predepositPay";

    /**
     * 获取会员平台券列表
     */
    public static final String PATH_PLATFORM_COUPON_LIST = "/member/coupon/list";

    /**
     * 卡密领取店铺券
     */
    public static final String PATH_RECEIVE_STORE_COUPON_BY_PWD = "/member/voucher/receive/pwd";

    /**
     * 领取卡密领取类型平台券（需要图片验证码）
     */
    public static final String PATH_RECEIVE_PLATFORM_COUPON_BY_PWD = "/member/coupon/receive/pwd";

    /**
     * 產品詳情頁-查詢優惠券列表(包含商店券、平台券)
     */
    public static final String PATH_GOODS_DETAIL_COUPON_LIST = "/search/coupon/activity/list";

    /**
     * 優惠券(包含商店券、平台券)領取
     */
    public static final String PATH_RECEIVE_COUPON = "/member/coupon/receive/search";

    /**
     * 购买第一步：请求平台券
     */
    public static final String PATH_BUY_COUPON_LIST = "/member/buy/coupon/list";

    /**
     * 商店想看列表
     */
    public static final String PATH_STORE_VIDEO_LIST = "/store/video";

    /**
     * 刪除城友消息
     */
    public static final String PATH_DELETE_MESSAGE = "/member/message/delete.json";

    /**
     * 獲取城友所有類型的未讀消息
     */
    public static final String PATH_GET_UNREAD_MESSAGE_COUNT = "/member/message/unread.json";

    /**
     * 修改城友站内信消息為已讀狀態
     */
    public static final String PATH_MARK_ALL_MESSAGE_READ = "/member/message/markRead.json";

    /**
     * 活動頁跳轉
     */
    public static final String PATH_ACTIVITY_INFO = "/member/want/activity/info";


    /**
     * 活動專場促銷產品搜索
     */
    public static final String PATH_SEARCH_PROMOTION = "/search/promotion";

    /**
     * 個人專頁背景圖修改
     */
    public static final String PATH_CHANGE_PERSONAL_BACKGROUND = "/member/background/modify.json";

	/**
     * 獲取支付寶(香港)支付參數
     */
    public static final String PATH_ALIPAY_HK = "/member/buy/pay/alipayhk";

    /**
     * 支付寶(香港)支付完成
     */
    public static final String PATH_ALIPAY_HK_FINISH = "/member/alipayhk/finish";

    /**
     * 微信登录（第一步）
     */
    public static final String PATH_WX_LOGIN_STEP1 = "/loginconnect/new/weixin/login";


    /**
     * 微信登录（第二步）
     */
    public static final String PATH_WX_LOGIN_STEP2 = "/loginconnect/new/weixin/login/bind";

    /**
     * 微信绑定
     */
    public static final String PATH_BIND_WEIXIN = "/loginconnect/new/weixin/bind";

    /**
     * 微信登錄绑定身份碼發送
     */
    public static final String PATH_WX_BIND_SEND_SMS_CODE = "/loginconnect/weixin/smscode/send";


    /**
     * 微信解綁
     */
    public static final String PATH_UNBIND_WEIXIN = "/loginconnect/new/weixin/unbind";
    /**
     * 邮箱保存
     */
    public static final String PATH_MEMBEREMAIL_EDIT ="/member/memberemail/edit" ;

    /**
     * 發表想要帖-購物車產品
     */
    public static final String PATH_POST_GOODS_CART = "/member/wantpost/cart/goods";

    /**
     * 發表想要帖-最近購買
     */
    public static final String PATH_POST_GOODS_BUY = "/member/wnatpost/buyer/goods";

    /**
     * 發表想要帖-最近瀏覽產品
     */
    public static final String PATH_POST_GOODS_VISIT = "/member/wantpost/goods/browse";


    /**
     * 發表想要帖-產品搜索
     */
    public static final String PATH_POST_GOODS_SEARCH = "/member/wantpost/goods/search";


    /**
     * 聖誕活動參與狀態
     */
    public static final String PATH_GET_JOIN_CHRISTMAS_STATUS = "/member/christmas/join";


    /**
     * 设置婚姻
     */
    public static final String PATH_SET_MARRIAGE ="/member/membermarriage/edit" ;
    /**
     * 保存尺碼數據
     */
    public static final String PATH_MEASUREMENT_EDIT ="/member/measurement/edit" ;
    /**
     * 個人簡歷頁
     */
    public static final String PATH_MEMBER_RESUME = "/member/resume/info";
    /**
     * 與我溝通
     */
    public static final String PATH_RESUME_COMMUNICATION = "/member/resume/communication";
    /**
     * 我的投遞
     */
    public static final String PATH_RESUME_DELIVER = "/member/resume/deliver";
    /**
     * 、關注
     */
    public static final String PATH_RESUME_FOLLOW = "/member/resume/follow";
    public static final String PATH_POSITION_EDIT ="/member/resume/position/edit" ;
    public static final String PATH_RESUME_STATE_EDIT ="/member/resume/state/edit" ;
    /**
     * 保存或修改 資格證書-獲獎經歷-擅長技能
     */
    public static final String PATH_CAREER_EDIT = "/member/career/edit";
    /**
     * 修改簡歷開放狀態
     */
    public static final String PATH_RESUME_OPEN = "/member/resume/open/edit";
    /**
     *投遞簡歷
     */
    public static final String PATH_STORE_HR_APPLY = "/app/store/hr/apply";
    /**
     * 獲取教育與工作頁
     */
    public static final String PATH_CAREER_DETAIL = "/member/career/detail";
    /**E
     * 保存或修改工作經歷
     */
    public static final String PATH_EXPERIENCE_EDIT ="/member/experience/edit" ;
    /**
     * 保存或修改教育經歷
     */
    public static final String PATH_EDUCATION_EDIT = "/member/education/edit";
    /**
     * 刪除工作經歷
     */
    public static final String PATH_EDUCATION_DELETE="/member/education/delete";
    /**
     * 刪除教育經歷
     */
    public static final String PATH_EXPERIENCE_DELETE ="/member/experience/delete" ;
    /**
     * 開放聯繫方式給商家
     */
    public static final String PATH_COMMUNICATION_AUTHOR="/member/resume/communication/author";
    /**
     * 作者相關想要帖
     */
    public static final String PATH_WANT_POST_MEMBER_INFO_LIST = "/want_post/member/info/list";
    /**
     * 商店相關想要帖
     */
    public static final String PATH_STORE_POST = "/store/post";
    /**
     * 商店產品相關想要帖
     */
    public static final String PATH_STORE_GOODS_POST = "/store/goods/post";

    /**
     * 商品SKU列表
     */
    public static final String PATH_SKU_LIST = "/goods/sku";

    /**
     * 店鋪導航菜單
     */
    public static final String PATH_STORE_NAVIGATION = "/store/navigation";
    /**
     * 貼文發表驗證
     */
    public static final String PATH_WANT_POST_ISSUE_VALIDATE = "/member/want_post/issue/validate";


    /**
     * 貼文成真
     */
    public static final String PATH_POST_COME_TRUE = "/member/wantpost/cometrue";


    /**
     * 評論成真
     */
    public static final String PATH_COMMENT_COME_TRUE = "/member/comment/cometrue";


    /**
     * facebook登錄（第一步）
     */
    public static final String PATH_FACEBOOK_LOGIN = "/loginconnect/facebook/login";


    /**
     * facebook登錄（第二步）
     */
    public static final String PATH_FACEBOOK_LOGIN_BIND = "/loginconnect/facebook/login/bind";

    /**
     * facebook賬號綁定
     */
    public static final String PATH_FACEBOOK_BIND = "/loginconnect/facebook/bind";

    /**
     * facebook賬號解綁
     */
    public static final String PATH_FACEBOOK_UNBIND = "/loginconnect/facebook/unbind";

    /**
     * 訂單不同支付方式所需付款金額
     */
    public static final String PATH_PAYMENT_PRICE = "/member/buy/payment/price";

    /**
     * 移動端門店自提支付成功訂單列表頁
     * /member/buy/orders接口不再使用
     */
    public static final String PATH_BUY_ORDERS = "/member/buy/orders/info";


    /**
     * 獲取用戶token(活動身份令牌)
     */
    public static final String PATH_GET_MEMBER_TOKEN = "/member/getToken";
    /**
     * 獲取用戶token(活動身份令牌)
     */
    public static final String PATH_GET_IM_TOKEN = "/member/getUserTokenBySession";
    /**
     * 獲取用戶对话框列表
     */
    public static final String PATH_GET_IM_CONVERSATION = "/member/im/conversation";

    /**
     * 商城url地址解析(目前只解析店鋪，商品，貼文)
     */
    public static final String PATH_PARSE_URL = "/url/skip";


    /**
     * im平台客服列表
     */
    public static final String ADMIN_STAFF="/member/admin/staff";

    /**
     * 微信賬號編輯
     */
    public static final String PATH_EDIT_WECHAT_ID = "/member/memberwechat/edit";

    /**
     * Facebook賬號編輯
     */
    public static final String PATH_EDIT_FACEBOOK_ID = "/member/memberfacebook/edit";

    /**
     * 個人名片
     */
    public static final String MEMBER_CARD_DETAIL = "/member/card/detail";
    /**
     * 店鋪名片
     */
    public static final String STORE_CARD_DETAIL = "/app/store/business_card";
    /**
     * 新活動專場頁
     */
    public static final String PATH_SHOP_SESSION="/shop/goods/list";

    /**
     * 退货发货信息
     */
    public static final String PATH_RETURN_SHIP = "/member/return/ship";

    /**
     * 退货发货信息保存
     */
    public static final String PATH_RETURN_SHIP_SAVE = "/member/return/ship/save";

    /**
     * 【實名認證】保存
     */
    public static final String PATH_SAVE_REAL_NAME_INFO = "/consigneeNameAuth/identification";

    /**
     * 【實名認證】認證列表
     */
    public static final String PATH_REAL_NAME_LIST = "/consigneeNameAuth/list";

    /**
     * 【實名認證】刪除
     */
    public static final String PATH_DELETE_REAL_NAME_INFO = "/consigneeNameAuth/delete";

    /**
     * 【實名認證】編輯認證信息
     */
    public static final String PATH_EDIT_REAL_NAME_INFO = "/consigneeNameAuth/edit";


    /**
     * 【實名認證】是否顯示認證入口
     */
    public static final String PATH_DETERMINE_SHOW_REAL_NAME_POPUP = "/consigneeNameAuth/show";

    /**
     * 【商家端】商家端首頁統計數據
     */
    public static final String PATH_SELLER_INDEX = "/member/seller/index";
    /**
     * 【商家端】是否顯示商家入口
     */
    public static final String PATH_SELLER_ISSELLER = "/member/seller/isSeller";
    /**
     * 【商家端】編輯店鋪是否營業
     */
    public static final String PATH_SELLER_ISOPEN = "/member/seller/isOpen";


    /**
     * 【商家】訂單列表
     */
    public static final String PATH_SELLER_ORDERS_LIST = "/member/seller/orders/list";

    /**
     * 【商家】訂單詳情
     */
    public static final String PATH_SELLER_ORDER_DETAIL = "/member/seller/orders/info";

     /**
     * 【商家】鏟平交易信息
     * 兼退款訂單信息/refundId
     * 兼退貨訂單信息/refundId
     */
    public static final String PATH_SELLER_RETURN_ORDER_DETAIL = "/member/seller/return/orders/info";
    /**
     * 【商家】退款處理
     */
    public static final String PATH_SELLER_REFUND_HANDLE ="/member/seller/refund/handle";

    /**
     * 【商家】
     * 退款訂單列表
     */

    public static final String PATH_SELLER_REFUND_LIST = "/member/seller/refund/list";
    /**
     * 【商家】
     * 退款訂單詳情
     * 退貨詳情
     */
    public static final String PATH_SELLER_REFUND_INFO = "/member/seller/refund/info";
    public static final String PATH_SELLER_RETURN_INFO = "/member/seller/return/info";
    /**
    *退貨訂單信息(產品交易信息)
     */
    public static final String PATH_SELLER_REFUND_ORDERS_INFO = "/member/seller/refund/orders/info";
    public static final String PATH_SELLER_RETURN_ORDERS_INFO = "/member/seller/return/orders/info";
    /**
     * 【商家】
     * 退貨處理
     */
    public static final String PATH_SELLER_RETURN_HANDLE = "/member/seller/return/handle";
    /**
     * 【商家】
     * 退貨收貨
     */
    public static final String PATH_SELLER_RETURN_RECEIVE_SAVE = "/member/seller/return/receivesave";
    /**
     * 【商家】
     * 商品發佈頁
     */
    public static final String PATH_SELLER_GOODS_PUBLISH_PAGE = "/member/seller/goods/publish/page";

    /**
     * 【商家】
     * 查詢商品分類綁定的品牌列表
     */
    public static final String PATH_SELLER_QUERY_BIND_BRANDS = "/member/seller/query/bind/brands";
    /**
     * 【商家】
     * 查詢所有品牌所在地
     */
    public static final String PATH_SELLER_QUERY_COUNTRY_ALL = "/member/seller/query/country/all";
    /**
     * 【商家】
     * 商品發佈頁 保存
     */
    public static final String PATH_SELLER_GOODS_PUBLISH_SAVE = "/member/seller/goods/publish/save";
    /**
     * 【商家】
     * 退款訂單信息(產品交易信息)
     */
    public static final String PATH_SELLER_ORDERS_INFO = "/member/seller/orders/info";
    /**
     * 【商家】
     * 退貨訂單列表
     */
    public static String PATH_SELLER_RETURN_LIST="/member/seller/return/list";

    /**
     * 【商家】訂單買家信息
     */
    public static final String PATH_SELLER_BUYER_INFO = "/member/seller/orders/memberInfo";


    /**
     * 【商家】訂單發貨物流信息
     */
    public static final String PATH_SELLER_LOGISTICS_LIST = "/member/seller/orders/send/info";


    /**
     * 【商家】訂單發貨
     */
    public static final String PATH_SELLER_ORDER_SHIP = "/member/seller/orders/send/save";


    /**
     * 【購物專場】新版可復用購物專場主頁
     */
    public static final String PATH_SHOPPING_ZONE = "/shoppingzone";


    /**
     * 【商家】商品列表
     */
    public static final String PATH_SELLER_GOODS_LIST = "/member/seller/goods/list";

    /**
     * 【商家】批量上架商品
     */
    public static final String PATH_SELLER_GOODS_BATCH_ON_SHELF = "/member/seller/goods/putaway";

    /**
     * 【商家】批量下架商品
     */
    public static final String PATH_SELLER_GOODS_BATCH_OFF_SHELF = "/member/seller/goods/soldout";

    /**
     * 【商家】商品詳情
     */
    public static final String PATH_SELLER_GOODS_DETAIL = "/member/seller/goods";

    /**
     * 【商家】刪除商品
     */
    public static final String PATH_SELLER_DELETE_GOODS = "/member/seller/goods/delete";

    /**
     * 【商家】複製商品
     */
    public static final String PATH_SELLER_COPY_GOODS = "/member/seller/goods/copy";

    /**
     * 优惠券口令解析
     */
    public static final String PATH_PARSE_COUPON_WORD = "/app/command/crack";

    /**
     * 口令領取優惠券
     */
    public static final String PATH_RECEIVE_WORD_COUPON = "/member/app/command/receive";



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
        OkHttpClient client = getOkHttpClient();
        String url;
        if (StringUtil.isUrlString(path)) {
            url = path;
        } else {
            url = API_BASE_URL + path;
        }
        SLog.info("url[%s]", url);
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
                    // SLog.info("statusCode[%d]", statusCode);

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
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
        boolean result = true;
        try {
            OkHttpClient client = getOkHttpClient();
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
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            result = false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        return result;
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
            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
            int code = responseObj.getInt("code");
            if (code != ResponseCode.SUCCESS) {
                return null;
            }

            captchaKey = responseObj.getSafeString("datas.captchaKey");


            // 2. 獲取到captcha key后，再下載驗證碼圖片
            String path = PATH_MAKE_CAPTCHA + makeQueryString(EasyJSONObject.generate("captchaKey", captchaKey, "clientType", Constant.CLIENT_TYPE_ANDROID));
            SLog.info("path[%s]", path);

            String url = API_BASE_URL + path;

            OkHttpClient client = getOkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

            is = response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            }
            catch (Exception e) {
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return null;
                    }

                    EasyJSONArray adminMobileAreaList = responseObj.getSafeArray("datas.adminMobileAreaList");

                    for (Object object : adminMobileAreaList) {
                        final MobileZone mobileZone = (MobileZone) EasyJSONBase.jsonDecode(MobileZone.class, object.toString());
                        SLog.info("mobileZone: %s", mobileZone);
                        mobileZoneList.add(mobileZone);
                    }

                    SLog.info("获取MobileZone数据成功");
                    return mobileZoneList;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return;
                    }

                    EasyJSONArray adminMobileAreaList = responseObj.getSafeArray("datas.adminMobileAreaList");
                    for (Object object : adminMobileAreaList) {
                        MobileZone mobileZone = (MobileZone) EasyJSONBase.jsonDecode(MobileZone.class, object.toString());
                        SLog.info("mobileZone: %s", mobileZone);
                        mobileZoneList.add(mobileZone);
                    }

                    SLog.info("获取MobileZone数据成功");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
     * 城友異步上傳文件
     * @param file
     */
    public static void asyncUploadFile(File file) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }


        TwantApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                Object data = message;
                if (data != null) {
                    if ("1".equals(data.toString())){
                        ToastUtil.error(TwantApplication.getInstance().getApplicationContext(), "網絡異常，上傳失敗");
                    }
                }
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                String avatarUrl = syncUploadFile(file);
                SLog.info("成功調用異步上傳  %s",avatarUrl);
                if (avatarUrl != null) {
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_UPLOAD_FILE_SUCCESS, avatarUrl);
                } else {
                    return 1;
                }

                return null;
            }
        });
    }

    /**
     * 城友同步上傳文件
     * 成功上傳返回文件相對URL  失敗返回null
     * @param file
     */
    public static String syncUploadFile(File file) {
        long threadId = Thread.currentThread().getId();
        Context context = TwantApplication.getInstance();

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return null;
        }

        // 檢查文件大小, 如果待上傳文件大小超過上限，則嘗試壓縮
        if (file.length() > Config.UPLOAD_FILE_SIZE_LIMIT) {
            file = FileUtil.getCompressedImageFile(context, file);

            SLog.info("path[%s], size[%d]", file.getAbsolutePath(), file.length());

            // 壓縮失敗
            if (file == null) {
                return null;
            }
        }
        SLog.info("Here");
        // 如果壓縮后，文件還是太大，則報錯
        if (file.length() > Config.UPLOAD_FILE_SIZE_LIMIT) {
            SLog.info("Here");
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_TOAST,
                    new ToastData(ToastData.TYPE_ERROR, context.getString(R.string.text_upload_file_too_large)));
            return null;
        }

        OkHttpClient client = getOkHttpClient();
        SLog.info("Here");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("token", token);
        // 拼装文件参数
        builder.addFormDataPart("file", file.getName(), RequestBody.create(STREAM, file));

        RequestBody requestBody = builder.build();

        String url = Config.API_BASE_URL + Api.PATH_UPLOAD_FILE;

        SLog.info("Here");

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();

            String responseStr = response.body().string();
            SLog.info("threadId[%d], responseStr[%s]", threadId, responseStr);

            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
            if (ToastUtil.isError(responseObj)) {
                return null;
            }

            return responseObj.getSafeString("datas.name");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        SLog.info("Here");
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

        OkHttpClient client = getOkHttpClient();
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
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

        // 過濾環信格式，提取內容
        if (StringUtil.isEMMessageText(content)) {
            content = StringUtil.getEMMessageText(content);
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
                "to", to,
                "messageType", messageType,
                "targetType", "users",
                "messageBody", messageBody
        );

        SLog.info("params[%s]", params.toString());

        String path = Api.PATH_IM_SEND_MESSAGE + makeQueryString(EasyJSONObject.generate("token", token));

        SLog.info("path[%s], params[%s]", path, params);
        Api.postJsonIo(path, params.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                SLog.info("發送成功, response[%s], responseStr[%s]", response, responseStr);
            }
        });
    }


    public static OkHttpClient getOkHttpClient() {
        // 如果是生產模式，則使用正常的OkHttpClient
        if (!Config.DEVELOPER_MODE) {
            return new OkHttpClient();
        }
        // SLog.info("如果是開發模式，設置OkHttpClient忽略ssl驗證");
        // 如果是開發模式，設置OkHttpClient忽略ssl驗證
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // .addInterceptor(interceptor)
                .sslSocketFactory(sslContext.getSocketFactory(), xtm)
                .hostnameVerifier(DO_NOT_VERIFY)
                .build();
        return okHttpClient;
    }
}
