package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrderDetail;
import com.jingshuiqi.util.PageObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsOrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrderDetail record);

    int insertSelective(GoodsOrderDetail record);

    GoodsOrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrderDetail record);

    int updateByPrimaryKey(GoodsOrderDetail record);

    List<GoodsOrderDetail> findOrderDetail(@Param("uuid") String uuid);

    int getUserOrdersCommentRow(@Param("pageObject") PageObject pageObject);

    List<GoodsOrderDetail> findOrderCommentDetail(@Param("pageObject")PageObject pageObject);

    List<GoodsOrderDetail> findCancelOrders(@Param("pageObject")PageObject pageObject);

    int getCancelOrdersRow(@Param("pageObject")PageObject pageObject);
}