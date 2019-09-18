package com.jingshuiqi.bean;

public class Sku {
    private Integer id;

    private String uuid;

    private String goodsUuid;

    private Integer stock;

    private String properties;

    private Double oldPrice;

    private Double newPrice;

    private Double oneCommission;

    private Double twoCommission;

    private Double cityCommission;

    private Double topCommission;

    private String reserve;

    private Integer isDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getGoodsUuid() {
        return goodsUuid;
    }

    public void setGoodsUuid(String goodsUuid) {
        this.goodsUuid = goodsUuid == null ? null : goodsUuid.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties == null ? null : properties.trim();
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public Double getOneCommission() {
        return oneCommission;
    }

    public void setOneCommission(Double oneCommission) {
        this.oneCommission = oneCommission;
    }

    public Double getTwoCommission() {
        return twoCommission;
    }

    public void setTwoCommission(Double twoCommission) {
        this.twoCommission = twoCommission;
    }

    public Double getCityCommission() {
        return cityCommission;
    }

    public void setCityCommission(Double cityCommission) {
        this.cityCommission = cityCommission;
    }

    public Double getTopCommission() {
        return topCommission;
    }

    public void setTopCommission(Double topCommission) {
        this.topCommission = topCommission;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}