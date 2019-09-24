package com.jingshuiqi.dto;

import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.bean.GoodsOrderDetail;
import lombok.Data;

import java.util.List;

/**
 * @Author Administrator
 * @create 2019/9/23 0023 17:55
 */
@Data
public class GoodsOrderBean extends GoodsOrder {
    private String nickName;

    private String icon;

    private Double agentCommission;

    private Double cityCommission;

    private Double topCommission;

    private String detailUuid;

    private  List<GoodsOrderDetail> list;
}
