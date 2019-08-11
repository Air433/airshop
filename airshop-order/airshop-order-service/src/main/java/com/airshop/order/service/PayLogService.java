package com.airshop.order.service;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 19:03
 */
public interface PayLogService {

    void createPayLog(Long orderId, Long actualPay);

}
