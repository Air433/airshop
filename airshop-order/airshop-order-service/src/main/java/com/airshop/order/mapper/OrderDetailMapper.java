package com.airshop.order.mapper;

import com.airshop.order.pojo.OrderDetail;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

/**
 * @Author ouyanggang
 * @Date 2019/8/6 - 17:17
 */
public interface OrderDetailMapper extends Mapper<OrderDetail>, InsertListMapper<OrderDetail> {
}
