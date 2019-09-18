package com.jingshuiqi.bean;

public class GoodsWithBLOBs extends Goods {
    private String details;

    private String specification;

    private String afterSales;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification == null ? null : specification.trim();
    }

    public String getAfterSales() {
        return afterSales;
    }

    public void setAfterSales(String afterSales) {
        this.afterSales = afterSales == null ? null : afterSales.trim();
    }
}