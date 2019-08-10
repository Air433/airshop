package com.airshop.order.dto;

import com.airshop.item.dto.CartDTO;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:55
 */
public class OrderDTO {

    @NotNull
    private Long addressId;
    @NotNull
    private Integer paymentType;
    @NotNull
    private List<CartDTO> carts;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public List<CartDTO> getCarts() {
        return carts;
    }

    public void setCarts(List<CartDTO> carts) {
        this.carts = carts;
    }
}
