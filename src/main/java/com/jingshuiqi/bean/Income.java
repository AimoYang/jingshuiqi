package com.jingshuiqi.bean;

public class Income {
    private Integer id;

    private String openId;

    private Double incomeAll;

    private Double remain;

    private Double waitCash;

    private Double alreadyCash;

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

    public Double getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(Double incomeAll) {
        this.incomeAll = incomeAll;
    }

    public Double getRemain() {
        return remain;
    }

    public void setRemain(Double remain) {
        this.remain = remain;
    }

    public Double getWaitCash() {
        return waitCash;
    }

    public void setWaitCash(Double waitCash) {
        this.waitCash = waitCash;
    }

    public Double getAlreadyCash() {
        return alreadyCash;
    }

    public void setAlreadyCash(Double alreadyCash) {
        this.alreadyCash = alreadyCash;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }
}