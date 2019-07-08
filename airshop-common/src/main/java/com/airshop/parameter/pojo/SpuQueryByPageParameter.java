package com.airshop.parameter.pojo;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:54
 */
public class SpuQueryByPageParameter extends BrandQueryByPageParameter{
    /**
     *         - page：当前页，int
     *         - rows：每页大小，int
     *         - sortBy：排序字段，String
     *         - desc：是否为降序，boolean
     *         - key：搜索关键词，String
     *         - saleable: 是否上下架
     */
    private Boolean saleable = true;

    public Boolean getSaleable() {
        return saleable;
    }

    public void setSaleable(Boolean saleable) {
        this.saleable = saleable;
    }

    public SpuQueryByPageParameter(){

    }

    public SpuQueryByPageParameter(Integer page, Integer rows, String sortBy, Boolean desc, String key, Boolean saleable) {
        super(page, rows, sortBy, desc, key);
        this.saleable = saleable;
    }

    public SpuQueryByPageParameter(Boolean saleable) {
        this.saleable = saleable;
    }

    @Override
    public String toString() {
        return "SpuQueryByPageParameter{" +
                "saleable=" + saleable +
                '}';
    }
}
