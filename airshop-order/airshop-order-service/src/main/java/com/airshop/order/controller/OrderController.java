package com.airshop.order.controller;

import com.airshop.order.dto.OrderDTO;
import com.airshop.order.pojo.Order;
import com.airshop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:43
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDTO orderDTO){
        Long orderId = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(orderId);
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> queryById(@PathVariable(name = "id") Long orderId){
        Order order = orderService.queryById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * 生成支付链接
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    public ResponseEntity<String> generateUrl(@PathVariable(value = "id")Long orderId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.generateUrl(orderId));
    }
}
