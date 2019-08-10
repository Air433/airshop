package com.airshop.order.utils;

import com.airshop.myexception.AirExceptionEnum;
import com.airshop.myexception.MyException;
import com.airshop.order.config.PayConfig;
import com.airshop.order.dto.PayStateEnum;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
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

            }
        } catch (Exception e) {
            log.error("查询订单支付状态异常", e);
            return null;
        }

        return null;
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


    }
}
