package com.airshop.order.service.impl;

import com.airshop.auth.entity.UserInfo;
import com.airshop.interceptor.LoginInterceptor;
import com.airshop.item.client.GoodsClient;
import com.airshop.item.dto.CartDTO;
import com.airshop.item.pojo.Sku;
import com.airshop.myexception.AirExceptionEnum;
import com.airshop.myexception.MyException;
import com.airshop.order.client.AddressClient;
import com.airshop.order.dto.AddressDTO;
import com.airshop.order.dto.OrderDTO;
import com.airshop.order.dto.OrderStatusEnum;
import com.airshop.order.mapper.OrderDetailMapper;
import com.airshop.order.mapper.OrderMapper;
import com.airshop.order.mapper.OrderStatusMapper;
import com.airshop.order.pojo.Order;
import com.airshop.order.pojo.OrderDetail;
import com.airshop.order.pojo.OrderStatus;
import com.airshop.order.service.OrderService;
import com.airshop.order.service.PayLogService;
import com.airshop.order.utils.PayHelper;
import com.airshop.utils.IdWorker;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:45
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private PayHelper payHelper;
    @Autowired
    private PayLogService payLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createOrder(OrderDTO orderDTO) {

        //生成订单ID，采用自己的算法生成订单ID
        long orderId = idWorker.nextId();
        //填充order，订单中的用户信息数据从Token中获取，填充到order中
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setOrderId(orderId);
        order.setPaymentType(orderDTO.getPaymentType());
        order.setPostFee(0L);

        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        order.setUserId(userInfo.getId());
        order.setBuyerNick(userInfo.getUsername());
        order.setBuyerRate(false);

        //收货人地址信息，应该从数据库中物流信息中获取，这里使用的是假的数据
        AddressDTO addressDTO = AddressClient.findById(orderDTO.getAddressId());
        if (addressDTO == null){
            throw new MyException(AirExceptionEnum.RECEIVER_ADDRESS_NOT_FOUND);
        }
        order.setReceiver(addressDTO.getName());
        order.setReceiverAddress(addressDTO.getAddress());
        order.setReceiverCity(addressDTO.getCity());
        order.setReceiverDistrict(addressDTO.getDistrict());
        order.setReceiverMobile(addressDTO.getPhone());
        order.setReceiverZip(addressDTO.getZipCode());
        order.setReceiverState(addressDTO.getState());

        Map<Long, Integer> skuNumMap = orderDTO.getCarts().stream()
                .collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));


        List<Sku> skuList = goodsClient.querySkusBySkuIds(new ArrayList<>(skuNumMap.keySet())).getBody();

        Long totalPay = 0L;

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Sku sku : skuList) {
            Integer num = skuNumMap.get(sku.getId());
            totalPay += num* sku.getPrice();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetail.setNum(num);
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(), ","));

            orderDetailList.add(orderDetail);
        }

        order.setActualPay(totalPay + order.getPostFee());
        order.setTotalPay(totalPay);

        orderMapper.insertSelective(order);

        orderDetailMapper.insertList(orderDetailList);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.INIT.value());
        orderStatus.setCreateTime(new Date());

        orderStatusMapper.insertSelective(orderStatus);

        goodsClient.decreaseStock(orderDTO.getCarts());

        return orderId;
    }

    @Override
    public Order queryById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);

        if (order == null){
            throw new MyException(AirExceptionEnum.ORDER_NOT_FOUND);
        }

        Example detailExample = new Example(OrderDetail.class);

        detailExample.createCriteria().andEqualTo("orderId", orderId);

        List<OrderDetail> orderDetailList = orderDetailMapper.selectByExample(detailExample);

        order.setOrderDetails(orderDetailList);

        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);

        order.setOrderStatus(orderStatus);

        return order;
    }

    @Override
    public String generateUrl(Long orderId) {

        Order order = queryById(orderId);

        if (!order.getOrderStatus().getStatus().equals(OrderStatusEnum.INIT.value())){
            throw new MyException(AirExceptionEnum.ORDER_STATUS_EXCEPTION);
        }

        String url = payHelper.createPayUrl(orderId, "商城测试", 1L);

        if (StringUtils.isBlank(url)){
            throw new MyException(AirExceptionEnum.CREATE_PAY_URL_ERROR);
        }

        //生成支付日志
        payLogService.createPayLog(orderId, order.getActualPay());

        return url;
    }


}
