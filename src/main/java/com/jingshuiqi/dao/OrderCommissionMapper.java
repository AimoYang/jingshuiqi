package com.jingshuiqi.dao;

import com.jingshuiqi.bean.OrderCommission;
import org.apache.ibatis.annotations.Param;

public interface OrderCommissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCommission record);

    int insertSelective(OrderCommission record);

    OrderCommission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderCommission record);

    int updateByPrimaryKey(OrderCommission record);

    Double sumOneWaitCalculate(@Param("openId") String openId);

    Double sumTwoWaitCalculate(@Param("openId") String openId);

    OrderCommission selectByOrderDetailUuid(@Param("orderDetailUuid") String orderDetailUuid);
}