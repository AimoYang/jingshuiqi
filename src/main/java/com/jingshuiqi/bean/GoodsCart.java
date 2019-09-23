package com.jingshuiqi.bean;

public class GoodsCart {
    private Integer id;

    private String openId;

    private String goodsUuid;

    private String skuUuid;

    private Integer quantity;

    private Short isDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getGoodsUuid() {
        return goodsUuid;
    }

    public void setGoodsUuid(String goodsUuid) {
        this.goodsUuid = goodsUuid == null ? null : goodsUuid.trim();
    }

    public String getSkuUuid() {
        return skuUuid;
    }

    public void setSkuUuid(String skuUuid) {
        this.skuUuid = skuUuid == null ? null : skuUuid.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}