package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Goods;
import com.jingshuiqi.bean.GoodsWithBLOBs;
import com.jingshuiqi.util.PageObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsWithBLOBs record);

    int insertSelective(GoodsWithBLOBs record);

    GoodsWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GoodsWithBLOBs record);

    int updateByPrimaryKey(Goods record);

    Goods findGoodsInfoByUuid(@Param("uuid") String uuid);

    List<Goods> findCollectInfo(@Param("pageObject") PageObject pageObject);

    int getCollectInfoRow(@Param("pageObject")PageObject pageObject);
}