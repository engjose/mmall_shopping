package com.mmall.dao;

import com.mmall.pojo.Cart;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserAndProductId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    int selectCartUnchecked(Integer userId);

    List<Cart> selectCartByUser(Integer userId);

    void deleteProducts(List<String> productIds, Integer userId);
}