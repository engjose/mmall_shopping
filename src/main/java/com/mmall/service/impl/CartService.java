package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ResultMap;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by panyuanyuan on 2017/7/2.
 */
@Service
public class CartService implements ICartService{

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ResultMap addCart(Integer productId, Integer productNum, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        ResultMap resultMap = checkParams(productId, productNum, loginUser);
        if(resultMap != null) {
            return resultMap;
        }

        Cart cart = cartMapper.selectByUserAndProductId(productId, loginUser.getId());
        if(cart == null) {
            //添加购物车商品
            Cart cartItem = new Cart();
            cartItem.setQuantity(productNum);
            cartItem.setProductId(productId);
            cartItem.setUserId(loginUser.getId());
            cartItem.setChecked(Const.Cart.CART_CHECHKED);
            cartItem.setCreateTime(new Date());
            cartItem.setUpdateTime(new Date());
            cartMapper.insertSelective(cartItem);
        } else {
            //更新购物车商品
            cart.setQuantity(productNum + cart.getQuantity());
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        CartVo cartVo = this.getCartVo(loginUser.getId());
        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "OK", cartVo);
    }

    @Override
    public ResultMap deleteCart(String productIds, HttpSession session) {

        User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
        if(loginUser == null) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }

        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if(!CollectionUtils.isEmpty(productIdList)) {
            cartMapper.deleteProducts(productIdList, loginUser.getId());
        }
        return ResultMap.getResultMap(ResponseCode.SUCCESS.getCode(), "ok");
    }

    /**
     * 封装购物车VO给前端
     *
     * @param userId
     * @return
     */
    private CartVo getCartVo(Integer userId) {

        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        List<Cart> cartList = cartMapper.selectCartByUser(userId);
        BigDecimal totalPrice = new BigDecimal("0");

        if(!CollectionUtils.isEmpty(cartList)) {
            for(Cart cart: cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                BeanUtils.copyProperties(cart, cartProductVo);

                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product != null) {
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductChecked(cart.getChecked());

                    if(product.getStock() >= cart.getQuantity() ) {
                        cartProductVo.setLimitQuatity(Const.Cart.CART_STOCK_LIMIT_SUCCESS);
                        cartProductVo.setQuantity(cart.getQuantity());
                    } else {
                        cartProductVo.setLimitQuatity(Const.Cart.CART_STOCK_LIMIT_FAIL);
                        cartProductVo.setProductStock(product.getStock());
                        cartProductVo.setQuantity(product.getStock());

                        //更新购物车中的产品数量
                        Cart cartQuery = new Cart();
                        cartQuery.setId(cart.getId());
                        cartQuery.setQuantity(product.getStock());
                        cartMapper.updateByPrimaryKeySelective(cartQuery);
                    }

                    //设置这个商品的总价钱
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                }

                //计算总价
                if(Const.Cart.CART_CHECHKED == cartProductVo.getProductChecked()) {
                    totalPrice = BigDecimalUtil.add(totalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }

                cartProductVoList.add(cartProductVo);
            }
        }

        cartVo.setCartTotalPrice(totalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return  cartVo;
    }

    /**
     * 判断商品是不是全部选择状态
     *
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if(userId == null) {
            return  false;
        }
        return cartMapper.selectCartUnchecked(userId) == 0;
    }



    /**
     * 校验添加购物车参数
     *
     * @param productId
     * @param productNum
     * @param loginUser
     * @return
     */
    private ResultMap checkParams(Integer productId, Integer productNum, User loginUser) {
        if(loginUser == null) {
            return ResultMap.getResultMap(ResponseCode.NEED_LOGIN.getCode(), "需要登录");
        }

        if(null == productId || productNum == null) {
            return ResultMap.getResultMap(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "商品不能为空");
        }
        return null;
    }
}



















