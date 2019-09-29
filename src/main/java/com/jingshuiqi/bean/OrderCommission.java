package com.jingshuiqi.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class OrderCommission {
    private Integer id;

    private String orderDetailUuid;

    private String parentOpenId;

    private String grandpaOpenId;

    private Double parentCommission;

    private Double grandpaCommission;

    private Double actualParentCommission;

    private Double actualGrandpaCommission;

    private Short isSuccess;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    private Date doTime;

    private Short doType;

    private String reserve;

    private String reserve1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderDetailUuid() {
        return orderDetailUuid;
    }

    public void setOrderDetailUuid(String orderDetailUuid) {
        this.orderDetailUuid = orderDetailUuid == null ? null : orderDetailUuid.trim();
    }

    public String getParentOpenId() {
        return parentOpenId;
    }

    public void setParentOpenId(String parentOpenId) {
        this.parentOpenId = parentOpenId == null ? null : parentOpenId.trim();
    }

    public String getGrandpaOpenId() {
        return grandpaOpenId;
    }

    public void setGrandpaOpenId(String grandpaOpenId) {
        this.grandpaOpenId = grandpaOpenId == null ? null : grandpaOpenId.trim();
    }

    public Double getParentCommission() {
        return parentCommission;
    }

    public void setParentCommission(Double parentCommission) {
        this.parentCommission = parentCommission;
    }

    public Double getGrandpaCommission() {
        return grandpaCommission;
    }

    public void setGrandpaCommission(Double grandpaCommission) {
        this.grandpaCommission = grandpaCommission;
    }

    public Double getActualParentCommission() {
        return actualParentCommission;
    }

    public void setActualParentCommission(Double actualParentCommission) {
        this.actualParentCommission = actualParentCommission;
    }

    public Double getActualGrandpaCommission() {
        return actualGrandpaCommission;
    }

    public void setActualGrandpaCommission(Double actualGrandpaCommission) {
        this.actualGrandpaCommission = actualGrandpaCommission;
    }

    public Short getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Short isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDoTime() {
        return doTime;
    }

    public void setDoTime(Date doTime) {
        this.doTime = doTime;
    }

    public Short getDoType() {
        return doType;
    }

    public void setDoType(Short doType) {
        this.doType = doType;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1 == null ? null : reserve1.trim();
    }


    @Override
    public String toString() {
        return "OrderCommission{" +
                "id=" + id +
                ", orderDetailUuid='" + orderDetailUuid + '\'' +
                ", parentOpenId='" + parentOpenId + '\'' +
                ", grandpaOpenId='" + grandpaOpenId + '\'' +
                ", parentCommission=" + parentCommission +
                ", grandpaCommission=" + grandpaCommission +
                ", actualParentCommission=" + actualParentCommission +
                ", actualGrandpaCommission=" + actualGrandpaCommission +
                ", isSuccess=" + isSuccess +
                ", createTime=" + createTime +
                ", doTime=" + doTime +
                ", doType=" + doType +
                ", reserve='" + reserve + '\'' +
                ", reserve1='" + reserve1 + '\'' +
                '}';
    }
}