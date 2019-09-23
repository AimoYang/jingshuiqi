package com.jingshuiqi.vo;

import com.jingshuiqi.bean.FloorBind;
import lombok.Data;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 17:30
 * @Description:
 */
@Data
public class IndexFloorInfo extends FloorBind {

    //弃用

    private Integer state;

    private String goodsName;

    private Integer showCount;

    private String thumb;

    private double price;
}
