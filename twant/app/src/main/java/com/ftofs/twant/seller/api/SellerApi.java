package com.ftofs.twant.seller.api;

import okhttp3.MediaType;


/**
 * Api接口
 * @author zwm
 */
public class SellerApi {
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");  // （ 二进制流，不知道下载文件类型）
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8"); // JSON字符串

    public static final int BUFFER_SIZE = 4096;


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
     * 查詢商傢物流模板
     */
    public static final String PATH_SELLER_FREIGHT_TEMPLATE = "/member/seller/freight/template";
    /**
     * 【商家】
     * 查詢所有品牌所在地
     */
    public static final String PATH_SELLER_QUERY_COUNTRY_ALL = "/member/seller/query/country/all";
    /**
     * 【商家】
     * 查詢所有品牌所在地
     */
    public static final String PATH_SELLER_GOODS_CATEGORY = "/member/seller/goods/category";
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
     * 【賣家】獲取商品Sku列表
     */
    public static final String PATH_SELLER_GOODS_SKU_LIST = "/member/seller/goods/sku";

    //商品管理
    /**
     * 【賣家】鎮店之寶列表
     */
    public static final String PATH_SELLER_GOODS_FEATURES_LIST = "/member/seller/goods/features/list";




}
