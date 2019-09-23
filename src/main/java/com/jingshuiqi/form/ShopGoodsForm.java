package com.jingshuiqi.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 19:22
 * @Description:
 */
@Data
public class ShopGoodsForm {

    @NotNull
    private String goodsUuid;

    @NotNull
    private String skuUuid;

    @NotNull
    private Integer quantity;

}
