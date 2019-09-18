package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrderDetail;

public interface GoodsOrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrderDetail record);

    int insertSelective(GoodsOrderDetail record);

    GoodsOrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrderDetail record);

    int updateByPrimaryKey(GoodsOrderDetail record);
}