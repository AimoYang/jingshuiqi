package com.jingshuiqi.bean;

public class Kuaidi100Delivery {
    private Integer id;

    private String deliveryName;

    private String deliveryCode;

    private String deliverySort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName == null ? null : deliveryName.trim();
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode == null ? null : deliveryCode.trim();
    }

    public String getDeliverySort() {
        return deliverySort;
    }

    public void setDeliverySort(String deliverySort) {
        this.deliverySort = deliverySort == null ? null : deliverySort.trim();
    }
}