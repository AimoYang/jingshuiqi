package com.jingshuiqi.vo;

import lombok.Data;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 19:24
 * @Description:
 */
@Data
public class ShopGoodsInfo {

    private Integer id;

    private String goodsUuid;

    private Integer skuUuid;

    private Integer amount;

    private String goodsName;

    private String thumb;

    private double oldPrice;

    private double newPrice;

    private String properties;

    private Short isShelf;
}
