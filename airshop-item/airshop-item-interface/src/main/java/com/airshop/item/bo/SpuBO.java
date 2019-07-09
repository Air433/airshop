package com.airshop.item.bo;

import com.airshop.item.pojo.Sku;
import com.airshop.item.pojo.Spu;
import com.airshop.item.pojo.SpuDetail;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:44
 */
public class SpuBO extends Spu {

    /**
     * 商品分类名称
     */
    @Transient
    private String cname;

    /**
     * 品牌名称
     */
    @Transient
    private String bname;

    /**
     * 商品详情
     */
    @Transient
    private SpuDetail spuDetail;

    /**
     * sku列表
     */
    @Transient
    private List<Sku> skus;

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public SpuBO() {
    }

    public SpuBO(Long brandId, Long cid1, Long cid2, Long cid3, String title, String subTitle, Boolean saleable, Boolean valid, Date createTime, Date lastUpdateTime) {
        super(brandId, cid1, cid2, cid3, title, subTitle, saleable, valid, createTime, lastUpdateTime);
    }
}
