package com.mmall.service;

import com.mmall.common.ResultMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

public interface IOrderService {

    /***
     * alipay支付订单
     *
     * @param orderNo
     * @param userId
     * @param path
     * @return
     */
    ResultMap pay(Long orderNo, Integer userId, String path);

    /**
     * alipay回调后校验
     *
     * @param params
     * @return
     */
    ResultMap callBackAlipay(Map<String, String> params);

    /**
     * 查询订单的支付状态
     *
     * @param orderNo
     * @param session
     * @return
     */
    ResultMap queryOrderPayStatus(Long orderNo, HttpSession session);
}
