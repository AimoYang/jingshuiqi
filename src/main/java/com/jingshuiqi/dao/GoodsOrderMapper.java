package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrder;
import com.jingshuiqi.util.PageObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrder record);

    int insertSelective(GoodsOrder record);

    GoodsOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrder record);

    int updateByPrimaryKey(GoodsOrder record);

    GoodsOrder findOrdersByUuid(@Param("uuid") String uuid);

    List<GoodsOrder> findUserOrdersInfo(@Param("pageObject") PageObject pageObject);

    int getUserOrdersRow(@Param("pageObject")PageObject pageObject);
}