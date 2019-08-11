package com.airshop.order.controller;

import com.airshop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author ouyanggang
 * @Date 2019/8/11 - 10:37
 */
@RestController
public class PayNotifyController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/wxpay/notify", produces = "application/xml")
    public ResponseEntity<String> payNotify(@RequestBody Map<String, String> msg){

        orderService.handleNotify(msg);

        String result = "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
        return ResponseEntity.ok(result);
    }

}
