package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    /**
     * 向订单表中添加数据
     * @param order
     */
    Long insert(Orders order);
}
