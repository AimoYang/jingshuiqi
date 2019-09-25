package com.jingshuiqi.dao;

import com.jingshuiqi.bean.Logistics;
import org.apache.ibatis.annotations.Param;

public interface LogisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Logistics record);

    int insertSelective(Logistics record);

    Logistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Logistics record);

    int updateByPrimaryKey(Logistics record);

    Logistics findDelivery(@Param("uuid") String uuid);
}