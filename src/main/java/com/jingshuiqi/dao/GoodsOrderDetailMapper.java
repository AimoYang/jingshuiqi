package com.jingshuiqi.dao;

import com.jingshuiqi.bean.GoodsOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsOrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsOrderDetail record);

    int insertSelective(GoodsOrderDetail record);

    GoodsOrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsOrderDetail record);

    int updateByPrimaryKey(GoodsOrderDetail record);

    List<GoodsOrderDetail> selectByOrderUuid(@Param("orderUuid") String orderUuid);

    GoodsOrderDetail selectByUuid(@Param("uuid")String uuid);
}