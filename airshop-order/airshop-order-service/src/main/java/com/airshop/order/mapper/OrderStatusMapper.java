package com.airshop.order.mapper;

import com.airshop.order.pojo.OrderStatus;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ouyanggang
 * @Date 2019/8/6 - 17:48
 */
public interface OrderStatusMapper extends Mapper<OrderStatus>, SelectByIdListMapper<OrderStatus, Long> {
}
