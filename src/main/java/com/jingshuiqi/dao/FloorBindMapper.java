package com.jingshuiqi.dao;

import com.jingshuiqi.bean.FloorBind;

public interface FloorBindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FloorBind record);

    int insertSelective(FloorBind record);

    FloorBind selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FloorBind record);

    int updateByPrimaryKey(FloorBind record);
}