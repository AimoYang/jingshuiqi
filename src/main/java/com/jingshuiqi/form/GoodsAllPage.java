package com.jingshuiqi.form;

import com.jingshuiqi.util.PageObject;
import lombok.Data;

/**
 * @Auther: Mr.Yang
 * @Date: 2019/9/22 0022 18:40
 * @Description:
 */
@Data
public class GoodsAllPage extends PageObject {

    private Integer express;

    private short state;

    private String time;

    private String price;

    private String show;

    private String name;

}
