package com.airshop.order.service;

import com.airshop.common.pojo.PageResult;
import com.airshop.order.dto.OrderDTO;
import com.airshop.order.dto.PayStateEnum;
import com.airshop.order.pojo.Order;

import java.util.Map;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:45
 */
public interface OrderService {
    Long createOrder(OrderDTO orderDTO);

    Order queryById(Long orderId);

    String generateUrl(Long orderId);

    void handleNotify(Map<String, String> msg);

    PayStateEnum queryOrderStateByOrderId(Long orderId);

    PageResult<Order> queryOrderByPage(Integer page, Integer rows);
}
