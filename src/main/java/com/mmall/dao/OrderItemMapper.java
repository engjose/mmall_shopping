package com.mmall.dao;

import com.mmall.pojo.OrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> selectOrderItemsByUserIdAndOrderNo(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);
}