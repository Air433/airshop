package com.airshop.order.controller;

import com.airshop.common.pojo.PageResult;
import com.airshop.order.dto.OrderDTO;
import com.airshop.order.dto.PayStateEnum;
import com.airshop.order.pojo.Order;
import com.airshop.order.service.OrderService;
import com.airshop.order.service.PayLogService;
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
    @Autowired
    private PayLogService payLogService;

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

    @GetMapping("state/{id}")
    public ResponseEntity<PayStateEnum> queryOrderStateByOrderId(@PathVariable("id")Long orderId){
        return ResponseEntity.ok(orderService.queryOrderStateByOrderId(orderId));
    }

    @GetMapping("list")
    public ResponseEntity<PageResult<Order>> queryOrderByPage(@RequestParam("page")Integer page,
                                                              @RequestParam("rows")Integer rows){
        return ResponseEntity.ok(orderService.queryOrderByPage(page, rows));
    }
}
