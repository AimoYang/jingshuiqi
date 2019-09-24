package com.jingshuiqi.vo;

import com.jingshuiqi.bean.Sku;
import lombok.Data;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/23 0023 20:26
 * @Description:
 */
@Data
public class OrderGoods {

    private String uuid;

    private String goodsName;

    private String thumb;

    private Double freight;

    private Integer deductionMax;

    private Integer quantity;

    private Sku sku;
}
