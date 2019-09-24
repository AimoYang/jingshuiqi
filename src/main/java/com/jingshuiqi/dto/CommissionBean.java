package com.jingshuiqi.dto;

/**
 * @Author Administrator
 * @create 2019/9/22 0022 16:14
 */
public class CommissionBean {
    private Integer id;

    private String openId;

    private Double allMoney;

    private Double remain;

    private Double waitMoney;

    private Double alreadyMoney;

    private Double commonMoney;

    private Double cityMoney;

    private Double teamMoney;

    private String reserve;

    private Integer isDelete;

    private Double waitCalculate;

    public Double getWaitCalculate() {
        return waitCalculate;
    }

    public void setWaitCalculate(Double waitCalculate) {
        this.waitCalculate = waitCalculate;
    }

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

    public Double getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(Double allMoney) {
        this.allMoney = allMoney;
    }

    public Double getRemain() {
        return remain;
    }

    public void setRemain(Double remain) {
        this.remain = remain;
    }

    public Double getWaitMoney() {
        return waitMoney;
    }

    public void setWaitMoney(Double waitMoney) {
        this.waitMoney = waitMoney;
    }

    public Double getAlreadyMoney() {
        return alreadyMoney;
    }

    public void setAlreadyMoney(Double alreadyMoney) {
        this.alreadyMoney = alreadyMoney;
    }

    public Double getCommonMoney() {
        return commonMoney;
    }

    public void setCommonMoney(Double commonMoney) {
        this.commonMoney = commonMoney;
    }

    public Double getCityMoney() {
        return cityMoney;
    }

    public void setCityMoney(Double cityMoney) {
        this.cityMoney = cityMoney;
    }

    public Double getTeamMoney() {
        return teamMoney;
    }

    public void setTeamMoney(Double teamMoney) {
        this.teamMoney = teamMoney;
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
