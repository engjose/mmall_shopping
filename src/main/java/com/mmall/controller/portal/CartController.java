package com.mmall.controller.portal;

import com.mmall.common.ResultMap;
import com.mmall.service.ICartService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 购物车Controller
 *
 * Created by panyuanyuan on 2017/7/2.
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 将商品添加到购物车中
     *
     * @param productId
     * @param productNum
     * @param session
     * @return
     */
    @RequestMapping(value = "/addCart", method = RequestMethod.POST)
    public ResultMap addCart(Integer productId, Integer productNum, HttpSession session) {
        return cartService.addCart(productId, productNum, session);
    }

    /**
     * 删除购物车中的商品
     *
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/deleteCart", method = RequestMethod.POST)
    public ResultMap deleteCart(String productIds, HttpSession session) {
        return cartService.deleteCart(productIds, session);
    }
}
