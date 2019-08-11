package com.airshop.myexception;

/**
 * @Author ouyanggang
 * @Date 2019/6/18 - 22:25
 */
public enum AirExceptionEnum {

    /**
     * 分类信息无法找到
     */
    CATEGORY_NOT_FOUND,

    /**
     * 品牌信息无法找到
     */
    BRAND_NOT_FOUND,

    GOODS_SKU_NOT_FOUND,

    GOODS_STOCK_NOT_FOUND,

    CART_NOT_FOUND,

    RECEIVER_ADDRESS_NOT_FOUND,

    STOCK_NOT_FOUND,

    ORDER_NOT_FOUND,

    PAY_FAIL,

    ORDER_STATUS_EXCEPTION,

    CREATE_PAY_URL_ERROR,

    WX_PAY_SIGN_INVALID,

    WX_PAY_NOTIFY_PARAM_ERROR;
}
