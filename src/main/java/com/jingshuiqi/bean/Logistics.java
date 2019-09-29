package com.jingshuiqi.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Logistics {
    private Integer id;

    private String expressageName;

    private String expressageCom;

    private String expressageId;

    private String goodsOrderUuid;

    private Double money;

    private String reserve;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpressageName() {
        return expressageName;
    }

    public void setExpressageName(String expressageName) {
        this.expressageName = expressageName == null ? null : expressageName.trim();
    }

    public String getExpressageCom() {
        return expressageCom;
    }

    public void setExpressageCom(String expressageCom) {
        this.expressageCom = expressageCom == null ? null : expressageCom.trim();
    }

    public String getExpressageId() {
        return expressageId;
    }

    public void setExpressageId(String expressageId) {
        this.expressageId = expressageId == null ? null : expressageId.trim();
    }

    public String getGoodsOrderUuid() {
        return goodsOrderUuid;
    }

    public void setGoodsOrderUuid(String goodsOrderUuid) {
        this.goodsOrderUuid = goodsOrderUuid == null ? null : goodsOrderUuid.trim();
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}