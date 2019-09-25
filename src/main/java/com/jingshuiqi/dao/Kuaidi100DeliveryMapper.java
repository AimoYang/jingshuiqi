package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Kuaidi100Delivery;

public interface Kuaidi100DeliveryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Kuaidi100Delivery record);

    int insertSelective(Kuaidi100Delivery record);

    Kuaidi100Delivery selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Kuaidi100Delivery record);

    int updateByPrimaryKey(Kuaidi100Delivery record);
}