package com.mmall.vo;

import java.math.BigDecimal;

/**
 * 购物车中商品的信息
 *
 * Created by panyuanyuan on 2017/7/2.
 */
public class CartProductVo {

    /** 公务车商品id */
    private Integer id;

    /** 用户ID */
    private Integer userId;

    /** 商品ID */
    private Integer productId;

    /** 商品数量 */
    private Integer quantity;

    /** 商品名称 */
    private String productName;

    /** 商品子标题 */
    private String productSubtitle;

    /** 商品主图 */
    private String productMainImage;

    /** 商品价格 */
    private BigDecimal productPrice;

    /** 商品状态 */
    private Integer productStatus;

    /** 商品的总价 */
    private BigDecimal productTotalPrice;

    /** 商品的库存 */
    private Integer productStock;

    /** 商品是否被勾选 */
    private Integer productChecked;

    /** 限制数量的返回结果 */
    private int limitQuatity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductChecked() {
        return productChecked;
    }

    public void setProductChecked(Integer productChecked) {
        this.productChecked = productChecked;
    }

    public int getLimitQuatity() {
        return limitQuatity;
    }

    public void setLimitQuatity(int limitQuatity) {
        this.limitQuatity = limitQuatity;
    }
}
