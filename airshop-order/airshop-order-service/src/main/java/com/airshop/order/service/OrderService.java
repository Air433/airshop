package com.airshop.order.service;

import com.airshop.order.dto.OrderDTO;
import com.airshop.order.pojo.Order;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:45
 */
public interface OrderService {
    Long createOrder(OrderDTO orderDTO);

    Order queryById(Long orderId);

    String generateUrl(Long orderId);
}
