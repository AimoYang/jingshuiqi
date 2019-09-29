package com.jingshuiqi.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AgentCommission {
    private Integer id;

    private String orderDetailUuid;

    private Integer type;

    private String agentOpenId;

    private Double commission;

    private Double actualCommission;

    private Short isSuccess;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAgentOpenId() {
        return agentOpenId;
    }

    public void setAgentOpenId(String agentOpenId) {
        this.agentOpenId = agentOpenId == null ? null : agentOpenId.trim();
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getActualCommission() {
        return actualCommission;
    }

    public void setActualCommission(Double actualCommission) {
        this.actualCommission = actualCommission;
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
        return "AgentCommission{" +
                "id=" + id +
                ", orderDetailUuid='" + orderDetailUuid + '\'' +
                ", type=" + type +
                ", agentOpenId='" + agentOpenId + '\'' +
                ", commission=" + commission +
                ", actualCommission=" + actualCommission +
                ", isSuccess=" + isSuccess +
                ", createTime=" + createTime +
                ", doTime=" + doTime +
                ", doType=" + doType +
                ", reserve='" + reserve + '\'' +
                ", reserve1='" + reserve1 + '\'' +
                '}';
    }
}