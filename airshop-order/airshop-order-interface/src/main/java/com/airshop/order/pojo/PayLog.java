package com.airshop.order.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 19:02
 */
@Table(name = "tb_pay_log")
public class PayLog {

    @Id
    private Long orderId;
    private Long totalFee;
    private Long userId;
    private String transactionId;
    private Integer status;
    private Integer payType;
    private String bankType;
    private Date createTime;
    private Date payTime;
    private Date refundTime;
    private Date closedTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(Date closedTime) {
        this.closedTime = closedTime;
    }
}
