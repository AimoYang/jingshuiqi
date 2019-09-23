package com.jingshuiqi.vo;

import com.jingshuiqi.bean.GoodsCart;
import lombok.Data;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 19:24
 * @Description:
 */
@Data
public class ShopGoodsInfo extends GoodsCart {

    private String goodsName;

    private String thumb;

    private double oldPrice;

    private double newPrice;

    private String properties;

    private Short isShelf;
}
