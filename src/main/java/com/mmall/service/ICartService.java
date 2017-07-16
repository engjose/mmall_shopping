package com.mmall.service;

import com.mmall.common.ResultMap;
import javax.servlet.http.HttpSession;

/**
 * Created by panyuanyuan on 2017/7/2.
 */
public interface ICartService {

    /**
     * 添加商品到购物车中
     *
     * @param productId
     * @param productNum
     * @param session
     * @return
     */
    ResultMap addCart(Integer productId, Integer productNum, HttpSession session);

    /**
     * 删除商品信息
     *
     * @param productIds
     * @return
     */
    ResultMap deleteCart(String productIds,HttpSession session);
}
