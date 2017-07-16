package com.mmall.common;

/**
 * Created by panyuanyuan on 2017/6/14.
 */
public class Const {

    /** session中用户登录后的key */
    public static final String LOGIN_USER = "LOGIN_USER";

    /** 用户相关常量 */
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EAMIL = "USER_EAMIL";

    /** Token前缀 */
    public static final String TOKEN_PREFIX = "TOKEN_";

    /** 用户角色 */
    public static final int ROL_MANAGER = 0;
    public static final int ROL_COMMEN = 1;

    /**
     * 购物车的常量
     */
    public interface Cart {
        int CART_CHECHKED = 1;
        int CART_UNCHECKED = 0;

        int CART_STOCK_LIMIT_FAIL = 0;
        int CART_STOCK_LIMIT_SUCCESS = 1;
    }

    /**
     * 订单状态的枚举常量
     */
    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单按成"),
        ORDER_CLOSE(60, "订单关闭");


        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 支付回调
     */
    public interface AlipayCallBack {
        /** 交易创建，等待买家付款 */
        String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

        /** TRADE_CLOSED */
        String TRADE_CLOSED = "TRADE_CLOSED";

        /** TRADE_SUCCESS */
        String TRADE_SUCCESS = "TRADE_SUCCESS";

        /** TRADE_FINISHED */
        String TRADE_FINISHED = "TRADE_FINISHED";

        /** 交易成功 */
        String RESPONSE_SUCCESS = "success";

        /** 交易失败 */
        String RESPONSE_FAILED = "failed";
    }

    /**
     * 支付平台
     */
    public enum PayPlatForm {
        ALIPAY(1, "支付宝");

        private int code;
        private String value;

        PayPlatForm(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
