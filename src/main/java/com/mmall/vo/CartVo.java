package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车VO对象
 *
 * Created by panyuanyuan on 2017/7/2.
 */
public class CartVo {

    /** 购物车中的购物项 */
    private List<CartProductVo> cartProductVoList;

    /** 购物车中商品总价 */
    private BigDecimal cartTotalPrice;

    /** 是否已经全部勾选 */
    private Boolean allChecked;

    /** 图片主机 */
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
