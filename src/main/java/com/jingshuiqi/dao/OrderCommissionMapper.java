package com.jingshuiqi.dao;

import com.jingshuiqi.bean.OrderCommission;

public interface OrderCommissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderCommission record);

    int insertSelective(OrderCommission record);

    OrderCommission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderCommission record);

    int updateByPrimaryKey(OrderCommission record);
}