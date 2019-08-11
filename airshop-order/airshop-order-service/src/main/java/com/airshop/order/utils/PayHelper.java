package com.airshop.order.utils;

import com.airshop.myexception.AirExceptionEnum;
import com.airshop.myexception.MyException;
import com.airshop.order.config.PayConfig;
import com.airshop.order.dto.OrderStatusEnum;
import com.airshop.order.dto.PayStateEnum;
import com.airshop.order.mapper.OrderMapper;
import com.airshop.order.mapper.OrderStatusMapper;
import com.airshop.order.mapper.PayLogMapper;
import com.airshop.order.pojo.Order;
import com.airshop.order.pojo.OrderStatus;
import com.airshop.order.pojo.PayLog;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import jdk.nashorn.internal.runtime.JSONFunctions;
import net.minidev.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 15:00
 */
@EnableConfigurationProperties(value = PayConfig.class)
@Component
public class PayHelper {

    private static final Logger log = LoggerFactory.getLogger(PayHelper.class);

    @Autowired
    private PayConfig payConfig;

    private WXPay wxPay;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private PayLogMapper payLogMapper;

    final String SUCCESS = "SUCCESS";

    @PostConstruct
    public void init(){
//        wxPay = new WXPay(payConfig, WXPayConstants.SignType.MD5, true);
        wxPay = new WXPay(payConfig);
    }

    public String createPayUrl(Long orderId, String description, Long totalPay) {
        String key = "order:pay:url:" + orderId;

        try {
            String url = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(url)){
                return url;
            }
        } catch (Exception e) {
            log.error("查询缓存付款链接异常， 订单号：{}", orderId, e);
        }
        try {
            Map<String, String> data = new HashMap<>();
            //描述
            data.put("body", description);
            //订单号
            data.put("out_trade_no", orderId.toString());
            //总金额
            data.put("total_fee", totalPay.toString());
            //调用微信支付的终端ip
            data.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            data.put("notify_url", payConfig.getNotifyUrl());
            //交易类型为扫码支付
            data.put("trade_type", "NATIVE");

            Map<String, String> resp;

            resp = wxPay.unifiedOrder(data);

            final String SUCCESS = "SUCCESS";
            if (SUCCESS.equals(resp.get("return_code"))) {
                String url = resp.get("code_url");

                try {
                    this.stringRedisTemplate.opsForValue().set(key, url, 10, TimeUnit.MINUTES);
                } catch (Exception e) {
                    log.error("缓存付款链接异常,订单编号：{}", orderId, e);
                }
                return url;
            } else {
                log.error("创建预交易订单失败，错误信息：{}", resp.get("return_msg"));
                return null;
            }

        } catch (Exception e) {
            log.error("支付失败：", e);
            throw new MyException(AirExceptionEnum.PAY_FAIL);
        }

    }

    /**
     * 查询订单支付状态
     * @param orderId
     * @return
     */
    public PayStateEnum queryPageState(Long orderId){

        Map<String, String> data = new HashMap<>();

        data.put("out_trade_no", orderId.toString());

        try {
            Map<String, String> result = wxPay.orderQuery(data);
            if (CollectionUtils.isEmpty(result)){
                log.error("【支付状态查询】链接微信服务失败，订单编号：{}", orderId);
                return PayStateEnum.NOT_PAY;
            }
            if (WXPayConstants.FAIL.equals(result.get("return_code"))){
                log.error("【支付状态查询】查询微信订单支付结果失败，错误码：{}, 订单编号：{}", result.get("result_code"), orderId);
                return PayStateEnum.NOT_PAY;
            }

            isSignatureValid(result);

            String state = result.get("trade_state");

            if (StringUtils.equals(SUCCESS, state)){
                handleNotify(result);
                return PayStateEnum.SUCCESS;
            }else if (StringUtils.equals("USERPAYING", state) || StringUtils.equals("NOTPAY", state)){
                //未支付成功
                return PayStateEnum.NOT_PAY;
            }else {
                //其他返回付款失败
                return PayStateEnum.FAIL;
            }
        } catch (Exception e) {
            log.error("查询订单支付状态异常", e);
            return PayStateEnum.NOT_PAY;
        }
    }

    private void isSignatureValid(Map<String, String> result){
        try {
            boolean bool1 = WXPayUtil.isSignatureValid(result, payConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
            boolean bool2 = WXPayUtil.isSignatureValid(result, payConfig.getKey(), WXPayConstants.SignType.MD5);

            if (!bool1 && !bool2){
                throw new MyException(AirExceptionEnum.WX_PAY_SIGN_INVALID);
            }
        } catch (Exception e) {
            log.error("【微信支付】检验签名失败，数据：{}", result);
            throw new MyException(AirExceptionEnum.WX_PAY_SIGN_INVALID);
        }
    }

    public void handleNotify(Map<String, String> msg){

        isSignatureValid(msg);

        //订单金额
        String totalFee = msg.get("total_fee");
        //订单编号
        String outTradeNo = msg.get("out_trade_no");
        //商户订单号
        String transactionId = msg.get("transaction_id");
        //银行类型
        String bankType = msg.get("bank_type");
        if (StringUtils.isBlank(totalFee) || StringUtils.isBlank(outTradeNo)
        || StringUtils.isBlank(transactionId) || StringUtils.isBlank(bankType)){
            log.error("【微信支付回调】支付回调返回数据不正确");
            throw new MyException(AirExceptionEnum.WX_PAY_NOTIFY_PARAM_ERROR);
        }

        Long outTradeNoL = Long.valueOf(outTradeNo);

        Order order = orderMapper.selectByPrimaryKey(outTradeNoL);

        if (/*order.getActualPay() */1 != Long.valueOf(totalFee)){
            log.error("【微信支付回调】支付回调返回数据不正确");
            throw new MyException(AirExceptionEnum.WX_PAY_NOTIFY_PARAM_ERROR);
        }

        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(outTradeNoL);

        if (!orderStatus.getStatus().equals(OrderStatusEnum.INIT.value())){
            return;
        }

        PayLog payLog = payLogMapper.selectByPrimaryKey(order.getOrderId());

        if (payLog.getStatus() == PayStateEnum.NOT_PAY.getValue()){
            payLog.setBankType(bankType);
            payLog.setPayTime(new Date());
            payLog.setTransactionId(transactionId);
            payLog.setStatus(PayStateEnum.SUCCESS.getValue());
            payLogMapper.updateByPrimaryKey(payLog);
        }

        if (!orderStatus.getStatus().equals(OrderStatusEnum.PAY_UP.value())){
            orderStatus.setStatus(OrderStatusEnum.PAY_UP.value());
            orderStatus.setPaymentTime(new Date());
            orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
        }

    }
}
