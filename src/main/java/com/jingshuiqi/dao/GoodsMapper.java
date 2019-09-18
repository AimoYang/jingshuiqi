package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Goods;
import com.jingshuiqi.bean.GoodsWithBLOBs;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsWithBLOBs record);

    int insertSelective(GoodsWithBLOBs record);

    GoodsWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GoodsWithBLOBs record);

    int updateByPrimaryKey(Goods record);
}