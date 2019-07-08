package com.airshop.item.bo;

import com.airshop.item.pojo.Spu;

import javax.persistence.Transient;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:44
 */
public class SpuBO extends Spu {

    @Transient
    private String cname;

    @Transient
    private String bname;

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
}
