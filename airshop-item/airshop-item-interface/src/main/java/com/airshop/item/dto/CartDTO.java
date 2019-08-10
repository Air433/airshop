package com.airshop.item.dto;

/**
 * @Author ouyanggang
 * @Date 2019/8/5 - 17:50
 */
public class CartDTO {

    private Long skuId;

    private Integer num;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
