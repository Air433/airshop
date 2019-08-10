package com.airshop.order.service.impl;

import com.airshop.interceptor.LoginInterceptor;
import com.airshop.order.dto.PayStateEnum;
import com.airshop.order.mapper.PayLogMapper;
import com.airshop.order.pojo.PayLog;
import com.airshop.order.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Payload;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 19:03
 */
@Service
public class PayLogServiceImpl implements PayLogService {
    @Autowired
    private PayLogMapper payLogMapper;
    @Override
    public void createPayLog(Long orderId, Long actualPay) {

        PayLog payLog;

        payLog = payLogMapper.selectByPrimaryKey(orderId);

        if (payLog != null){
            return;
        }

        payLog = new PayLog();
        payLog.setStatus(PayStateEnum.NOT_PAY.getValue());
        payLog.setPayType(1);
        payLog.setOrderId(orderId);
        payLog.setTotalFee(actualPay);
        payLog.setCreateTime(new Date());

        payLog.setUserId(LoginInterceptor.getLoginUser().getId());

        payLogMapper.insertSelective(payLog);
    }


}
