package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrder;

public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);

    GoodsOrder findOrdersByUuid(String uuid);
}